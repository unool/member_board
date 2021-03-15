package org.zerock.member_board.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zerock.member_board.entity.*;
import org.zerock.member_board.entity.redis.Attend;
import org.zerock.member_board.entity.role.Kind;
import org.zerock.member_board.entity.role.Role;
import org.zerock.member_board.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class InsertTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    AttendRepository attendRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ReviewImageRepository reviewImageRepository;

    @Autowired
    ParticipationRepository participationRepository;

    @Test
    public void test(){
        IntStream.rangeClosed(1,20).forEach(i->{

            Member member = Member.builder()
                    .email("unool1@yahoo.co.kr")
                    .build();

            Board board = Board.builder()
                    .writer(member)
                    .content("content.. " + i)
                    .title("title..." + i)
                    .costs("1000"+i)
                    .place("경기도 의정부시 의정부동 34-3 | 경기도 의정부시 태평로 100")
                    .end(true)
                    .position("lat:37.7412983, lng:127.0518019")
                    .limitDate(LocalDateTime.now())
                    .build();


            Board resultBoard =boardRepository.save(board);


            List<String> members = new ArrayList<>();

            members.add("unool1@yahoo.co.kr");
            members.add("unool5@yahoo.co.kr");

            Attend attend = Attend.builder()
                    .id(resultBoard.getBno().toString())
                    .currentCnt("2")
                    .members(members)
                    .requiredCnt("5")
                    .build();


            attendRepository.save(attend);


            Participation participation = Participation.builder()
                    .bno(board)
                    .member(member)
                    .build();

            Member member2 = Member.builder()
                    .email("unool5@yahoo.co.kr")
                    .build();
            Participation participation2 = Participation.builder()
                    .bno(board)
                    .member(member2)
                    .build();


            participationRepository.save(participation);
            participationRepository.save(participation2);




            Member writer = Member.builder()
                    .email("unool1@yahoo.co.kr")
                    .build();

            Review review = Review.builder()
                    .title("bno ... " + i + "..review")
                    .board(board)
                    .content("content... " + i)
                    .writer(writer)
                    .build();
            reviewRepository.save(review);

            ReviewImage reviewImage = ReviewImage.builder()
                    .uuid("111")
                    .imgName("imageName")
                    .path("ddd")
                    .review(review)
                    .build();

            reviewImageRepository.save(reviewImage);

        });


    }
}
