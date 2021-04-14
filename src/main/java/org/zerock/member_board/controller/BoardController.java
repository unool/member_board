package org.zerock.member_board.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member_board.dto.*;
import org.zerock.member_board.service.BoardService;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Map;


@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(@Param("requestDTO")PageRequestDTO pageRequestDTO, Model model) throws InterruptedException {

        model.addAttribute("result", boardService.getList(pageRequestDTO));
        return "/board/list";
    }

    @GetMapping("/register")
    public String register(BoardDTO boardDTO, Model model){

        model.addAttribute("boardDTO" , boardDTO);
        return "/board/register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO dto,
                               Errors errors, RedirectAttributes redirectAttributes,
                               Model model) throws ParseException {

        if (errors.hasErrors()) {
            // 회원가입 실패시, 입력 데이터를 유지
            model.addAttribute("boardDTO", dto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = boardService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/board/register";
        }

        Long bno = boardService.register(dto);
        redirectAttributes.addAttribute("bno", bno);
        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                     Long bno, Model model)
    {
        System.out.println(pageRequestDTO);
        BoardDTO boardDTO = boardService.get(bno);
        model.addAttribute("dto", boardDTO);
        return "/board/read";
    }

    @GetMapping("/modify")
    public String modify(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                     Long bno, Model model)
    {
        BoardDTO boardDTO = boardService.get(bno);
        model.addAttribute("dto", boardDTO);
        return "/board/modify";
    }



    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes redirectAttributes)
    {
        boardService.removeWithReplyiesAndReviewAll(bno); //모임게시물과 관련된 모든것을 삭제
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list";
    }

    @PostMapping("/modify")
    public String modify(@Valid BoardDTO dto, Errors errors, @ModelAttribute("requestDTO")
            PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes, Model model)
    {
        if (errors.hasErrors()) {

            //기존 데이터 그대로
            model.addAttribute("dto", dto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = boardService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/board/modify";
        }

        boardService.modify(dto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        redirectAttributes.addAttribute("type[0]", pageRequestDTO.getType(0));
        redirectAttributes.addAttribute("type[1]", pageRequestDTO.getType(1));
        redirectAttributes.addAttribute("type[2]", pageRequestDTO.getType(2));
        redirectAttributes.addAttribute("typeKeyword", pageRequestDTO.getTypeKeyword());
        redirectAttributes.addAttribute("region", pageRequestDTO.isRegion());
        redirectAttributes.addAttribute("regionKeyword", pageRequestDTO.getRegionKeyword());
        redirectAttributes.addAttribute("minCost", pageRequestDTO.getMinCost());
        redirectAttributes.addAttribute("maxCost", pageRequestDTO.getMaxCost());
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
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getTypeKeyword());
        redirectAttributes.addAttribute("bno", bno);
        return "redirect:/board/read";
    }


}
