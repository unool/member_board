package org.zerock.member_board.websocket.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.zerock.member_board.service.util.LogManager;
import org.zerock.member_board.websocket.entity.WSSession;
import org.zerock.member_board.websocket.service.WebSocketService;
import java.util.List;
import java.util.Map;


@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private WebSocketService webSocketService;

    @EventListener //이벤트에 대한 리스너로 등록
    public void handleWebSocketConnectListener(SessionConnectedEvent event) { //SessionConnectedEvent라는 클래스가 존재해야한다
        LogManager.log("소켓 접속");
        MessageHeaderAccessor accessor = NativeMessageHeaderAccessor.getAccessor(event.getMessage(), SimpMessageHeaderAccessor.class);
        GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader("simpConnectMessage");
        Map<String, Object> nativeHeaders = (Map<String, Object>) generic.getHeaders().get("nativeHeaders");
        String userEmail = ((List<String>) nativeHeaders.get("userEmail")).get(0);
        String sessionId = (String) generic.getHeaders().get("simpSessionId");


        WSSession wsSession = WSSession.builder()
                .simpSessionId(sessionId)
                .email(userEmail)
                .build();
        webSocketService.connectedWSSession(wsSession);
//        ChatMessage chatMessage = ChatMessage.builder()
//                .msg_type(MSG_Type.CONNECT)
//                .build();
//        webSocketService.broadcast(chatMessage);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        LogManager.log("소켓 끊임");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        webSocketService.closedWSSession(sessionId);
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event)  {
        LogManager.log("구독 완료");

//        구독에 대한 broadcast가 패킷 수신 이후로 바뀜
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String sessionId = headerAccessor.getSessionId();
//
//        String subAdd = headerAccessor.getHeader("simpDestination").toString();
    }
}