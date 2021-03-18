package org.zerock.member_board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.dto.PageResultDTO;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.Participation;
import org.zerock.member_board.entity.User;
import org.zerock.member_board.entity.redis.Attend;
import org.zerock.member_board.repository.AttendRepository;
import org.zerock.member_board.repository.BoardRepository;
import org.zerock.member_board.repository.ParticipationRepository;
import org.zerock.member_board.repository.ReplyRepository;

import org.zerock.member_board.repository.querydsl.SearchBoardRepositoryImpl;
import org.zerock.member_board.service.util.MemberHandler;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Transactional
@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final AttendRepository attendRepository;
    private final ParticipationRepository participationRepository;
    private final SearchBoardRepositoryImpl searchBoardRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long register(BoardDTO dto) {

        Board board = dtoToEntity(dto);

        Board savedBoard = boardRepository.save(board);

        String email = MemberHandler.GetMemberEmail();

        List<String> members = new ArrayList<>();
        members.add(email);

        Attend attend = Attend.builder()
                .id(savedBoard.getBno().toString())
                .currentCnt(String.valueOf(1))
                .requiredCnt(dto.getReqCnt().toString())
                .members(members)
                .build();

        attendRepository.save(attend);

        return savedBoard.getBno();
    }


    //Spring Data JPA만 사용 (검색 없는 버전)
//    @Override
//    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
//
//        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageRequestDTO.getPageable(
//                Sort.by("bno").descending()));
//
//        List<BoardDTO> boardDTOList = new ArrayList<>();
//
//        for(Object[] objects : result)
//        {
//            Board board = (Board) objects[0];
//
//            Optional<Attend> attend = attendRepository.findById(board.getBno().toString());
//
//            BoardDTO boardDTO = entityToDTO((Board)objects[0],(Member) objects[1],(Long)objects[2], attend.get());
//
//            boardDTOList.add(boardDTO);
//        }
//
//        int totalPage = result.getTotalPages();
//
//        return new PageResultDTO<>(boardDTOList, totalPage, result.getPageable());
//    }


    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Page<Object[]> result = searchBoardRepository.searchBoard(pageRequestDTO.getType(), pageRequestDTO.getTypeKeyword(),
                pageRequestDTO.isRegion(), pageRequestDTO.getRegionKeyword(), pageRequestDTO.getMinCost(), pageRequestDTO.getMaxCost(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()));

        List<BoardDTO> boardDTOList = new ArrayList<>();

        for(Object[] objects : result)
        {
            Board board = (Board) objects[0];

            Optional<Attend> attend = attendRepository.findById(board.getBno().toString());

            BoardDTO boardDTO = entityToDTO((Board)objects[0],(Member) objects[1],(Long)objects[2], attend.get());

            boardDTOList.add(boardDTO);
        }

        return new PageResultDTO<>(boardDTOList, result.getTotalPages(), result.getPageable());
    }

    @Override
    public BoardDTO get(Long bno) {

        String redisID = bno.toString();
        Optional<Attend> result = attendRepository.findById(redisID);
        Attend attend = result.get();

        Object object = boardRepository.getBoardByBno(bno);
        Object[] arr = (Object[]) object;
        return entityToDTO((Board)arr[0], (Member) arr[1], (Long)arr[2], attend);
    }

    @Override
    public void removeWithReplyies(Long bno) {

        replyRepository.deleteByBno(bno);

        boardRepository.deleteById(bno);
    }


    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = boardRepository.getOne(boardDTO.getBno());

        if(!board.equals(null))
        {
            board.changeValue(boardDTO);
        }
    }

    @Override
    public void confirm(Long bno) {
        Board board = boardRepository.getOne(bno);

        if(board != null)
        {

            if(board.getEnd() == true) //이미 처리됨..
            {
                return;
            }

            board.setEnd(true);

            Optional<Attend> result = attendRepository.findById(bno.toString());

            Attend attend = result.get();

            List<String> members = attend.getMembers();

            for(String email : members)
            {
                Board tempBoard = Board.builder()
                        .bno(bno).build();

                Member tempMember = Member.builder()
                        .email(email)
                        .build();

                Participation participation = Participation.builder()
                        .bno(tempBoard)
                        .member(tempMember)
                        .build();
                participationRepository.save(participation);

            }


        }

    }
}
