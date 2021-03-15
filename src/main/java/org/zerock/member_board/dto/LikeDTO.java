package org.zerock.member_board.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LikeDTO {

    private String likeCnt;

    private boolean contains;

}
