package org.zerock.member_board.controller;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member_board.dto.LikeDTO;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.dto.ParticipationDTO;
import org.zerock.member_board.dto.ReviewDTO;
import org.zerock.member_board.service.ReviewService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@RequestMapping("/review")
@Controller
@RequiredArgsConstructor
public class ReviewController {

    Logger loggger = LoggerFactory.getLogger(ReviewController.class);



    @Autowired
    private final ReviewService reviewService;

    @GetMapping("/reviewList")
    public void list(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model){

        model.addAttribute("result",   reviewService.getList(pageRequestDTO));
    }

    @GetMapping({"/reviewRead", "/reviewModify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long rro, Model model)
    {
        model.addAttribute("dto",reviewService.get(rro));
    }

    @GetMapping("/reviewRegister")
    public void register(String email, Model model)
    {

        model.addAttribute("dtoList", reviewService.getList(email)); //작성 가능한 보드를 가져오기위해
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
