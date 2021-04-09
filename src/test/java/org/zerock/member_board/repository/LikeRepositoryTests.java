package org.zerock.member_board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.entity.redis.Like;

import java.util.ArrayList;
import java.util.stream.IntStream;
@Transactional
@SpringBootTest
public class LikeRepositoryTests {

    @Autowired
    LikeRepository likeRepository;


    @Test
    public void Insert()
    {
        IntStream.rangeClosed(1,20).forEach(i->{

            Like like = Like.builder()
                    .id(String.valueOf(i))
                    .likeCnt(String.valueOf(0))
                    .likeMembers(new ArrayList<>())
                    .build();

            likeRepository.save(like);
        });
    }
}
