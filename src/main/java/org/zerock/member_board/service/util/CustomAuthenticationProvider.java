package org.zerock.member_board.service.util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.zerock.member_board.dto.SessionUser;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.error.exception.ControllerException;
import org.zerock.member_board.service.MemberServiceImpl;

import javax.servlet.http.HttpSession;

/**
 * 인증 과정 중 실제로 password를 검증하는 클래스
 */

@Service
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MemberServiceImpl memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final HttpSession httpSession;

    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        Member member = (Member) memberService.loadUserByUsername(username);


        if(!matchPassword(password, member.getPassword())) {
            throw new BadCredentialsException(username);
        }

        if(!member.isEnabled()) {
            throw new BadCredentialsException(username);
        }

        httpSession.setAttribute("user",new SessionUser(member));

        return new UsernamePasswordAuthenticationToken(username, password, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return true;
    }

    private boolean matchPassword(String loginPwd, String password) {
        Boolean match = bCryptPasswordEncoder.matches(loginPwd, password);
        return match;
    }

}


