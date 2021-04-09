package org.zerock.member_board.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.zerock.member_board.service.BoardService;
import org.zerock.member_board.service.ReviewService;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final BoardService boardService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("boardDTOList",
                boardService.getRecentBoard());

        model.addAttribute("reviewDTOList",
                reviewService.getRecentReview());
        return "/index";
    }
}
