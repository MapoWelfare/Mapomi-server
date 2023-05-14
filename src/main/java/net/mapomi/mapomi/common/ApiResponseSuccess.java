package net.mapomi.mapomi.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ApiResponseSuccess {
    @ApiModelProperty(value = "true", example = "true")
    private boolean success;
    @ApiModelProperty(value = "{ }", example = "{ }")
    private String data;
}
