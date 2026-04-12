package com.chat.app.repository;

import com.chat.app.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
   boolean existsByRoomId(String roomId);

    Room findByRoomId(String roomId);
}