package org.zerock.member_board.entity;
import lombok.*;
import org.zerock.member_board.dto.BoardDTO;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString(exclude = "writer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member writer;

    @Column(name = "meeting_costs")
    private Long costs;

    @Column(name = "meeting_place")
    private String place;

    @Column(name = "position")
    private String position;

    @Column(name = "limitdate")
    private LocalDateTime limitDate;

    @Column(name = "end")
    private Boolean end;


    public Board changeValue(BoardDTO boardDTO)
    {
        LocalDateTime newDate = LocalDateTime
                .parse(boardDTO.getLimitDate(), DateTimeFormatter.ISO_DATE_TIME); //datetime-local에서 읽을 수 있는형태로 저장


        this.title = boardDTO.getTitle();
        this.content = boardDTO.getContent();
        this.costs = boardDTO.getCosts();
        this.place = boardDTO.getPlace();
        this.position = boardDTO.getPosition();
        this.limitDate = newDate;

        return this;
    }


}
