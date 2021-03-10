package org.zerock.member_board.service;

import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.dto.PageResultDTO;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.redis.Attend;

import javax.servlet.http.HttpSession;

public interface BoardService {

    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    BoardDTO get(Long bno);

    void removeWithReplyies(Long bno);

    void modify(BoardDTO boardDTO);

    default Board dtoToEntity(BoardDTO dto){

        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .costs(dto.getCosts())
                .place(dto.getPlace())
                .position(dto.getPosition())
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
                .writerName(member.getName())
                .costs(board.getCosts())
                .place(board.getPlace())
                .curCnt(Long.valueOf(attend.getCurrentCnt()))
                .reqCnt(Long.valueOf(attend.getRequiredCnt()))
                .members(attend.getMembers())
                .position(board.getPosition())
                .replyCount(replyCount.intValue())
                .build();

        return boardDTO;
    }


}
