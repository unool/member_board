package org.zerock.member_board.service.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CustomSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(60*60); //세션만료60분
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}