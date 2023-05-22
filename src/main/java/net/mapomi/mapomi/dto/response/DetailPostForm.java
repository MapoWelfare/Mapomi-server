package net.mapomi.mapomi.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import net.mapomi.mapomi.domain.user.Disabled;


@Getter
public class DetailPostForm {
    private Long postId;
    private String title;
    private Long userId;
    private String picture;
    private String content;
    @ApiModelProperty(example = "글 작성자의 닉네임")
    private String author;
    private int views;
    @ApiModelProperty(example = "날짜+시간")
    private String schedule;
    @ApiModelProperty(example = "소요시간")
    private String duration;
    @ApiModelProperty(example = "출발지")
    private String departure;
    @ApiModelProperty(example = "도착지")
    private String destination;
    private String type;
    private boolean complete;

    @Builder
    public DetailPostForm(Long postId, String title, String content, int views, String schedule, String duration, String departure, String destination, boolean complete) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.views = views;
        this.schedule = schedule;
        this.duration = duration;
        this.departure = departure;
        this.destination = destination;
        this.complete = complete;
    }


    public void setUserInfo(Disabled disabled) {
        this.userId = disabled.getId();
        this.picture = disabled.getPicture();
        this.author = disabled.getNickName();
        this.type = disabled.getType();
    }
}
