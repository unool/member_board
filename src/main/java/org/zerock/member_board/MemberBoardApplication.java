package org.zerock.member_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MemberBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberBoardApplication.class, args);
    }

}
