package org.zerock.member_board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.member_board.entity.redis.Like;
import org.zerock.member_board.repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ReviewServiceTests {

    @Autowired
    LikeRepository likeRepository;

    @Test
    public void likeTest(){

        String rro = "50";
        Optional<Like> result = likeRepository.findById(rro);

        Like like = result.get();

        like.getLikeMembers().remove("ddd@test.com");

        likeRepository.save(like);

//        List<String> addedList = new ArrayList<>();
//        addedList.add("ddd@test.com");
//
//        like.setLikeMembers(addedList);
//
//        likeRepository.save(like);


        System.out.println("ddd");
    }
}
