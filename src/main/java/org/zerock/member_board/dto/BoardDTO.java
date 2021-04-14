package org.zerock.member_board.dto;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;

    @Size(min = 2, max = 20)
    @NotEmpty(message = "제목은 빈값 일 수 없습니다")
    @NotNull(message = "제목은 Null 일 수 없습니다")
    private String title; //필수

    @Size(min = 2, max = 200)
    @NotEmpty(message = "내용은 빈값 일 수 없습니다")
    @NotNull(message = "내용은 Null 일 수 없습니다")
    private String content; //필수

    @NotEmpty(message = "작성자는 빈값 일 수 없습니다")
    @NotNull(message = "작성자는 Null 일 수 없습니다")
    private String writerEmail; //필수


    @NotNull(message = "비용은 Null 일 수 없습니다")
    private Long costs; //필수

    @NotEmpty(message = "장소는 빈값 일 수 없습니다")
    @NotNull(message = "장소는 Null 일 수 없습니다")
    private String place; //필수

    private Long curCnt;

    private Long reqCnt;

//    place가 들어가면 무조건 들어가는 값이기때문에 주석처리
//    @NotEmpty(message = "위치값은 빈값 일 수 없습니다")
//    @NotNull(message = "위치값은 Null 일 수 없습니다")
    private String position; //필수

    private List<String> members;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    @NotEmpty(message = "제한날짜는 빈값 일 수 없습니다")
    @NotNull(message = "제한날짜는 Null 일 수 없습니다")
    private String limitDate; //필수

    private int replyCount;


    @NotNull(message = "종료는 Null 일 수 없습니다")
    private Boolean end; //필수

}
