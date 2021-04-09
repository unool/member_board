package org.zerock.member_board.repository.querydsl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.repository.BoardRepository;
@Transactional
@SpringBootTest
public class SearchBoardRepositoryTests {

    @Autowired
    SearchBoardRepositoryImpl searchBoardRepository;

    @Test
    public void tempTest(){
//
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        Page<Object[]> result = searchBoardRepository.searchBoard(null,"모여",
//                true,1L,100L, pageable);
//
//        for(Object[] ob: result)
//        {
//            System.out.println(ob.toString());
//        }
//        System.out.println(result.getTotalElements());
    }


}
