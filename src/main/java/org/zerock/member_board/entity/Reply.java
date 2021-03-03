package org.zerock.member_board.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString (exclude = "board")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;

//(fetch = FetchType.LAZY)
    @ManyToOne (fetch = FetchType.LAZY)
    private Board board;

    public void changeContent(String text) {
        this.text = text;
    }
}
