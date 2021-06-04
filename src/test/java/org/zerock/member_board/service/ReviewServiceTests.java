package org.zerock.member_board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.entity.redis.Like;
import org.zerock.member_board.repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Transactional
@SpringBootTest
public class ReviewServiceTests {

    @Autowired
    LikeRepository likeRepository;


}
