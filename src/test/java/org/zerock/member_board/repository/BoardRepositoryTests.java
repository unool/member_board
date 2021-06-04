package org.zerock.member_board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void insertBoard(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("user"+i+"@naver.com")
                    .build();

            memberRepository.save(member);

            Board board = Board.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .writer(member)
                    .build();

            boardRepository.save(board);
        });


    }


    @Transactional
    @Test
    public void testGet(){
//        Board board = (Board)boardRepository.getBoardWithWriter(10L);
//        System.out.println(board.getWriter());
//

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row->{
            Object[] arr = (Object[]) row;

            System.out.println(Arrays.toString(arr));
        });

    }

    @Transactional
    @Test
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }
}
