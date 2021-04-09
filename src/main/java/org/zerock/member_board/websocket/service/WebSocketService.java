package org.zerock.member_board.websocket.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.zerock.member_board.service.util.LogManager;
import org.zerock.member_board.websocket.dto.ChatMessage;
import org.zerock.member_board.websocket.dto.ChatRoom;
import org.zerock.member_board.websocket.dto.MSG_Type;
import org.zerock.member_board.websocket.entity.WSSession;
import org.zerock.member_board.websocket.repository.ChatRoomRepository;
import org.zerock.member_board.websocket.util.AddressUtil;
import java.util.*;


@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private Map<String, WSSession> connectedList = new HashMap<>();

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void connectedWSSession(WSSession session)
    {
        //동일 이메일로 웹소켓이 들어왔을 경우, 자식 소켓이 들어온 것으로 인식 (해당 로직대로 구동하려면 중복로그인을 방지해야한다)
        WSSession wsSession = checkLoginUser(session.getEmail());
        System.out.println("===== 웹소켓 접속이다");
        if(wsSession == null)
        {
            connectedList.put(session.getSimpSessionId(),session);
        }
        else
        {
            System.out.println("===== 자식 웹소켓 접속이다");
            wsSession.setChildSessionId(session.getSimpSessionId());
        }


//        HashMap<String, String> data = new HashMap<>();
//        data.put("connectedEmail", session.getEmail()); //비로그인시 빈값 들어감
//
//
//        ChatMessage chatMessage = ChatMessage.builder()
//                .msg_type(MSG_Type.CONNECT)
//                .data(data)
//                .build();
//
//        broadcast(chatMessage);
    }

    private WSSession checkLoginUser(String email)
    {
        for(WSSession ws : connectedList.values())
        {
            if(ws.getEmail().equals(email))
                return ws;
        }

        return null;
    }

    public void closedWSSession(String simpSessionId)
    {
        if(connectedList.containsKey(simpSessionId)) //포함 된 경우 부모소켓, 자식 소켓만 열려있는 경우는 그냥 채팅 가능하도록 정책
        {
            WSSession wsSession = connectedList.get(simpSessionId);
            if(wsSession.getRoomID() != null) //채팅 요청만 해놓고 성립 전 나간 경우 방 삭제
            {
                if(wsSession.getRoomID() != "")
                {
                    chatRoomRepository.deleteRoom(wsSession.getRoomID());
                }
            }
            connectedList.remove(simpSessionId);

        }
        else //미포함 경우 자식소켓
        {
            WSSession parentWS = findParentWS(simpSessionId);

            if(parentWS == null) //채팅방에 한 유저가 나간 순간 방 폭파되고 각 유저들의 상태도 초기화되므로 두번째 유저는 부모소켓 없음
            {
                return;
            }
            String roomID = parentWS.getRoomID();
            if(roomID != "") //방 참가 유저였다면, (웹소켓이 Disconnect 됬는데 접속 리스트에 없다면 100%)
            {

                HashMap<String,Object> data = new HashMap<>();
                data.put("out_member", parentWS.getSimpSessionId());

                ChatMessage chatMessage = ChatMessage.builder()
                        .msg_type(MSG_Type.USER_OUT)
                        .data(data)
                        .build();

                ChatRoom chatRoom = chatRoomRepository.getRoom(roomID);
                chatRoom.broadcastMembers(chatMessage);

                chatRoom.clear();
            }
        }
    }


    private WSSession findParentWS(String childWSID) {

        for(WSSession wsSession : connectedList.values())
        {
            if(wsSession.getChildSessionId() == childWSID)
            {
                return wsSession;
            }
        }

        return null;
    }

    public void subAppWSSession() {

       HashMap<String, Object> data = new HashMap<>();

       List<String> loginList = new ArrayList<>();
       for(WSSession session : connectedList.values())
       {

           loginList.add(session.getEmail());
       }

       data.put("login_list", loginList);

       ChatMessage chatMessage = ChatMessage.builder()
               .msg_type(MSG_Type.SUB_RES)
               .data(data)
               .build();
//        Thread.sleep(2000);
       broadcast(chatMessage);
    }

    public ChatMessage receiveChatMessage(String sessionID, ChatMessage message)
    {
        LogManager.log("receiveChatMessage : " + message);
        ChatMessage chatMessage = null;

        switch (message.getMsg_type())
        {
            case CHAT_REQ : //대화요청
                chatMessage = chat_req(sessionID, message);
                break;
            case JOIN_REQ:
                chatMessage = join_req(sessionID, message);
                break;
            case SUB_REQ:
                subAppWSSession();
                break;

        }


        return chatMessage;
    }

    private ChatMessage join_req(String sessionID, ChatMessage msg){
        System.out.println(msg);
        String join = msg.getData().get("join").toString();

        ChatRoom chatRoom = chatRoomRepository.getRoom(msg.getData().get("room_id").toString());
        ChatMessage returnMsg = null;

        HashMap<String, Object> data = new HashMap<>();
        data.put("room_id", chatRoom.getRoomID());


        if(chatRoom == null) //수락 하기전에 방이 사라진경우, 요청하고 신청 유저가 나간 경우
        {
            data.put("open", "false");
        }

        if(join.equals("false")) //불참
        {
            data.put("open", "false");
            chatRoom.clear(); //방이 이미 만들어졌으므로
        }
        else //참가
        {
            WSSession wsSession = connectedList.get(sessionID);
            chatRoom.joinRoom(wsSession); //방 참가
            data.put("open", "true");
        }

        data.put("room_member", getRoomMembersEmail(chatRoom));
        ChatMessage chatMessage = ChatMessage.builder()
                .msg_type(MSG_Type.ROOM_OPEN)
                .data(data)
                .build();

        returnMsg = chatMessage;

        return returnMsg;
    }

    public List<String> getRoomMembersEmail(ChatRoom chatRoom){

        List<String> emailList = new ArrayList<>();

        for(WSSession wsSession : chatRoom.getMembers().values())
        {
            emailList.add(wsSession.getEmail());
        }

        return emailList;
    }

    private ChatMessage chat_req(String sessionID, ChatMessage msg){

        if(!checkPossibleChat(msg.getData().get("req_member").toString(),
                msg.getData().get("chat_member").toString()))
        {
            HashMap<String, Object> data = new HashMap<>();
            data.put("req_result", "false");
            data.put("req_member", msg.getData().get("req_member").toString());

            ChatMessage chatMessage = ChatMessage.builder()
                    .msg_type(MSG_Type.CHAT_RES)
                    .data(data)
                    .build();
            return chatMessage;
        }

        WSSession wsSession = connectedList.get(sessionID);

        ChatRoom chatRoom = createRoomAndJoin(wsSession);

        msg.addData("req_result","true");
        msg.addData("room_id", chatRoom.getRoomID());

        ChatMessage chatMessage = ChatMessage.builder()
                .msg_type(MSG_Type.CHAT_RES)
                .data(msg.getData())
                .build();


        return chatMessage;
    }

    private ChatRoom createRoomAndJoin(WSSession wsSession)
    {
        ChatRoom chatRoom = createRoom();
        chatRoom.joinRoom(wsSession);
        return chatRoom;
    }

    private boolean checkPossibleChat(String req_member, String chat_member) {

        for(WSSession wsSession : connectedList.values())
        {
            String email = wsSession.getEmail();


            if(email.equals(req_member) || email.equals(chat_member))
            {
                if(wsSession.getRoomID() == null)
                    continue;

                if(!wsSession.getRoomID().equals(""))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private ChatRoom createRoom()
    {
        ChatRoom chatRoom = new ChatRoom(this);

        LogManager.log(String.valueOf(this.hashCode()));
        chatRoomRepository.addRoom(chatRoom.getRoomID(),chatRoom); //room 생성
        return chatRoom;
    }


    public void destroyRoom(ChatRoom chatRoom)
    {
        chatRoomRepository.deleteRoom(chatRoom.getRoomID()); //리스트에서 정리
    }

    public void roomSend(String destination, ChatMessage chatMessage){

        messagingTemplate.convertAndSend(destination, chatMessage);
    }

    public void disposRoomMessage(String room_id, ChatMessage chatMessage) //룸에 있는 유저의 패킷
    {
        LogManager.log("disposRoomMessage : " + chatMessage);

        if(chatRoomRepository.getRoom(room_id) == null) //존재하지 않는 방일 겨우 처리하지 않음. (대화 중 한 유저 나가면 방 폐기됨.)
        {
            return;
        }

        switch (chatMessage.getMsg_type())
        {
            case ROOM_CHAT:
                room_chat(room_id, chatMessage); //들어 온 내용을 그대로 전달
                break;
        }
    }

    private void room_chat(String room_id, ChatMessage chatMessage)
    {
        String destination = getDestination(room_id);


        System.out.println("룸 채팅 목적지 : " + destination);
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

    private String getDestination(String room_id)
    {
        return AddressUtil.getRoomSendAdd() + room_id;
    }


//    public void open_res(ChatMessage chatMessage){
//
//        ChatRoom chatRoom = chatRoomRepository.getRoom(chatMessage.getData().get("room_id").toString());
//
//        if(chatRoom == null)
//            return;
//
//        int index = chatRoom.getMembers().indexOf(chatMessage.getData().get("chat_member"));
//
//
//    }


    public void broadcast(ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/cli/broad", chatMessage);
    }


}
