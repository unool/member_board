package org.zerock.member_board.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member extends BaseEntity{

    @Id
    private String email;

    private String password;

    private String name;
}
