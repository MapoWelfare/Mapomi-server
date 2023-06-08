package net.mapomi.mapomi.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class JoinDto {
    @ApiModelProperty(example = "닉네임")
    String nickname;
    @ApiModelProperty(example = "폰 번호")
    String phoneNum;
    @ApiModelProperty(example = "이메일")
    String email;



    public JoinDto(String nickname, String phoneNum,String email) {
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.email = email;
    }
}
