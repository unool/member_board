package org.zerock.member_board.controller;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.member_board.service.NaverMapService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
