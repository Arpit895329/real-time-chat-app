package com.chat.app.service;

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
        return roomRepository.save(room);
    }
}
