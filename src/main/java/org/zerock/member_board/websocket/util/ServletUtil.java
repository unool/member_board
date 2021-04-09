package org.zerock.member_board.websocket.util;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class ServletUtil {
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();

        if (ra == null) {
            return null;
        }

        return ((ServletRequestAttributes) ra).getRequest();
    }
    public static HttpSession getSession() {
        HttpServletRequest req = getHttpServletRequest();
        if (req != null) {
            return req.getSession();
        }

        return null;
    }
}
