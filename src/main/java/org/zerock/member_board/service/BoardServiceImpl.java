package org.zerock.member_board.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.zerock.member_board.dto.BoardDTO;
import org.zerock.member_board.dto.PageRequestDTO;
import org.zerock.member_board.dto.PageResultDTO;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.Participation;
import org.zerock.member_board.entity.Review;
import org.zerock.member_board.entity.redis.Attend;
import org.zerock.member_board.repository.*;
import org.zerock.member_board.repository.querydsl.SearchBoardRepositoryImpl;
import org.zerock.member_board.service.util.MemberHandler;

import java.util.*;


@Transactional
@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final AttendService attendService;
    private final ReviewServiceImpl reviewService;
    private final ReviewRepository reviewRepository;
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
    public PageResultDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {

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
    public void removeWithReplyiesAndReviewAll(Long bno) { //한 게시물에 대한 모든 데이터 삭제

        List<Review> result = reviewRepository.getReviewsByBno(bno);

        //리뷰 전체 삭제
        for(Review review : result)
        {
            //리뷰 로우, 리뷰 이미지 로우, 리뷰 실제 이미지 삭제, 레디스 좋아요 삭제
            reviewService.removeReviewWithReviewImageAndLike(review.getRro());
//            reviewImageRepository.deleteReviewImageByReview(review); //리뷰에 딸린 리뷰이미지 삭제
//            reviewRepository.delete(review); //리뷰 자체 삭제

        }

        participationRepository.deleteByBno(bno);  //참가 전체 삭제
        attendService.deleteAttend(bno); //레디스 참석 정보 삭제
        replyRepository.deleteByBno(bno); //댓글 전체 삭제
        boardRepository.deleteById(bno); //본 게시물 삭제


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

    @Override
    public List<BoardDTO> getRecentBoard() {

        List<Board> list = boardRepository.findTop3ByOrderByBnoDesc(); //메소드 이름으로 쿼리 생성
        List<BoardDTO> dtoList = new ArrayList<>();

        for(Board board : list)
        {
            BoardDTO boardDTO = BoardDTO.builder()
                    .bno(board.getBno())
                    .position(board.getPosition())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .place(board.getPlace())
                    .costs(board.getCosts())
                    .build();

            dtoList.add(boardDTO);
        }

        return dtoList;
    }

    @Override
    public Map<String, String> validateHandling(Errors errors) {

        Map<String, String> validatorResult = new HashMap<>();

        for(FieldError error : errors.getFieldErrors())
        {
            String fieldName = String.format("valid_%s",error.getField());
            validatorResult.put(fieldName,error.getDefaultMessage());
        }

        return validatorResult;
    }

}
