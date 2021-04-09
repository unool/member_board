package org.zerock.member_board.websocket.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.zerock.member_board.websocket.dto.ChatMessage;
import org.zerock.member_board.websocket.service.WebSocketService;

@RequiredArgsConstructor
@Controller
public class GreetingController {

    private final WebSocketService webSocketService;

    @MessageMapping("/broad")
    @SendTo("/cli/broad")
    public ChatMessage greeting(ChatMessage message, @Header("simpSessionId") String sessionId) throws Exception {

        return webSocketService.receiveChatMessage(sessionId,message);
    }

    @MessageMapping("/room/{room_id}")
    public void receiveRoomMessage(@DestinationVariable("room_id") String room_id,
                                   ChatMessage message, @Header("simpSessionId") String sessionId) {

        System.out.println("receive room : " +message);
        if(message == null || !StringUtils.hasText(room_id))
        {
            return;
        }

        webSocketService.disposRoomMessage(room_id, message);
    }
}