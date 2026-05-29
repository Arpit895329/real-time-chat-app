package com.chat.app.dto;

import lombok.Data;

@Data
public class RoomRequest {
 private String roomName;
 private String username;
 private boolean locked;
 private String password;
}
