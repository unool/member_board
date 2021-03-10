package org.zerock.member_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.member_board.dto.AttendDTO;
import org.zerock.member_board.dto.MemberDTO;
import org.zerock.member_board.service.AttendService;


@RequiredArgsConstructor
@RequestMapping("/attend")
@Controller
public class AttendController {

    private final AttendService attendService;


    @ResponseBody
    @PostMapping ("/getAttend")
    public AttendDTO getAttend(Long bno){


        return   attendService.getAttend(bno);
    }

    @ResponseBody
    @PostMapping ("/attendMember")
    public ResponseEntity<AttendDTO> attendMember(Long bno, String attendEmail){

        AttendDTO attendDTO = attendService.attendMember(bno, attendEmail);

        return new ResponseEntity<>(attendDTO, HttpStatus.OK);
    }
}
