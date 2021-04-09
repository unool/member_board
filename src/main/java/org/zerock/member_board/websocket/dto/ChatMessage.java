package org.zerock.member_board.websocket.dto;
import lombok.*;
import java.util.HashMap;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessage {

    private HashMap<String,Object> data = new HashMap<>();

    private MSG_Type msg_type;

    public void addData(String key, Object value){
        data.put(key, value);
    }
}