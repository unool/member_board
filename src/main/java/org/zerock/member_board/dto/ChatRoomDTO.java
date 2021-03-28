package org.zerock.member_board.dto;



import lombok.*;


@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {

    private String appSubAdd;
    private String appSendAdd;
    private String roomID;
    private String myEmail;

}
