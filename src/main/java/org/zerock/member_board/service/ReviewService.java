package org.zerock.member_board.service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.member_board.dto.*;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.Review;
import org.zerock.member_board.entity.ReviewImage;
import org.zerock.member_board.entity.redis.Like;
import org.zerock.member_board.service.util.LogManager;
import java.util.ArrayList;
import java.util.List;


public interface ReviewService {

    List<ParticipationDTO> getList(String email);

    Long register(ReviewDTO dto, MultipartFile[] photos);

    PageResultDTO<ReviewDTO> getList(PageRequestDTO pageRequestDTO);

    ReviewDTO get(Long rro);

    void modify(ReviewDTO reviewDTO, MultipartFile[] photos);

//    예외처리로 인해 현재 쓰이지 않음
//    default Review dtoToEntity(ReviewDTO dto){
//
//        Review review = null;
//
//        try{
//            Board board = Board.builder()
//                    .bno(dto.getBno())
//                    .build();
//
//            Member member = Member.builder()
//                    .email(dto.getWriterEmail())
//                    .build();
//
//            review = Review.builder()
//                    .title(dto.getTitle())
//                    .content(dto.getContent())
//                    .board(board)
//                    .writer(member)
//                    .build();
//        }
//        catch (Exception e)
//        {
//            LogManager.log(e.getMessage());
//        }
//
//
//        return review;
//    }

    default Review dtoToEntity(Board board, Member member, ReviewDTO dto){

        Review review = null;

        try{
            Board tempBoard = Board.builder()
                    .bno(board.getBno())
                    .build();

            Member tempMember = Member.builder()
                    .email(member.getEmail())
                    .build();

            review = Review.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .board(tempBoard)
                    .writer(tempMember)
                    .build();
        }
        catch (Exception e)
        {
            LogManager.log(e.getMessage());
        }


        return review;
    }

    default ReviewDTO entityToDTO(Review review, List<ReviewImage> imageList, Member member, Board board, Like like){

        List<ReviewImageDTO> list = new ArrayList<>();
        if(imageList != null)
        {
            if(imageList.size() > 0)
            {
                int i = 0;

                for(ReviewImage reviewImage : imageList)
                {
                    if(reviewImage == null)
                    {
                        break;
                    }
                    ReviewImageDTO reviewImageDTO = ReviewImageDTO.builder()
                            .inum(reviewImage.getInum())
                            .uuid(reviewImage.getUuid())
                            .imgName(reviewImage.getImgName())
                            .path(reviewImage.getPath())
                            .build();
                    list.add(reviewImageDTO);
                }
            }
        }

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .rro(review.getRro())
                .bno(board.getBno())
                .title(review.getTitle())
                .content(review.getContent())
                .writerEmail(member.getEmail())
                .like(like)
                .imageList(list)
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();

        return reviewDTO;
    }

    ResponseEntity<byte[]> getPhotoFile(String fileName);

    LikeDTO getReviewLikeData(Long rro);

    void clickLikeReview (Long rro);

    void removeReviewWithReviewImage(Long rro);

    List<ReviewDTO> getRecentReview();
}
