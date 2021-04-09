package org.zerock.member_board.entity.redis;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import javax.persistence.Id;
import java.util.List;


@Builder
@Setter
@Getter
@RedisHash("Like")
public class Like {

    @Id
    private String id;

    private String likeCnt;

    private List<String> likeMembers;
}

