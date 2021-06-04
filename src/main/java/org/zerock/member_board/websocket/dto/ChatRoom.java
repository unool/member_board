package org.zerock.member_board.websocket.dto;
import lombok.Getter;
import org.zerock.member_board.service.util.LogManager;
import org.zerock.member_board.websocket.entity.WSSession;
import org.zerock.member_board.websocket.service.WebSocketService;
import org.zerock.member_board.websocket.util.AddressUtil;
import java.util.*;


@Getter
public class ChatRoom {

    private String roomID = "";
    HashMap<String,WSSession> members = new HashMap<>();
    private Timer timer = new Timer();
    private WebSocketService webSocketService;
    boolean timerCheck = false; //현재 타이머 구동 여부

    public ChatRoom(WebSocketService webSocketService){

        this.webSocketService = webSocketService;
        roomID = UUID.randomUUID().toString();
    }


    public void joinRoom(WSSession wsSession){

        wsSession.setRoomID(roomID);
        members.put(wsSession.getSimpSessionId(), wsSession);

        //팡 폐기 되고 있거나, 폐기 됬는데 들어 올 경우 예외처리 필요

        if(members.size()  < 2)
        {
            waitRoom(); //방 타이머 시작, 시간 초과시 방 폐기
        }
        else{
            stopTimer();
        }
    }

    public void waitRoom(){
        timerCheck = true;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                clear();
            }
        };
        timer.schedule(timerTask, 15000); //20초간 대기
    }

    public void clear(){

        if(timerCheck) //타이머가 돌고있는 상황이라면
        {
            stopTimer(); //시작 안한 상태에서, 혹은 종료된 상태에서 cancel시 예외처리

            //채팅 취소됨을 메세지 보냄
            HashMap<String,Object> data = new HashMap<>();
            data.put("room_id",roomID);
            data.put("open","false");
            data.put("room_member", webSocketService.getRoomMembersEmail(this));
            ChatMessage chatMessage = ChatMessage.builder()
                    .msg_type(MSG_Type.ROOM_OPEN)
                    .data(data)
                    .build();
            webSocketService.broadcast(chatMessage);
        }


        for(WSSession wsSession : members.values())
        {
            wsSession.setRoomID("");
            wsSession.setChildSessionId("");
        }
        members.clear();
        webSocketService.destroyRoom(this);
    }

    public void roomBroadcastMembers(ChatMessage chatMessage)
    {
        String destination = getDestination(roomID);

        LogManager.log("desti : " + destination+ " , " + chatMessage);
        webSocketService.roomSend(destination, chatMessage);
    }

    private String getDestination(String chatRoomId) {
        return AddressUtil.getRoomSendAdd() + chatRoomId;
    }

    private void stopTimer()
    {
        timerCheck = false;
        timer.cancel();
    }
}
