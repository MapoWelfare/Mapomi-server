package net.mapomi.mapomi.dto.request;

import lombok.Getter;

@Getter
public class LoginDto {
    private String nickName;

    public LoginDto(String nickName) {
        this.nickName = nickName;
    }
}
