package net.mapomi.mapomi.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinDto {
    @ApiModelProperty(example = "중복체크된 아이디")
    String id;
    @ApiModelProperty(example = "비번")
    String password;
    @ApiModelProperty(example = "실명")
    String name;
    @ApiModelProperty(example = "폰 번호")
    String phoneNum;
    @ApiModelProperty(example = "닉네임")
    String nickname;
    @ApiModelProperty(example = "약관")
    boolean term;
    @ApiModelProperty(example = "거주지")
    String residence;
    @ApiModelProperty(example = "나이")
    int age;
    @ApiModelProperty(example = "장애정보")
    String type;

    @Builder
    public JoinDto(String id, String password, String name, String phoneNum, String nickname, boolean term, String residence, int age, String type) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.nickname = nickname;
        this.term = term;
        this.residence = residence;
        this.age = age;
        this.type = type;
    }
}
