package org.zerock.member_board.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zerock.member_board.service.util.CustomSessionListener;
import javax.servlet.http.HttpSessionListener;


@Configuration
public class SpringConfig {

    @Bean
    public HttpSessionListener httpSessionListener(){ //세션 리스터

        HttpSessionListener httpSessionListener = new CustomSessionListener();
        return httpSessionListener;
    }

}
