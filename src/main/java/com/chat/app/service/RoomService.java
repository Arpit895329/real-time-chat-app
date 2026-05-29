package com.chat.app.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chat.app.dto.RoomRequest;
import com.chat.app.model.Room;
import com.chat.app.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(RoomRequest request) {
        String roomId = UUID.randomUUID().toString().substring(0, 6);
        Room room = new Room();
        room.setRoomId(roomId);
        room.setRoomName(request.getRoomName());
        room.setCreatedBy(request.getUsername());
        room.setLocked(request.isLocked());
        room.setPassword(request.isLocked() ? request.getPassword() : null);
        return roomRepository.save(room);
    }

    public boolean roomExists(String roomId) {
        return roomRepository.existsByRoomId(roomId);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoom(String roomId) {
        return roomRepository.findByRoomId(roomId);
    }

    public boolean isPasswordValid(String roomId, String password) {
        Room room = getRoom(roomId);
        if (room == null) {
            return false;
        }
        if (!room.isLocked()) {
            return true;
        }
        return Objects.equals(room.getPassword(), password);
    }
}
