package net.mapomi.mapomi.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class JoinDto {
    @ApiModelProperty(example = "닉네임")
    String nickname;
    @ApiModelProperty(example = "폰 번호")
    String phoneNum;

    public JoinDto(String nickname, String phoneNum) {
        this.nickname = nickname;
        this.phoneNum = phoneNum;
    }
}
