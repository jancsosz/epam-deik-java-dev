package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.exception.CustomException;
import com.epam.training.ticketservice.model.Room;
import com.epam.training.ticketservice.repository.RoomRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room findByName(String name) throws NotFoundException {

        Room room = roomRepository.findByNameContainingIgnoreCase(name);

        if (room != null) {
            return room;
        } else {
            throw new NotFoundException("No room found with such name");
        }
    }

    public Room mapToRoom(String name, int rows, int columns) {
        return Room.builder()
                .name(name)
                .rows(rows)
                .cols(columns)
                .build();
    }

    public void createRoom(Room room) throws CustomException {
        if (!roomRepository.existsByNameContainingIgnoreCase(room.getName())) {
            roomRepository.save(room);
        } else {
            throw new CustomException("Room already exists with such name");
        }
    }

    public void updateRoom(Room room) throws NotFoundException {
        if (roomRepository.existsByNameContainingIgnoreCase(room.getName())) {
            roomRepository.update(room.getName(),
                    room.getCols(),
                    room.getRows());
        } else {
            throw new NotFoundException("No room found with such name");
        }
    }

    public void deleteRoom(String name) throws NotFoundException {
        if (roomRepository.existsByNameContainingIgnoreCase(name)) {
            roomRepository.deleteByNameContainingIgnoreCase(name);
        } else {
            throw new NotFoundException("No room found with such name");
        }
    }

}
