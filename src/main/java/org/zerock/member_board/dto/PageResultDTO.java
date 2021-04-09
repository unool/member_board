package org.zerock.member_board.dto;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Data
public class PageResultDTO<DTO> {


    private List<DTO> dtoList;

    private int totalPage;

    private int page;

    private int size;

    private int start, end;

    private  boolean prev, next;

    private List<Integer> pageList;

    public PageResultDTO(List<DTO> list, int totalPage, Pageable pageable){

        this.dtoList = list; //Page<Entity> 형태의 DB 데이터를 DTO 리스트로 변환 (서비스에서 데이터 핸들링 하기위해)
        this.totalPage = totalPage;
        makePageList(pageable);
    }

    private void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber() + 1; //0일 경우 /10일때 0이므로 +1
        this.size = pageable.getPageSize();
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10; // 총 게시물이 적을 경우 구상한 페이지 쪽수보다 적을 수 있기 때문에
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
