package com.chat.app.Controller;
import com.chat.app.model.ChatMessage;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ChatController {

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
@GetMapping("/")
public String home() {
    return "landing";   
}

@GetMapping("/ping")
public void ping(jakarta.servlet.http.HttpServletResponse response) throws Exception {
    response.getWriter().write("OK");
}

}
