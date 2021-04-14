package org.zerock.member_board.dto;
import lombok.*;
import org.zerock.member_board.entity.redis.Like;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Size(min = 2, max = 20)
    @NotEmpty(message = "제목은 빈값 일 수 없습니다")
    @NotNull(message = "제목은 Null 일 수 없습니다")
    private String title;

    @Size(min = 2, max = 200)
    @NotEmpty(message = "내용은 빈값 일 수 없습니다")
    @NotNull(message = "내용은 Null 일 수 없습니다")
    private String content;


    @NotEmpty(message = "작성자는 빈값 일 수 없습니다")
    @NotNull(message = "작성자는 Null 일 수 없습니다")
    private String writerEmail;


    @NotNull(message = "참석모임은 Null 일 수 없습니다")
    private Long bno;

    private Like like;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    @Builder.Default
    private List<ReviewImageDTO> imageList = new ArrayList<>();

}
