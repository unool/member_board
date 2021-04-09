package org.zerock.member_board.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.member_board.dto.AttendDTO;
import org.zerock.member_board.service.AttendService;
import org.zerock.member_board.service.util.MemberHandler;
import java.util.HashMap;


@RequiredArgsConstructor
@RequestMapping("/attend")
@Controller
public class AttendController {

    private final AttendService attendService;

    @ResponseBody
    @PostMapping ("/getAttend")
    public HashMap<String, Object> getAttend(Long bno){

        AttendDTO attendDTO = attendService.getAttend(bno);
        HashMap<String, Object> hash = new HashMap<>();
        String curEmail = MemberHandler.GetMemberEmail();
        hash.put("AttendDTO", attendDTO);
        hash.put("ConnectedEmail", curEmail);
        return hash;
    }

    @ResponseBody
    @PostMapping ("/attendMember")
    public ResponseEntity<HashMap<String, Object>> attendMember(Long bno, String attendEmail){

        AttendDTO attendDTO = attendService.attendMember(bno, attendEmail);
        HashMap<String, Object> hash = new HashMap<>();
        String curEmail = MemberHandler.GetMemberEmail();
        hash.put("AttendDTO", attendDTO);
        hash.put("ConnectedEmail", curEmail);
        return new ResponseEntity<>(hash, HttpStatus.OK);
    }
}
