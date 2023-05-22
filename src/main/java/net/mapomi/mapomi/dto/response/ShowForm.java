package net.mapomi.mapomi.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ShowForm implements Serializable {
    @ApiModelProperty(example = "글 ID")
    private Long postId;
    @ApiModelProperty(example = "작품 판매글 제목")
    private String title;
    @ApiModelProperty(example = "게시 일자")
    private String date;
    @ApiModelProperty(example = "예약 일시")
    private String schedule;
    @ApiModelProperty(example = "출발지")
    private String departure;
    @ApiModelProperty(example = "도착지")
    private String destination;
    @ApiModelProperty(example = "프로필 사진")
    private String picture;
    @ApiModelProperty(example = "동행완료 여부",notes = "true -> 완료")
    private boolean complete;

    protected ShowForm() {}

    @Builder
    public ShowForm(Long postId, String title, String date, String schedule, String departure, String destination, String picture, boolean complete) {
        this.postId = postId;
        this.title = title;
        this.date = date;
        this.schedule = schedule;
        this.departure = departure;
        this.destination = destination;
        this.picture = picture;
        this.complete = complete;
    }
}
