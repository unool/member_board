package org.zerock.member_board.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.member_board.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {


}
