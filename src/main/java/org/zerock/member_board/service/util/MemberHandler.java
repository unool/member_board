package org.zerock.member_board.service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.zerock.member_board.entity.Member;

@Component
public class MemberHandler {

    public static String GetMemberEmail(){
        Object object = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String email = "";
        if(object.getClass().getName().equals(Member.class.getName()))
        {
            Member member = (Member) object;
            email = member.getEmail();
        }
        else if(object.getClass().getName().equals(DefaultOAuth2User.class.getName()))
        {
            DefaultOAuth2User user = (DefaultOAuth2User) object;
            email = user.getAttribute("email");
        }
        else
        {
            //예외처리
        }



        return email;
    }

}
