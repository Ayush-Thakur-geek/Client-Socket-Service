package com.uber.ClientSocketService.controllers;

import com.uber.ClientSocketService.dtos.ChatRequestDTO;
import com.uber.ClientSocketService.dtos.ChatResponseDTO;
import com.uber.ClientSocketService.dtos.TestRequestDTO;
import com.uber.ClientSocketService.dtos.TestResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    @Autowired
    private SimpMessagingTemplate  template;

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponseDTO pingCheck(TestRequestDTO message) {
        System.out.println("Received message from client: " + message.getData());
        return TestResponseDTO.builder().data("Received").build();
    }

    @MessageMapping("/chat/{room}")
    @SendTo("/topic/message/{room}")
    public ChatResponseDTO chatMessage(@DestinationVariable String room, ChatRequestDTO request) {
        System.out.println("Received message from client: " + request.getMessage());
        return ChatResponseDTO.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp("" + System.currentTimeMillis())
                .build();
    }

//    @SendTo()
//    @Scheduled(fixedDelay = 2000)
//    public void sendPeriodicMessage() {
//        System.out.println("scheduler");
//        template.convertAndSend("/topic/schedule", "Scheduled response");
//    }

    @MessageMapping("/privateChat/{room}/{userId}")
    public void privateChatMessage(@DestinationVariable String room, @DestinationVariable String userId, ChatRequestDTO request) {
        ChatResponseDTO response = ChatResponseDTO.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp("" + System.currentTimeMillis())
                .build();

        template.convertAndSendToUser(userId, "/queue/privateMessage/" + room, response);
    }

}
