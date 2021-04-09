package org.zerock.member_board.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member_board.dto.*;
import org.zerock.member_board.service.ReviewService;
import java.util.List;


@RequestMapping("/review")
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviewList")
    public String list(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model, String reason){

        model.addAttribute("result",   reviewService.getList(pageRequestDTO));
        model.addAttribute("reason", reason);
        return "/review/reviewList";
    }

    @GetMapping("/reviewRead")
    public String read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long rro, Model model)
    {
        model.addAttribute("dto",reviewService.get(rro));
        return "/review/reviewRead";
    }

    @GetMapping("/reviewModify")
    public String modify(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long rro, Model model)
    {
        model.addAttribute("dto",reviewService.get(rro));
        return "/review/reviewModify";
    }

    @GetMapping("/reviewRegister")
    public String register(String email, Model model, RedirectAttributes redirectAttributes)
    {
        List<ParticipationDTO> result = reviewService.getList(email);

        if(result == null)
        {
            redirectAttributes.addAttribute("reason","redirect");
            return "redirect:/review/reviewList";
        }
        else
        {
            model.addAttribute("dtoList", result); //작성 가능한 보드를 가져오기위해
            return "/review/reviewRegister";
        }
    }

    @PostMapping("/reviewRegister")
    public String register(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                           ReviewDTO reviewDTO, MultipartFile[] photos)
    {
        reviewService.register(reviewDTO, photos);
        return "redirect:/review/reviewList";
    }

    @PostMapping("/reviewModify")
    public String modify(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
                         ReviewDTO reviewDTO, MultipartFile[] photos , RedirectAttributes redirectAttributes)
    {
        reviewService.modify(reviewDTO, photos);
        return "redirect:/review/reviewList"; //바꿀것
    }

    @GetMapping("/removeReview")
    public String removeReview(Long rro)
    {
        reviewService.removeReviewWithReviewImage(rro);
        return "redirect:/review/reviewList";
    }


    @GetMapping("/getPhoto")
    public ResponseEntity<byte[]> getPhoto(String fileName){

        return reviewService.getPhotoFile(fileName);
    }

    @ResponseBody
    @PostMapping("/getLikeData")
    public LikeDTO getLikeData(Long rro){

        return reviewService.getReviewLikeData(rro);
    }

    @ResponseBody
    @PostMapping("/clickLike")
    public ResponseEntity<String> clickLike(Long rro){

        reviewService.clickLikeReview(rro);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
