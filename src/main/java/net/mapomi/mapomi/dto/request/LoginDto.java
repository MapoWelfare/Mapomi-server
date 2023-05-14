package net.mapomi.mapomi.dto.request;

import lombok.Getter;

@Getter
public class LoginDto {
    private String id;
    private String password;

    public LoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
