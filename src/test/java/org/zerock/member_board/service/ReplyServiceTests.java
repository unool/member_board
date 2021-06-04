package org.zerock.member_board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.dto.ReplyDTO;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Reply;

import java.util.List;
@Transactional
@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    ReplyService replyService;


    @Test
    public void getList(){


        List<ReplyDTO> list = replyService.getList(100L);

        list.forEach(replyDTO -> System.out.println(replyDTO));
    }
}
