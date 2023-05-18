package net.mapomi.mapomi.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class OauthDto {
    @ApiModelProperty(example = "액세스 토큰")
    private String accessToken;
}
