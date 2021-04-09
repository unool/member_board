package org.zerock.member_board.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
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

    private String title;

    private String content;

    private String writerEmail;

    private String writerName;

    private Long costs;

    private String place;

    private Long curCnt;

    private Long reqCnt;

    private String position;

    private List<String> members;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private String limitDate;

    private int replyCount;

    private Boolean end;

}
