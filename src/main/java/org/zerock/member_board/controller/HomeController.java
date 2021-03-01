package org.zerock.member_board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){
        System.out.println("index!!!!!");

        return "index";
    }

}
