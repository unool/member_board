package org.zerock.member_board.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import org.zerock.member_board.websocket.dto.ChatMessage;
import org.zerock.member_board.websocket.dto.MSG_Type;
import org.zerock.member_board.websocket.service.WebSocketService;

import java.util.Map;

@Controller
public class GreetingController {

    @Autowired
    WebSocketService webSocketService;


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