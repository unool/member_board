package org.zerock.member_board.service.util;
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


        if(object.getClass().getName().equals(DefaultOAuth2User.class.getName()))
        {
            //Oauth2 로그인일 경우
            DefaultOAuth2User user = (DefaultOAuth2User) object;
            email = user.getAttribute("email");
        }
        else
        {
            //그냥 일반 Spring Security 로그인일 경우
            email = (String) object;
        }

        return email;
    }

}
