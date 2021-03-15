package org.zerock.member_board.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = "writer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member writer;

    @Column(name = "meeting_costs")
    private String costs;

    @Column(name = "meeting_place")
    private String place;

    @Column(name = "position")
    private String position;

    @Column(name = "limitdate")
    private LocalDateTime limitDate;

    @Column(name = "end")
    private Boolean end;


    public void changeContent(String content){
        this.content = content;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeEnd(Boolean end){
        this.end = end;
    }
}
