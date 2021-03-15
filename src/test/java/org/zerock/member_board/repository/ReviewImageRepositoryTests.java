package org.zerock.member_board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.member_board.entity.Review;
import org.zerock.member_board.entity.ReviewImage;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

@SpringBootTest
public class ReviewImageRepositoryTests {

    @Autowired
    ReviewImageRepository reviewImageRepository;


    @Test
    public void Insert(){

        LongStream.rangeClosed(1, 20).forEach(i->{

            Review review = Review.builder()
                    .rro(i)
                    .build();

            ReviewImage reviewImage = ReviewImage.builder()
                    .uuid("222")
                    .imgName("2imageName")
                    .path("ddd")
                    .review(review)
                    .build();

            reviewImageRepository.save(reviewImage);
        });

    }


}
