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

    public PostBuildDto(String title, String content, String schedule) {
        this.title = title;
        this.content = content;
        this.schedule = schedule;
    }
}
