package org.zerock.member_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.member_board.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
