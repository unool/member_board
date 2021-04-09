package org.zerock.member_board.dto;
import lombok.Getter;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.User;
import java.io.Serializable;

/**
 *
 * 기타
 * Serializable implements하여 클래스 직렬화 시켜 메모리 위에 영속시킴.
 */

@Getter
public class SessionUser implements Serializable {
    private String name, email, picture;
//    public SessionUser(User user) {
//        this.name = user.getName();
//        this.email = user.getEmail();
//        this.picture = user.getPicture();
//    }

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}