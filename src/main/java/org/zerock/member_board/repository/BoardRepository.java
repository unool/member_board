package org.zerock.member_board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.member_board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b,w from Board b left join b.writer w where b.bno = :bno")
    Object getBoardWithWriter(@Param("bno") Long bno);



    @Query(value = "select b, m, count(r) from Board b " +
            "left join b.writer m left join Reply r on b = r.board group by b"
    , countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("select b, w, count(r) " +
            "from Board b left join b.writer w " +
            "left outer join Reply r on b = r.board " +
            "where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
