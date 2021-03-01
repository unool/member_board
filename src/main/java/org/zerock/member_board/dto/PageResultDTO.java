package org.zerock.member_board.dto;


import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Data
public class PageResultDTO<DTO, EN> {


    //list
    private List<DTO> dtoList;

    //total page num
    private int totalPage;

    //cur page num
    private int page;

    //list size
    private int size;

    //start, end page num
    private int start, end;

    //pre, nex
    private  boolean prev, next;

    //page num list
    private List<Integer> pageList;



    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){

        dtoList = result.stream().map(fn).collect(Collectors.toList()); //Page<Entity> 형태의 DB 데이터를 DTO 리스트로 변환 (서비스에서 데이터 핸들링 하기위해)

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());

    }

    private void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();


        //tmp end page

        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;


        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());


    }
}
