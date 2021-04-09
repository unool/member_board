package org.zerock.member_board.controller;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.member_board.service.NaverMapService;
import java.io.IOException;

/**
 * oauth2를 안 쓰고 구현한 네이버 로그인
 * 현재는 쓰이지 않음
 */

@RequiredArgsConstructor
@RequestMapping("/naver")
@RestController
public class NaverMapController {

    private final NaverMapService naverMapService;

    @GetMapping("/reqmap")
    public String reqMap() throws IOException, ParseException {
        return naverMapService.reqMapPostion("덕양구 화신로 105");
    }
}
