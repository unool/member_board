package org.zerock.member_board.repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Reply;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


@Transactional
@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    ReplyRepository replyRepository;

    @Test
    public void insertReply(){

        IntStream.rangeClosed(1,300).forEach(i-> {
            long bno = (long)(Math.random() * 100) + 1;

            Board board = Board.builder()
                    .bno(bno)
                    .build();

            Reply reply = Reply.builder()
                    .text("Reply..." + i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);
        });
    }

    @Transactional
    @Test
    public void findTest(){

        Optional<Reply> result = replyRepository.findById(20L);
        System.out.println("=================");
        System.out.println(result);
        System.out.println("=================");

//        System.out.println("=================");
//        System.out.println(result.get());
//        System.out.println("=================");
    }

    @Transactional
    @Test
    public void listBoard(){

        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder()
        .bno(100L).build());

        replyList.forEach(reply -> {
            System.out.println(reply);
        });
    }
}
