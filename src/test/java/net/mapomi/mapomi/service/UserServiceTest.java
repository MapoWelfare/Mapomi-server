package net.mapomi.mapomi.service;

import net.mapomi.mapomi.controller.UserController;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.request.JoinDto;
import net.mapomi.mapomi.dto.request.LoginDto;
import net.mapomi.mapomi.jwt.RefreshToken;
import net.mapomi.mapomi.jwt.RefreshTokenRepository;
import net.mapomi.mapomi.jwt.TokenDto;
import net.mapomi.mapomi.repository.UserRepository;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {
    @Autowired
    UserCommandService userCommandService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository tokenRepository;
    @Autowired
    private MockMvc mockMvc;


    private User disabled;
    private User abled;
    private User observer;
    private static final String id = "abc";
    private static final String pw = "abc";
    private static Long disabled_user_id = 0L;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userCommandService))
                .alwaysExpect(status().isOk())
                .build();

    }

    @Test
    @Order(1)
    @DisplayName("회원가입")
    @Transactional
    @Rollback(value = false)
    void join() {
        JoinDto dto = new JoinDto(id, pw, "송인서", "0105013334", "로롤", true, "경기도 안양", 20, "시각 장애");
        JoinDto dto2 = new JoinDto("def", "def", "유성욱", "0105013334", "나난", true, "경기도 안양", 20, "");
        JoinDto dto3 = new JoinDto("ghi", "ghi", "윤강현", "01043273481", "도동", true, "", 0, "");
        JSONObject obj = userCommandService.signup("disabled", dto);
        JSONObject obj2 = userCommandService.signup("abled", dto2);
        JSONObject obj3 = userCommandService.signup("observer", dto3);
        assertEquals(obj.get("success"), true);
        assertEquals(obj2.get("success"), true);
        assertEquals(obj3.get("success"), true);
    }

    @Test
    @Order(2)
    @DisplayName("회원 저장")
    @Rollback(value = false)
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
    @DisplayName("로그인, jwt 토큰")
    @Rollback(value = false)
    void login() {
        JSONObject login = userCommandService.login(new LoginDto(id, pw));
        disabled_user_id = disabled.getId();
        List<RefreshToken> refreshTokens = tokenRepository.findByKey(disabled_user_id);
        RefreshToken refreshToken = refreshTokens.get(refreshTokens.size()-1); //마지막꺼가 가장 최신반영된 토큰
        TokenDto tokens = (TokenDto) login.get("data");
        Assertions.assertEquals(refreshToken.getToken(), tokens.getRefreshToken());
    }

    @Test
    @Order(4)
    @DisplayName("id, nickname 중복체크")
    void check() throws Exception {
        RequestBuilder idBuilder = MockMvcRequestBuilders.post("/check/id")
                .content("{ \"id\" : \"def\"}")
                .contentType(MediaType.APPLICATION_JSON);
        RequestBuilder nickBuilder = MockMvcRequestBuilders.post("/check/nickname")
                .content("{ \"nickname\" : \"dd\"}")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(idBuilder)
                .andDo(print())
                .andExpect(content().json("{ \"success\" : false, \"message\" :  \"이미 존재하는 id입니다.\"}"));
        this.mockMvc.perform(nickBuilder)
                .andDo(print())
                .andExpect(content().json("{ \"success\" : true}"));;
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