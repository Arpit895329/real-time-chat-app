package com.chat.app.controller;
import com.chat.app.dto.RoomRequest;
import com.chat.app.model.ChatMessage;
import com.chat.app.model.Room;
import com.chat.app.repository.ChatMessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.chat.app.service.RoomService;

import java.util.Collections;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ChatController {

    private final RoomService roomService;
    private final ChatMessageRepository chatRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(RoomService roomService, ChatMessageRepository chatRepo, SimpMessagingTemplate messagingTemplate) {
        this.roomService = roomService;
        this.chatRepo = chatRepo;
        this.messagingTemplate = messagingTemplate;
    }

    private static final String GLOBAL_ROOM_ID = "GLOBAL";

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage message){
        if (message.getRoomId() == null || message.getRoomId().isBlank()) {
            message.setRoomId(GLOBAL_ROOM_ID);
        }
        ChatMessage saved = chatRepo.save(message);
        messagingTemplate.convertAndSend("/topic/messages/" + saved.getRoomId(), saved);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String username,
                       @RequestParam(required = false) String roomId,
                       Model model){
        model.addAttribute("username", username);
        model.addAttribute("roomId", roomId == null || roomId.isBlank() ? GLOBAL_ROOM_ID : roomId);
        return "chat";
    }
@GetMapping("/create")
public String create(){
    return "create";
}

@GetMapping("/join")
public String join(Model model){
    model.addAttribute("rooms", roomService.getAllRooms());
    return "join";
}

// @PostMapping("/create-room")
// public ResponseEntity<Room> createRoom(@RequestBody RoomRequest request) {
//     return ResponseEntity.ok(roomService.createRoom(request));
// }

@PostMapping("/create-room")
public String createRoomFromForm(@RequestParam String roomName,
                                 @RequestParam String username,
                                 @RequestParam(required = false, defaultValue = "false") boolean locked,
                                 @RequestParam(required = false) String password,
                                 Model model){ 
    RoomRequest request = new RoomRequest();
    request.setRoomName(roomName);
    request.setUsername(username);
    request.setLocked(locked);
    request.setPassword(password);

    Room room = roomService.createRoom(request);

    // send data to confirmation page
    model.addAttribute("roomId", room.getRoomId());
    model.addAttribute("roomName", room.getRoomName());
    model.addAttribute("username", username);
    return "room-created";
}

@PostMapping("/join-room")
public String joinRoom(@RequestParam String username,
                       @RequestParam String roomId,
                       @RequestParam(required = false) String password,
                       Model model){
    if (!roomService.roomExists(roomId)) {
        model.addAttribute("error", "Room not found");
        model.addAttribute("rooms", roomService.getAllRooms());
        return "join";
    }

    Room room = roomService.getRoom(roomId);
    if (room.isLocked() && !roomService.isPasswordValid(roomId, password)) {
        model.addAttribute("error", "Invalid room password");
        model.addAttribute("rooms", roomService.getAllRooms());
        return "join";
    }

    model.addAttribute("roomId", roomId);
    model.addAttribute("username", username);
    return "chat";
}

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

    @GetMapping("/messages")
    @ResponseBody
    public List<ChatMessage> getMessages(@RequestParam(required = false) String roomId){
        String effectiveRoomId = roomId == null || roomId.isBlank() ? GLOBAL_ROOM_ID : roomId;
        List<ChatMessage> messages = chatRepo.findTop200ByRoomIdOrderByIdDesc(effectiveRoomId);
        Collections.reverse(messages);
        return messages;
    }

}
