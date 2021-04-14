package org.zerock.member_board.repository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Reply;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno ")
    void deleteByBno(@Param("bno") Long bno);

    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
