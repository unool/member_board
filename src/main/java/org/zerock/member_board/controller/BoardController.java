package org.zerock.member_board.controller;

import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member_board.dto.AttendDTO;
import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.OAuthAttributes;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.repository.AttendRepository;
import org.zerock.member_board.service.AttendService;
import org.zerock.member_board.service.BoardService;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RequestMapping("/board/")
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private  AttendService attendService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model)
    {

        model.addAttribute("result", boardService.getList(pageRequestDTO));

    }

    @GetMapping("/register")
    public void register(){

    }


    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes) throws ParseException {

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime parsedLocalDateTime = LocalDateTime.parse(dto.getLimitDate());

        System.out.println("<> ===========" + dto);
        Long bno = boardService.register(dto);

        redirectAttributes.addAttribute("bno", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read","/modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                     Long bno, Model model)
    {
        BoardDTO boardDTO = boardService.get(bno);
//        AttendDTO attendDTO = attendService.getAttend(bno);


        model.addAttribute("dto", boardDTO);
//        model.addAttribute("attend", attendDTO);
    }



    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes redirectAttributes)
    {

        boardService.removeWithReplyies(bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";

    }

    @PostMapping("modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO")
            PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes)
    {
        boardService.modify(dto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }

    @GetMapping("/confirm")
    public String confirm(Long bno, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                          RedirectAttributes redirectAttributes)
    {
        boardService.confirm(bno);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", bno);
        return "redirect:/board/read";
    }

    @GetMapping("/test")
    public void test(){
        System.out.println("test");
    }
}
