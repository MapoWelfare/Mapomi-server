package net.mapomi.mapomi.service;

import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.request.JoinDto;
import net.mapomi.mapomi.dto.request.LoginDto;
import net.mapomi.mapomi.jwt.RefreshToken;
import net.mapomi.mapomi.jwt.RefreshTokenRepository;
import net.mapomi.mapomi.jwt.TokenDto;
import net.mapomi.mapomi.repository.UserRepository;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository tokenRepository;
    private User disabled;
    private User abled;
    private User observer;
    private static final String id = "abc";
    private static final String pw = "abc";
    private static Long disabled_user_id = 0L;
    @Test
    @Order(1)
    @DisplayName("회원가입")
    @Transactional
    @Rollback(value = false)
    void join() {
        JoinDto dto = new JoinDto(id, pw, "송인서", "0105013334", "로롤", true, "경기도 안양", 20, "시각 장애");
        JoinDto dto2 = new JoinDto("def", "def", "유성욱", "0105013334", "나난", true, "경기도 안양", 20, "");
        JoinDto dto3 = new JoinDto("ghi", "ghi", "윤강현", "01043273481", "도동", true, "", 0, "");
        JSONObject obj = userService.signup("disabled", dto);
        JSONObject obj2 = userService.signup("abled", dto2);
        JSONObject obj3 = userService.signup("observer", dto3);
        assertEquals(obj.get("success"), true);
        assertEquals(obj2.get("success"), true);
        assertEquals(obj3.get("success"), true);
    }

    @Test
    @Order(2)
    @DisplayName("회원 저장 테스트")
    void certifyStart() {
        disabled = userRepository.findByAccountId(id).orElseThrow();
        abled = userRepository.findByAccountId("def").orElseThrow();
        observer = userRepository.findByAccountId("ghi").orElseThrow();
        assertEquals(disabled.getNickName(), "로롤");
        assertEquals(abled.getNickName(), "나난");
        assertEquals(observer.getNickName(), "도동");
    }

    @Test
    @Order(3)
    @DisplayName("로그인, jwt 토큰 테스트")
    void login() {
        JSONObject login = userService.login(new LoginDto(id, pw));
        disabled_user_id = disabled.getId();
        List<RefreshToken> refreshTokens = tokenRepository.findByKey(disabled_user_id);
        RefreshToken refreshToken = refreshTokens.get(refreshTokens.size()-1); //마지막꺼가 가장 최신반영된 토큰
        JSONObject data = (JSONObject) login.get("data");
        Assertions.assertEquals(refreshToken.getToken(), data.get("refreshToken"));
    }

//    @Test
//    @Order(4)
//    @DisplayName("메일인증 초입단계, 메일 전송 여부")
//    @Transactional
//    @Rollback(value = false)
//    void requestCertify() {
//        CertifyDto certifyInfo = new CertifyDto(API_KEY, univName, certifyEmail, true);
////        MailForm mailForm  = certService.checkErrorAndMakeForm(certifyInfo);
////        certService.sendMail(mailForm);
//        JSONObject response = certController.sendMail(certifyInfo);
//        assertEquals(true, response.get("success"));
//        String code = certService.getCode(certifyInfo.getEmail());
//        assertEquals(code.length(), 4);
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("메일의 인증번호 체크")
//    @Transactional
//    @Rollback(value = false)
//    void responseCode() {
//        String code = certService.getCode(certifyEmail);
//        CodeResponseDto codeDto = new CodeResponseDto(API_KEY, univName, certifyEmail, code);
//        JSONObject jsonObject = certService.receiveMail(codeDto);
//        assertEquals(jsonObject.get("success"), true);
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("인증 verified = true 여부")
//    void showVerifiedStatus() {
//        Cert cert = certService.getCert(certifyEmail);
//        assertEquals(true, cert.isCertified());
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("인증 이력 조회")
//    void showStatus() {
//        JSONObject certifiedStatus = certService.getStatus(new StatusDto(API_KEY, certifyEmail));
//        Assertions.assertThrows(CertNotFoundException.class, ()->certService.getStatus(new StatusDto(API_KEY, uncertifyEmail)));
//        assertEquals(true, certifiedStatus.get("success"));
//    }

//    @Test
//    @Order(8)
//    @DisplayName("인증된 사람 리스트 출력")
//    void showList() {
//        JSONObject certifiedList = certService.getCertifiedList(API_KEY);
//        System.out.println(certifiedList.toJSONString());
//        assertEquals(false,certifiedList.get("data").equals(Object.class));
//    }




}