package org.zerock.member_board.websocket.entity;

import lombok.*;


@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSSession {

    String simpSessionId= "";
    String childSessionId= "";
    String email= "";
    String roomID= "";

    public void outRoom(){
        this.roomID = "";
        this.childSessionId = "";
    }
}
