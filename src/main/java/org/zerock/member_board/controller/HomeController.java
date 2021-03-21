package org.zerock.member_board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.member_board.service.BoardService;
import org.zerock.member_board.service.ReviewService;

import javax.servlet.http.HttpSession;


@Controller
public class HomeController {

    @Autowired
    BoardService boardService;

    @Autowired
    ReviewService reviewService;

    int rctBoardCnt = 3;

    @GetMapping("/")
    public String index(Model model){

        model.addAttribute("boardDTOList",
                boardService.getRecentBoard());

        model.addAttribute("reviewDTOList",
                reviewService.getRecentReview());


        return "index";
    }

    @GetMapping("/test")
    public String test(String a, String b){


        return "index";
    }


}
