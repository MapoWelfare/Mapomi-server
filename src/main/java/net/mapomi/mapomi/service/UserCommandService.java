package net.mapomi.mapomi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mapomi.mapomi.common.PropertyUtil;
import net.mapomi.mapomi.common.UserUtils;
import net.mapomi.mapomi.common.error.UserNotFoundException;
import net.mapomi.mapomi.domain.user.Abled;
import net.mapomi.mapomi.domain.user.Disabled;
import net.mapomi.mapomi.domain.user.Observer;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.OAuthAttributes;
import net.mapomi.mapomi.dto.request.DetailJoinDto;
import net.mapomi.mapomi.dto.request.JoinDto;
import net.mapomi.mapomi.dto.request.LoginDto;
import net.mapomi.mapomi.image.S3Service;
import net.mapomi.mapomi.jwt.*;
import net.mapomi.mapomi.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtProvider;
    private final UserUtils userUtils;
    private final S3Service imageService;
    private final OAuthService oAuthService;


    public User saveTempUser(User user){
        return userRepository.save(user);
    }

    public JSONObject login(String nickName) {
        User user = userRepository.findByNickName(nickName).orElseThrow(UserNotFoundException::new);
        TokenDto tokenDto = jwtProvider.createToken(user.getId(), user.getId(), user.getRole());
        //리프레시 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return PropertyUtil.response(tokenDto);
    }

    public TokenDto oAuthLogin(String email) { //소셜로그인
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        TokenDto tokenDto = jwtProvider.createToken(user.getId(), user.getId(), user.getRole());

        //리프레시 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    public JSONObject signup(String type, JoinDto joinInfo) throws NullPointerException, IOException, ParseException { //아이디 비번 이름 생일 통신사 번호 저장
        JSONObject OauthInfo = oAuthService.getOauthInfo(joinInfo.getAccessToken());
        OAuthAttributes OauthUser = OAuthAttributes.of(OauthInfo);
        if(type.equals("disabled")){
            Disabled user = new Disabled(joinInfo,OauthUser.getEmail());
            userRepository.save(user);
        }
        else if(type.equals("abled")){
            Abled user = new Abled(joinInfo,OauthUser.getEmail());
            userRepository.save(user);
        }
        else{
            Observer user = new Observer(joinInfo,OauthUser.getEmail());
            userRepository.save(user);
        }
        return PropertyUtil.response(true);
    }

    @Transactional(readOnly = true)
    public JSONObject checkNickName(String nickName) {
        Optional<User> user = userRepository.findByNickName(nickName);
        if(user.isPresent())
            return PropertyUtil.responseMessage("이미 존재하는 닉네임입니다.");
        return PropertyUtil.response(true);
    }


    public JSONObject reissue(TokenRequestDto tokenRequestDto) {
        User user = userUtils.getCurrentUser();

        // 만료된 refresh token 에러
        if (!jwtProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new NoSuchElementException("리프레쉬 토큰 만료");
        }

        List<RefreshToken> refreshTokens = refreshTokenRepository.findByKey(user.getId());
        RefreshToken refreshToken = refreshTokens.get(refreshTokens.size()-1); //마지막꺼가 가장 최신반영된 토큰
        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new NoSuchElementException("리프레쉬 토큰 불일치");

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto newCreatedToken = jwtProvider.createToken(user.getId(), user.getId(), user.getRole());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        refreshTokenRepository.save(updateRefreshToken);

        return PropertyUtil.response(newCreatedToken);
    }

    public JSONObject updateUserImage(MultipartFile multipartFile){
        User loginUser = userUtils.getCurrentUser();
        String url = imageService.uploadImage(multipartFile);
        loginUser.setPicture(url);
        userRepository.save(loginUser);
        try{


        }
        catch (Exception e){
            return PropertyUtil.responseMessage("이미지 저장 실패");
        }
        JSONObject obj = new JSONObject();
        obj.put("picture", loginUser.getPicture());
        return PropertyUtil.response(obj);
    }
}
