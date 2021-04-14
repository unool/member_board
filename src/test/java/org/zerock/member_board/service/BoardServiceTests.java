package org.zerock.member_board.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.dto.PageResultDTO;

@Transactional
@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void test1(){

        BoardDTO boardDTO = BoardDTO.builder()
                .title("테스트")
                .content("컨텐트")
                .writerEmail("user20@naver.com")
                .build();

//        boardService.register(boardDTO);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO> result = boardService.getList(pageRequestDTO);

        for(BoardDTO dto : result.getDtoList())
        {
            System.out.println(dto);
        }
    }

    @Test
    public void delete()
    {
        boardService.removeWithReplyiesAndReviewAll(10L);
    }

    @Test
    public void modify()
    {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .content("11")
                .build();


        boardService.modify(boardDTO);
    }


}
