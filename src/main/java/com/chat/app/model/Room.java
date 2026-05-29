package com.chat.app.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Data
public class Room {
 
    @Id
    String roomId;
    String roomName;
    String createdBy;
    boolean locked;
    String password;

}
