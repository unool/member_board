package org.zerock.member_board.dto;


import lombok.Getter;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.User;

import java.io.Serializable;

@Getter
public class SessionUser  {
    private String name, email, picture;
    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}