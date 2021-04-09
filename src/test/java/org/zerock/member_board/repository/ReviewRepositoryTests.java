package org.zerock.member_board.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
@Transactional
@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    ReviewRepository reviewRepository;


    @Test
    public void SelectTest(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("rro").descending());
        Page<Object[]> result = reviewRepository.getReviewListWithReviewImageMemberBoard(pageable);

        for(Object[] arr : result)
        {
            System.out.println("=======================");
            System.out.println(Arrays.toString(arr));
            System.out.println("=======================");
        }
    }


}
