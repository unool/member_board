package org.zerock.member_board.service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.member_board.dto.MemberDTO;
import org.zerock.member_board.dto.SessionUser;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.error.exception.ControllerException;
import org.zerock.member_board.repository.MemberRepository;

import javax.lang.model.type.ErrorType;
import javax.servlet.http.HttpSession;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements UserDetailsService,MemberService {

    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> result = memberRepository.findById(email);

        Member member;

        if(result.isPresent())
        {
            member = result.get();
//            httpSession.setAttribute("user",new SessionUser(member));
        }
        else
        {
            throw new AuthenticationServiceException(email);
//            throw new ControllerException("not.exist.username", this.getClass().getName());
        }
        return member;
    }

    @Override
    public String registerMember(MemberDTO dto) {

        Optional<Member> result = memberRepository.findById(dto.getEmail());

        if(result.isPresent())
        {
            //이미 아이디가 존재함 (예외발생)
            throw new ControllerException("exist email", this.getClass().getName());
        }

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

    @Override
    public MemberDTO checkRegisterID(String email) {

        Optional<Member> result = memberRepository.findById(email);

        MemberDTO memberDTO = null;
        if(result.isPresent())
        {
           Member member = result.get();
           memberDTO = MemberDTO.builder()
                   .email(member.getEmail())
                   .kind(member.getKind())
                   .build();

           return memberDTO;
        }

        memberDTO = MemberDTO.builder()
                .build();

        return memberDTO;
    }
}
