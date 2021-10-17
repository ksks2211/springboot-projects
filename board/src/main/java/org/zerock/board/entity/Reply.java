package org.zerock.board.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude="board")
public class Reply extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long rno;
    private String text;
    private String replier;

    @ManyToOne(fetch=FetchType.LAZY)
    private Board board;
}
