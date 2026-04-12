package com.chat.app.controller;
import com.chat.app.dto.RoomRequest;
import com.chat.app.model.ChatMessage;
import com.chat.app.model.Room;
import com.chat.app.service.RoomService;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ChatController {

    private final RoomService roomService;

    public ChatController(RoomService roomService) {
        this.roomService = roomService;
    }

@MessageMapping("/sendMessage")
@SendTo("/topic/messages")
public ChatMessage sendMessage(ChatMessage message){
    return message;
}
@GetMapping("/chat")
public String chat(@RequestParam String username, Model model){
    model.addAttribute("username", username);
    return "chat";
}
@GetMapping("/create")
public String create(){
    return "create";
}

@GetMapping("/join")
public String join(){
    return "join";
}

// @PostMapping("/create-room")
// public ResponseEntity<Room> createRoom(@RequestBody RoomRequest request) {
//     return ResponseEntity.ok(roomService.createRoom(request));
// }

@PostMapping("/create-room")
public String createRoomFromForm(@RequestParam String roomName,@RequestParam String username,Model model){ 
    RoomRequest request = new RoomRequest();
    request.setRoomName(roomName);
    request.setUsername(username);

    Room room = roomService.createRoom(request);

    // send data to next page
    model.addAttribute("roomId", room.getRoomId());
    model.addAttribute("username", username);
    return "chat";}

@GetMapping("/create-room")
public String showCreateRoomForm() {
    return "chat";
}

@GetMapping("/")
public String home() {
    return "landing";   
}

@GetMapping("/ping")
public void ping(jakarta.servlet.http.HttpServletResponse response) throws Exception {
    response.getWriter().write("OK");
}

}
