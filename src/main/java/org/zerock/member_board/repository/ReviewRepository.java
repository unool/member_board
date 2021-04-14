package org.zerock.member_board.repository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.entity.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("select r, ri, m, b from Review r left outer join ReviewImage ri on ri.review = r left outer join " +
            "r.writer m left outer join r.board b group by r") //첫번째 걸리는 이미지 아무거나 하나 가져오게, group by r로 묶어서
    Page<Object[]> getReviewListWithReviewImageMemberBoard(Pageable pageable);

    @Query("select r, ri, m, b from Review r left outer join ReviewImage ri on ri.review = r left outer join " +
            "r.writer m left outer join r.board b where r.rro = :rro")
    List<Object[]> getReviewWithReviewImageMemberBoard(@Param("rro") Long rro);

    @Query("select r,max(ri) from Review r left join ReviewImage ri on ri.review = r") //모든 이미지 가져오게
    Page<Object[]> getReviewWithAllReviewImage(Pageable pageable);


    @Transactional
    @Modifying
    @Query("update Review r set r.title = :title , r.content = :content where r.rro = :rro")
    void updateReviewTitleContent(@Param("title") String title,
                                  @Param("content") String content,
                                  @Param("rro") Long rro);

    @Query("select r from Review r where r.board.bno = :bno")
    List<Review> getReviewsByBno(Long bno);

//    @Query("select r, ri from Review r left outer join ReviewImage ri on ri.review = r group by r") //이미지 하나만 가져오면 됨
//    List<Object[]> getRecentReviewReviewImage()
}
