package org.zerock.member_board.service;
import org.zerock.member_board.dto.MemberDTO;
import org.zerock.member_board.entity.Member;


public interface MemberService {

    String registerMember(MemberDTO dto);
    void modifyMember(MemberDTO dto);
    void deleteMember(String email);

    default Member dtoToEntity(MemberDTO dto){

        Member member = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .build();

        return member;
    }

    MemberDTO checkRegisterID(String email);
}
