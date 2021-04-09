package org.zerock.member_board.dto;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private String email;

    private String password;

    private String name;

    private String phone;

    private String auth;

    private String kind;
}
