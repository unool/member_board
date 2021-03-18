package org.zerock.member_board.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {

    public Page<Object[]> searchBoard(boolean type[], String typeKeyword, boolean region,
                                      String regionKeyword, Long minMoney, Long maxMoney,
                                      Pageable pageable);
}
