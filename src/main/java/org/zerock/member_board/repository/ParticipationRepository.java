package org.zerock.member_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.member_board.entity.Participation;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation,Long> {

    @Query("select p, b, m from Participation p left outer join p.bno b left outer join p.member m" +
            " where p.member.email = :writerEmail")
    List<Object[]> getBoardByEmail(@Param("writerEmail") String writerEmail);
}
