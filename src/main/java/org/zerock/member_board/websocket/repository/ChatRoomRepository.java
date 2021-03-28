package org.zerock.member_board.websocket.repository;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.zerock.member_board.websocket.dto.ChatRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Getter
@Setter
public class ChatRoomRepository {

    private HashMap<String , ChatRoom> roomList = new HashMap<>();

    public ChatRoom getRoom(String roomID){

        return roomList.get(roomID);
    }

    public void deleteRoom(String roomID){
        roomList.remove(roomID);
    }

    public void addRoom(String id, ChatRoom room)
    {
        roomList.put(id, room);
    }


}
