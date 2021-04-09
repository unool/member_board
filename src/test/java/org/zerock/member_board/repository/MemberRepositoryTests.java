package org.zerock.member_board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.role.Kind;
import org.zerock.member_board.entity.role.Role;

import java.util.stream.IntStream;

@Transactional
@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void insertMember(){

        BCryptPasswordEncoder d = new BCryptPasswordEncoder();

        IntStream.rangeClosed(1,20).forEach(i->{
            Member member = Member.builder()
                    .email("unool" + i + "@yahoo.co.kr")
                    .kind(Kind.NORMAL.getKey())
                    .name("영현"+i)
                    .auth(Role.USER.getKey())
                    .password(d.encode("1234"))
                    .phone("010-4775-2222")
                    .build();

            memberRepository.save(member);

        });

    }

}
