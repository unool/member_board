package org.zerock.member_board.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ParticipationDTO {

    private Long bno;

    private String Title;
}
