package net.mapomi.mapomi.domain;

import lombok.Builder;
import lombok.Getter;
import net.mapomi.mapomi.common.BaseTimeEntity;
import net.mapomi.mapomi.domain.user.Disabled;
import net.mapomi.mapomi.dto.request.PostBuildDto;

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
    private String author = "";

    @Column
    private int views = 2;

    @Column
    private String schedule = "";

    @Column
    private String type = "";

    @Column
    private boolean complete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disabled_id")
    private Disabled disabled;

    @Builder
    public Post(String title, String content, String author, String type, String schedule) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.type = type;
        this.schedule = schedule;
        this.views = 2;
    }

    public void editPost(PostBuildDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.schedule = dto.getSchedule();
    }

    public void setDisabled(Disabled disabled) {
        this.disabled = disabled;
    }
}

