package net.mapomi.mapomi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.util.Map;

@Getter
@Slf4j
@ToString
public class OAuthAttributes {
    private String email;
    private String picture="";
    private String origin;

    @Builder
    public OAuthAttributes(String email, String picture, String origin) {
        this.email = email;
        this.picture = picture;
        this.origin = origin;
    }

    public static OAuthAttributes of(JSONObject attributes) {
        return ofKakao(attributes);
    }


    private static OAuthAttributes ofKakao(JSONObject attributes) {
        log.warn(attributes.toJSONString());
        JSONObject kakao_account = (JSONObject) attributes.get("kakao_account");
        JSONObject profile = (JSONObject) kakao_account.get("profile");
        return OAuthAttributes.builder()
//                .name((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .picture("")
//                .picture((String) profile.get("profile_image_url"))
                .origin("Kakao")
                .build();
    }
}
