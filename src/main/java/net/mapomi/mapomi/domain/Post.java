package net.mapomi.mapomi.domain;

import io.swagger.annotations.ApiModelProperty;
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
    private String schedule = ""; //날짜+일시

    @Column
    private String duration = ""; //소요시간

    @Column
    private String departure;

    @Column
    private String destination;

    @Column
    private String type = "";

    @Column
    private boolean complete = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disabled_id")
    private Disabled disabled;

    @Builder
    public Post(String title, String content, String author, String schedule, String duration, String departure, String destination, String type) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.schedule = schedule;
        this.duration = duration;
        this.departure = departure;
        this.destination = destination;
        this.type = type;
    }

    protected Post() {}

    public void editPost(PostBuildDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.schedule = dto.getSchedule();
    }

    public void setDisabled(Disabled disabled) {
        this.disabled = disabled;
    }

    public void addViews() {
        this.views+=2;
    }
}

