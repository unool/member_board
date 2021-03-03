package org.zerock.member_board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.ReplyDTO;
import org.zerock.member_board.service.ReplyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/replies/")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno)
    {
        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){

        System.out.println(replyDTO);

        Long rno = replyService.register(replyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<Long> delete(@PathVariable Long rno){

        replyService.remove(rno);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<Long> modify(@RequestBody ReplyDTO replyDTO){

        replyService.modify(replyDTO);

        return new ResponseEntity<>(replyDTO.getRno(), HttpStatus.OK);

    }


}
