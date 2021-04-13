package org.zerock.member_board.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthExceptionController {

    @GetMapping("/exception/authentication")
    public String exceptionAuth(){
        return "redirect:/member/login";
    }
}
