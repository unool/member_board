package org.zerock.member_board.service.util;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 *
 * 인증 실패시 관련 핸들러 (인증 과정 중 Exception 된것을 catch하여 작동)
 * 발생된 Exception에 따라 분기하여 login 폼으로 이동
 *
 */

@Service
/* 로그인 실패 대응 로직 */
public class UserLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {


        if (exception instanceof AuthenticationServiceException) {
            request.setAttribute("loginFailMsg", "존재하지 않는 사용자입니다.");

        } else if(exception instanceof BadCredentialsException) {
            request.setAttribute("loginFailMsg", "아이디 또는 비밀번호가 틀립니다.");

        } else if(exception instanceof LockedException) {
            request.setAttribute("loginFailMsg", "잠긴 계정입니다..");

        } else if(exception instanceof DisabledException) {
            request.setAttribute("loginFailMsg", "비활성화된 계정입니다..");

        } else if(exception instanceof AccountExpiredException) {
            request.setAttribute("loginFailMsg", "만료된 계정입니다..");

        } else if(exception instanceof CredentialsExpiredException) {
            request.setAttribute("loginFailMsg", "비밀번호가 만료되었습니다.");
        }


        // 로그인 페이지로 다시 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("/member/login"); //직접 /member/login을 거치지 않음
        dispatcher.forward(request, response);

    }
}