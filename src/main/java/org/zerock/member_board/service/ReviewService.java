package org.zerock.member_board.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.member_board.dto.*;
import org.zerock.member_board.entity.Board;
import org.zerock.member_board.entity.Member;
import org.zerock.member_board.entity.Review;
import org.zerock.member_board.entity.ReviewImage;
import org.zerock.member_board.entity.redis.Attend;
import org.zerock.member_board.entity.redis.Like;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface ReviewService {

    List<ParticipationDTO> getList(String email);

    Long register(ReviewDTO dto, MultipartFile[] photos);

    PageResultDTO<ReviewDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    ReviewDTO get(Long rro);

    void modify(ReviewDTO reviewDTO, MultipartFile[] photos);

    default Review dtoToEntity(ReviewDTO dto){

        Board board = Board.builder()
                .bno(dto.getBno())
                .build();

        Member member = Member.builder()
                .email(dto.getWriterEmail())
                .build();

        Review review = Review.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .board(board)
                .writer(member)
                .build();

        return review;
    }

    default ReviewDTO entityToDTO(Review review, List<ReviewImage> imageList, Member member, Board board, Like like){

        List<ReviewImageDTO> list = new ArrayList<>();

        System.out.println("===================");

        System.out.println("rro : "+review.getRro());

        if(imageList != null)
        {
            if(imageList.size() > 0)
            {

                System.out.println("size : "+imageList.size());

                int i = 0;

                for(ReviewImage reviewImage : imageList)
                {
                    if(reviewImage == null)
                    {
                        break;
                    }
                    System.out.println(i++);

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
}
