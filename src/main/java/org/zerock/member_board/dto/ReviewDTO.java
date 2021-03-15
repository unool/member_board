package org.zerock.member_board.dto;

import lombok.*;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.ReviewImage;
import org.zerock.member_board.entity.redis.Like;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Setter
@Getter
public class ReviewDTO {

    private Long rro;

    private String title;

    private String content;

    private String writerEmail;

    private Long bno;

    private Like like;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private List<ReviewImageDTO> imageList;



    //삭제삭제
    public String getTest(){
        return "i love you";
    }


}
