package net.mapomi.mapomi.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.PropertyUtil;
import net.mapomi.mapomi.common.error.UserNotFoundException;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.OAuthAttributes;
import net.mapomi.mapomi.dto.request.OauthDto;
import net.mapomi.mapomi.jwt.TokenDto;
import net.mapomi.mapomi.service.OAuthService;
import net.mapomi.mapomi.service.UserCommandService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;
    private final UserCommandService userService;
    private final String localURL = "http://localhost:8080";
    private final String productURL = "http://3.35.250.204:8080";
    private String KAKAO_ID = "ad91fa142c08bdf6f047dfb348f53b10";
    @ApiOperation(value = "액세스토큰 body에 넣어주세요.", notes ="data 안에는 공통적으로 토큰과 회원가입여부(joined)가 있습니다., \n" +
            "joined = true -> 홈으로 보내면되고, " +
            "joined = false 면 /join api 처리할 수 있게 회원가입 창으로 보내주시면 될 것 같아여")
    @PostMapping(value = "/oauth/get")
    public JSONObject getOauthToken(@org.springframework.web.bind.annotation.RequestBody OauthDto tokenDto) throws Exception {
        JSONObject OauthInfo = oAuthService.getOauthInfo(tokenDto);
        OAuthAttributes OauthUser = OAuthAttributes.of(OauthInfo);
        if(OauthUser.getEmail() == null || OauthUser.getEmail().isBlank())
            return PropertyUtil.responseMessage("회원가입 불가능(소셜로그인 실패)");
        TokenDto jwtToken;
        try{
            jwtToken = userService.oAuthLogin(OauthUser.getEmail());
        }
        catch(UserNotFoundException e){
            User savedUser = userService.saveTempUser(new User(OauthUser.getEmail(), OauthUser.getPicture()));
            jwtToken = userService.oAuthLogin(savedUser.getEmail());
        }
        return PropertyUtil.response(jwtToken);
    }

    @ApiOperation(value = "스프링용 카카오로그인 실행(인가코드)",notes = "로컬환경 : https://kauth.kakao.com/oauth/authorize?client_id=ad91fa142c08bdf6f047dfb348f53b10&redirect_uri=http://localhost:8080/login/oauth2/code/kakao&response_type=code\n" +
            "배포환경 : https://kauth.kakao.com/oauth/authorize?client_id=ad91fa142c08bdf6f047dfb348f53b10&redirect_uri=http://3.35.250.204:8080/login/oauth2/code/kakao&response_type=code")
    @GetMapping("/test1")
    public String kakaoLogin() {
        return "로컬환경 : https://kauth.kakao.com/oauth/authorize?client_id="+KAKAO_ID +
                "&redirect_uri="+localURL+"/login/oauth2/code/kakao&response_type=code" +"\n"+
                "배포환경 : https://kauth.kakao.com/oauth/authorize?client_id="+KAKAO_ID +
                "&redirect_uri="+productURL+"/login/oauth2/code/kakao&response_type=code";
    }

    @ApiOperation(value = "스프링용 카카오 액세스토큰 추출로직", notes = "웹, 안드, ios는 이 로직말고 /oauth/get으로 바로 액세스 토큰 전달해주세요")
    @GetMapping(value = "/login/oauth2/code/kakao")
    public JSONObject oauthKakao(@RequestParam(value = "code", required = false) String code) throws Exception {
        System.out.println("인가코드 = {"+code+"}");
        return oAuthService.getKakaoAccessToken(productURL+"/login/oauth2/code/kakao", code);
    }
}
