package net.mapomi.mapomi.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Getter
public class PostBuildDto {
    @ApiModelProperty(example = "동행글 제목")
    @NotEmpty
    private String title;
    @ApiModelProperty(example = "동행글 내용")
    private String content;
    @ApiModelProperty(example = "동행 예상 일시+시간")
    private String schedule;
    @ApiModelProperty(example = "예상 소요시간")
    private String duration;
    @ApiModelProperty(example = "출발지")
    private String departure;
    @ApiModelProperty(example = "도착지")
    private String destination;

    @Builder
    public PostBuildDto(String title, String content, String schedule, String duration, String departure, String destination) {
        this.title = title;
        this.content = content;
        this.schedule = schedule;
        this.duration = duration;
        this.departure = departure;
        this.destination = destination;
    }
}
