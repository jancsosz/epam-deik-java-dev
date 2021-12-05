package com.epam.training.ticketservice.room;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {


    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;


    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom = Room.builder()
                .name("test")
                .cols(1)
                .rows(1)
                .build();
    }

    @Test
    public void testGetAllRoomsShouldReturnListOfRooms() {

        //Given
        List<Room> expectedList = List.of(testRoom);

        //When
        when(roomRepository.findAll()).thenReturn(expectedList);
        List<Room> actualList = roomService.getAllRooms();

        //Then
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCreateRoom() throws CustomException {

        //Given

        //When
        roomService.createRoom(testRoom);

        //Then
        verify(roomRepository, times(1)).save(testRoom);
    }

    @Test
    public void testCreateRoomShouldThrowExceptionIfRoomAlreadyExist() throws CustomException {

        //Given

        //When
        when(roomRepository.existsByName(testRoom.getName())).thenReturn(true);

        //Then
        assertThrows(CustomException.class, () -> roomService.createRoom(testRoom));
        verify(roomRepository, times(0)).save(testRoom);

    }

    @Test
    public void testUpdateRoom() throws NotFoundException {

        //Given


        //When
        when(roomRepository.existsByName(testRoom.getName())).thenReturn(true);
        roomService.updateRoom(testRoom);

        //Then
        verify(roomRepository, times(1)).update(testRoom.getName(),
                testRoom.getCols(),
                testRoom.getRows());
    }

    @Test
    public void testUpdateRoomShouldThrowNotFoundExceptionWhenExistByNameReturnsFalse() {

        //Given


        //When
        when(roomRepository.existsByName(testRoom.getName())).thenReturn(false);

        //Then
        assertThrows(NotFoundException.class, () -> roomService.updateRoom(testRoom));
        verify(roomRepository, times(0)).update(testRoom.getName(),
                testRoom.getCols(),
                testRoom.getRows());
    }

    @Test
    public void testDeleteRoom() throws NotFoundException {

        //Given


        //When
        when(roomRepository.existsByName(testRoom.getName())).thenReturn(true);
        roomService.deleteRoom(testRoom.getName());

        //Then
        verify(roomRepository, times(1)).deleteByName(testRoom.getName());
    }

    @Test
    public void testDeleteRoomShouldThrowNotFoundExceptionWhenExistByNameReturnsFalse() {

        //Given


        //When
        when(roomRepository.existsByName(testRoom.getName())).thenReturn(false);

        //Then
        assertThrows(NotFoundException.class, () -> roomService.deleteRoom(testRoom.getName()));
        verify(roomRepository, times(0)).deleteByName(testRoom.getName());
    }

    @Test
    public void testFindByName() throws NotFoundException {
        // Given

        // When
        when(roomRepository.findByName(testRoom.getName())).thenReturn(testRoom);
        roomService.findByName(testRoom.getName());

        // Then
        verify(roomRepository, times(1)).findByName(testRoom.getName());
    }

    @Test
    public void testFindByNameShouldThrowNotFoundExceptionIfRoomDoesNotExistWithSuchName() {

        // Given

        // When
        when(roomRepository.findByName(testRoom.getName())).thenReturn(null);

        // Then
        assertThrows(NotFoundException.class, () -> roomService.findByName(testRoom.getName()));

    }

    @Test
    public void testMapToRoomShouldReturnRoomObjectWithGivenProperties() {

        // Given
        int columns = testRoom.getCols();
        int rows = testRoom.getRows();
        String roomName = testRoom.getName();
        Room expectedRoom = testRoom;

        // When
        Room actualRoom = roomService.mapToRoom(roomName, rows, columns);

        // Then
        assertEquals(expectedRoom, actualRoom);

    }

}
