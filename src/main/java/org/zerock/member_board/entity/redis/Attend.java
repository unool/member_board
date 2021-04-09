package org.zerock.member_board.entity.redis;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.util.List;

@Builder
@Setter
@Getter
@RedisHash("Attend")
public class Attend {

    @Id
    private String id;

    private String currentCnt;

    private String requiredCnt;

    private List<String> members;
}
