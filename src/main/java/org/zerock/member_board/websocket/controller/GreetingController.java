package org.zerock.member_board.websocket.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.zerock.member_board.service.util.MemberHandler;
import org.zerock.member_board.websocket.dto.ChatMessage;
import org.zerock.member_board.websocket.service.WebSocketService;

@RequiredArgsConstructor
@Controller
public class GreetingController {

    private final WebSocketService webSocketService;

    @MessageMapping("/broad")
    public void greeting(ChatMessage message, @Header("simpSessionId") String sessionId) {


       webSocketService.receiveChatMessage(sessionId,message);
    }

    @MessageMapping("/room/{room_id}")
    public void receiveRoomMessage(@DestinationVariable("room_id") String room_id,
                                   ChatMessage message, @Header("simpSessionId") String sessionId) {

        if(message == null || !StringUtils.hasText(room_id))
        {
            //채팅중에 부모창을 리프레시 하거나 닫으면 부모소켓 끊어질때 방 폐기하므로 해당 구문 진입
            //더 상세한 예외처리 필요
            return;
        }

        webSocketService.disposRoomMessage(room_id, message);
    }
}