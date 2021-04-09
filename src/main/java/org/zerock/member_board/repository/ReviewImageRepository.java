package org.zerock.member_board.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member_board.entity.Review;
import org.zerock.member_board.entity.ReviewImage;
import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query("select ri from ReviewImage ri where ri.review.rro = :rro")
    public List<ReviewImage> findByReviewRro(@Param("rro") Long rro);

    @Transactional
    @Modifying
    @Query("delete from ReviewImage ri where ri.review = :review")
    public void deleteReviewImageByReview(@Param("review") Review review);
}
