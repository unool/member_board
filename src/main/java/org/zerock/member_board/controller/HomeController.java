package org.zerock.member_board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){


        return "index";
    }

    @GetMapping("/test")
    public String test(String a, String b){


        return "index";
    }


}
