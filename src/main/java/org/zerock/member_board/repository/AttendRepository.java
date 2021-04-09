package org.zerock.member_board.repository;
import org.springframework.data.repository.CrudRepository;
import org.zerock.member_board.entity.redis.Attend;

public interface AttendRepository extends CrudRepository<Attend,String> {
}
