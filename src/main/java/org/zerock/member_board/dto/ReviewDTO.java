package org.zerock.member_board.dto;
import lombok.*;
import org.zerock.member_board.entity.redis.Like;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "like")
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

    @Builder.Default
    private List<ReviewImageDTO> imageList = new ArrayList<>();

}
