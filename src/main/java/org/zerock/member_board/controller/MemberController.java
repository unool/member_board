package org.zerock.member_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.member_board.dto.MemberDTO;
import org.zerock.member_board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public void login(String useremail, String userpass, Model model){

        System.out.println("loginGet");
        System.out.println("========================= "+useremail);
        System.out.println("========================= "+userpass);

        model.addAttribute("useremail",useremail);
        model.addAttribute("userepass",userpass);

    }
    @PostMapping ("/login")
    public void loginPost(){

        System.out.println("loginPost");
    }

    @GetMapping("/signup")
    public void signup(){

    }
    @PostMapping("/register")
    public String register(MemberDTO dto){
        memberService.registerMember(dto);

        return "redirect:/";
    }

    @GetMapping( "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }



    @ResponseBody
    @PostMapping("/checkemail/{email}")
    public MemberDTO checkemail(@PathVariable String email)
    {
        MemberDTO memberDTO = memberService.checkRegisterID(email);

        return memberDTO;
    }

}
