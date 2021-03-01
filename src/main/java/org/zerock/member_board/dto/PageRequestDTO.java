package org.zerock.member_board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Log4j2
@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO { //현재 페이지에 대한 정보를 담고 있는것 같은 DTO


    private int page;
    private int size;
    private String type;
    private String keyword;


    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;

    }

    public Pageable getPageable(Sort sort){
        log.info("getPageable......... " + page +" _ " + size);

        return PageRequest.of(page -1, size, sort); //repository.findAll 할때는 0부터 가져오라고 해야 하기 때문에 -1.
        //현재 Pageable 에서 세팅이 페이지의 첫번째는 0이라고 설정이 되있는듯

    }
}
