package org.zerock.member_board.repository;

import org.springframework.data.repository.CrudRepository;
import org.zerock.member_board.entity.redis.Like;

public interface LikeRepository extends CrudRepository<Like,String> {

}
