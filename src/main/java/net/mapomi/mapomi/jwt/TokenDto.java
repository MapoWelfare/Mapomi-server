package net.mapomi.mapomi.jwt;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;
    private boolean joined = true;

    public void setJoined(boolean joined) {
        this.joined = joined;
    }
}
