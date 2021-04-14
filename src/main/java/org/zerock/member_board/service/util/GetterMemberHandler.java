package org.zerock.member_board.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.error.exception.ControllerException;
import org.zerock.member_board.repository.MemberRepository;

import java.util.Optional;

/**
 * 특정 멤버와 관련하여 특정 작업을 할때 존재하는 멤버인지 체크를위해 멤버를 Get 하는 클래스
 */


@Component
public class GetterMemberHandler {

    private static MemberRepository memberRepository;

    @Autowired
    public GetterMemberHandler(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;

    }

    public static Member getMember(String email)
    {
        Member member = null;

        Optional<Member> result = memberRepository.findById(email);

        if(result.isPresent())
        {
            member = result.get();
        }
        return member;
    }
}
