package org.zerock.member_board.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.member_board.dto.MemberDTO;
import org.zerock.member_board.dto.SessionUser;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements UserDetailsService,MemberService {
    private final HttpSession httpSession;
    Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> result = memberRepository.findById(email);

        Member member;

        if(result.isPresent())
        {
            member = result.get();

            httpSession.setAttribute("user",new SessionUser(member));


        }
        else
        {
            member = Member.builder().build();
            //예외처리
        }

        return member;
    }

    @Override
    public String registerMember(MemberDTO dto) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Member member = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .password(encoder.encode(dto.getPassword()))
                .auth(dto.getAuth())
                .kind(dto.getKind())
                .build();
        return memberRepository.save(member).getEmail();
    }

    @Override
    public void modifyMember(MemberDTO dto) {
        Member member = memberRepository.getOne(dto.getEmail());
        member.setName(dto.getName());
        member.setPhone(dto.getPhone());
        member.setPassword(dto.getPassword());
        memberRepository.save(member);
    }

    @Override
    public void deleteMember(String email) {
        memberRepository.deleteById(email);
    }


}
