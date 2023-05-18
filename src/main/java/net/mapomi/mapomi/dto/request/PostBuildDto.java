package net.mapomi.mapomi.dto.request;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "동행 예상 일시")
    private String schedule;

    @ApiModelProperty(example = "출발지")
    private String departure;
    @ApiModelProperty(example = "도착지")
    private String destination;
    @ApiModelProperty(example = "예상 시간")
    private String time;

    public PostBuildDto(String title, String content, String schedule, String departure, String destination, String time) {
        this.title = title;
        this.content = content;
        this.schedule = schedule;
        this.departure = departure;
        this.destination = destination;
        this.time = time;
    }
}
