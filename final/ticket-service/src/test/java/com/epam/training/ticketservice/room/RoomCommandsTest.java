package com.epam.training.ticketservice.room;

import com.epam.training.ticketservice.commands.RoomCommands;
import com.epam.training.ticketservice.exception.CustomException;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.repository.RoomRepository;
import com.epam.training.ticketservice.service.RoomService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomCommandsTest {

    @Mock
    RoomService roomService;

    @Mock
    RoomRepository roomRepository;

    @InjectMocks
    RoomCommands roomCommands;

    private Room room;

    @BeforeEach
    void setUp() {
        room = Room.builder()
                .name("room")
                .rows(20)
                .cols(30)
                .build();
    }


    @Test
    public void testCreateRoomShouldPersistRoomWhenRoomDoesntExist() throws CustomException {

        // Given


        // When
        when(roomService.mapToRoom(room.getName(), room.getRows(), room.getCols())).thenReturn(room);
        roomCommands.createRoom(room.getName(), room.getRows(), room.getCols());

        // Then
        verify(roomService, times(1)).createRoom(any(Room.class));
    }

    @Test
    public void testCreateRoomShouldNotCreateRoomWhenCustomExceptionThrown()
            throws CustomException {

        // Given


        // When
        when(roomService.mapToRoom(room.getName(), room.getRows(), room.getCols())).thenReturn(room);
        doThrow(CustomException.class).when(roomService).createRoom(any(Room.class));
        roomCommands.createRoom(room.getName(), room.getRows(), room.getCols());

        // Then
        verify(roomRepository, times(0)).save(any(Room.class));
    }


    @Test
    public void testUpdateRoomShouldUpdateRoomWhenRoomFound() throws NotFoundException {

        // Given


        // When
        when(roomService.mapToRoom(room.getName(), room.getRows(), room.getCols())).thenReturn(room);
        roomCommands.updateRoom(room.getName(), room.getRows(), room.getCols());

        // Then
        verify(roomService, times(1)).updateRoom(any(Room.class));
    }

    @Test
    public void testUpdateRoomShouldNotUpdateRoomWhenCustomExceptionThrown()
            throws NotFoundException {

        // Given


        // When
        when(roomService.mapToRoom(room.getName(), room.getRows(), room.getCols())).thenReturn(room);
        doThrow(NotFoundException.class).when(roomService).updateRoom(any(Room.class));
        roomCommands.updateRoom(room.getName(), room.getRows(), room.getCols());

        // Then
        verify(roomRepository, times(0)).update(anyString(), anyInt(), anyInt());
    }

    @Test
    public void testDeleteRoomShouldDeleteRoomWhenRoomExists() throws NotFoundException {

        // Given


        // When
        roomCommands.deleteRoom(room.getName());

        // Then
        verify(roomService, times(1)).deleteRoom(room.getName());
    }

    @Test
    public void testDeleteRoomShouldNotDeleteRoomIfNotFoundExceptionThrown()
            throws NotFoundException {

        // Given


        // When
        doThrow(NotFoundException.class).when(roomService).deleteRoom(room.getName());
        roomCommands.deleteRoom(room.getName());

        // Then
        verify(roomRepository, times(0)).deleteByName(room.getName());
    }

    @Test
    public void testListRoomsShouldReturnExpectedStringIfNoRoomsAreFound() {

        // Given
        String expectedString = "There are no rooms at the moment";

        // When
        when(roomService.getAllRooms()).thenReturn(Collections.emptyList());
        String actualString = roomCommands.listRooms();

        // Then
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testListRoomsShouldReturnStringWithFoundRooms() {

        // Given
        Room testRoom1 = Room.builder()
                .name("room1")
                .cols(20)
                .rows(30)
                .build();


        Room testRoom2 = Room.builder()
                .name("room2")
                .cols(20)
                .rows(30)
                .build();

        String expectedString = testRoom1 + "\n" + testRoom2;

        // When
        when(roomService.getAllRooms()).thenReturn(List.of(testRoom1, testRoom2));
        String actualString = roomCommands.listRooms();

        // Then
        assertEquals(expectedString, actualString);
    }


}
