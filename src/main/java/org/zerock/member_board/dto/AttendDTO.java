package org.zerock.member_board.dto;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AttendDTO {

    private String id;

    private String currentCnt;

    private String requiredCnt;

    private List<String> members;
}
