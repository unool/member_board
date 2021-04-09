package org.zerock.member_board.entity;
import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Participation extends BaseEntity{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long pno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board bno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
