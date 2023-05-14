package net.mapomi.mapomi.domain;

import lombok.Getter;
import net.mapomi.mapomi.common.BaseTimeEntity;
import net.mapomi.mapomi.domain.user.Disabled;

import javax.persistence.*;

@Getter
@Entity
@SequenceGenerator(name = "Post_SEQ_GEN",sequenceName = "Post_SEQ")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Post_SEQ")
    @Column(name = "product_id")
    private Long id;  //작품 번호

    @Column
    private String title;

    @Column
    private String content = "";

    @Column
    private int views = 2;

    @Column
    private String schedule = "";

    @Column
    private boolean complete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disabled_id")
    private Disabled disabled;

}

