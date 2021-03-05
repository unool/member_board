package org.zerock.member_board.entity.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Kind {
    NAVER("KIND_NAVER", "네이버유저"),
    GOOGLE("KIND_GOOGLE", "구글유저"),
    NORMAL("KIND_NORMAL", "일반유저");
    private final String key;
    private final String title;

}


