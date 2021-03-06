package org.zerock.member_board.service;
import org.springframework.validation.Errors;
import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.dto.PageResultDTO;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.redis.Attend;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public interface BoardService {

    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);

    BoardDTO get(Long bno);

    void removeWithReplyiesAndReviewAll(Long bno);

    void modify(BoardDTO boardDTO);

    default Board dtoToEntity(BoardDTO dto){

        Member member = Member.builder().email(dto.getWriterEmail()).build();
        LocalDateTime newDate = LocalDateTime
                .parse(dto.getLimitDate(), DateTimeFormatter.ISO_DATE_TIME); //datetime-local에서 읽을 수 있는형태로 저장

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .costs(dto.getCosts())
                .place(dto.getPlace())
                .position(dto.getPosition())
                .limitDate(newDate)
                .end(dto.getEnd())
                .build();
        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount, Attend attend){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .costs(board.getCosts())
                .place(board.getPlace())
                .curCnt(Long.valueOf(attend.getCurrentCnt()))
                .reqCnt(Long.valueOf(attend.getRequiredCnt()))
                .members(attend.getMembers())
                .position(board.getPosition())
                .replyCount(replyCount.intValue())
                .limitDate(board.getLimitDate().toString())
                .end(board.getEnd())
                .build();

        return boardDTO;
    }


    void confirm(Long bno);

    List<BoardDTO> getRecentBoard();

    Map<String, String> validateHandling(Errors errors);

}
