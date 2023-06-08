package net.mapomi.mapomi.jwt;

import lombok.*;
import net.mapomi.mapomi.domain.Role;

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

    private Role role;

    private boolean joined = true;

    public void setJoined(boolean joined) {
        this.joined = joined;
    }
}
