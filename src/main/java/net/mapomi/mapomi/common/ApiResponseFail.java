package net.mapomi.mapomi.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class ApiResponseFail {
    @ApiModelProperty(value = "false", example = "false")
    private boolean success;
    private String message;
}
