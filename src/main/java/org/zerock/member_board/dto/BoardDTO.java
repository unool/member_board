package org.zerock.member_board.dto;
import lombok.*;
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

    private String title; //필수

    private String content; //필수

    private String writerEmail; //필수

    private Long costs; //필수

    private String place; //필수

    private Long curCnt;

    private Long reqCnt;

    private String position; //필수

    private List<String> members;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private String limitDate; //필수

    private int replyCount;

    private Boolean end; //필수

}
