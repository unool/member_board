package org.zerock.member_board.service;

import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.dto.PageResultDTO;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.redis.Attend;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface BoardService {

    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    BoardDTO get(Long bno);

    void removeWithReplyies(Long bno);

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

//        LocalDateTime newDate =  LocalDateTime.parse(board.getLimitDate().toString(), DateTimeFormatter.ISO_DATE_TIME);

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .costs(board.getCosts())
                .place(board.getPlace())
                .curCnt(Long.valueOf(attend.getCurrentCnt()))
                .reqCnt(Long.valueOf(attend.getRequiredCnt()))
                .members(attend.getMembers())
                .position(board.getPosition())
                .replyCount(replyCount.intValue())
//                .limitDate(newDate.toString())
                .limitDate(board.getLimitDate().toString())
                .end(board.getEnd())
                .build();

        return boardDTO;
    }


    void confirm(Long bno);


}
