package net.mapomi.mapomi.service;

import net.mapomi.mapomi.common.error.PostNotFoundException;
import net.mapomi.mapomi.domain.Accompany;
import net.mapomi.mapomi.dto.request.JoinDto;
import net.mapomi.mapomi.dto.request.PostBuildDto;
import net.mapomi.mapomi.jwt.JwtTokenProvider;
import net.mapomi.mapomi.jwt.TokenDto;
import net.mapomi.mapomi.repository.AccompanyRepository;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccompanyServiceTest {
    @Autowired
    UserCommandService userCommandService;
    @Autowired
    AccompanyService accompanyService;
    @Autowired
    AccompanyRepository accompanyRepository;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;

    private static Long buildPostId;
    private static Long deletePostId;

//    @BeforeAll
//    @Transactional
//    void before(){
//        JoinDto dto = new JoinDto("abc", "01048424426","");
//        userCommandService.signup("disabled", dto);
//        TokenDto token = (TokenDto) userCommandService.login("abc").get("data");
//        Authentication authentication = tokenProvider.getAuthentication(token.getAccessToken());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }



    @Test
    @Order(1)
    @DisplayName("글 생성")
    @Transactional
    @Rollback(value = false)
    void build() {
        userCommandService.login("abc");
        PostBuildDto buildDto1 = new PostBuildDto("등록 및 수정 테스트","내용1","2023-06-04T17:50:00","","","");
        PostBuildDto buildDto2 = new PostBuildDto("삭제 테스트","내용2","2023-06-04T17:50:00","","","");
        JSONObject response1 = accompanyService.build(buildDto1);
        JSONObject response2 = accompanyService.build(buildDto2);
        Long id1 = (Long) response1.get("id");
        Long id2 = (Long) response2.get("id");
        buildPostId = id1;
        deletePostId = id2;
        Optional<Accompany> post1 = accompanyRepository.findById(buildPostId);
        Optional<Accompany> post2 = accompanyRepository.findById(deletePostId);
        assertTrue(post1.isPresent() && post2.isPresent());
    }

    @Test
    @Order(2)
    @DisplayName("글 삭제")
    @Transactional
    @Rollback(value = false)
    void delete() {
        accompanyService.delete(deletePostId);
        assertTrue((accompanyRepository.findById(deletePostId).isEmpty()));
        assertNotEquals(2, accompanyRepository.count());
    }

    @Test
    @Order(3)
    @DisplayName("글 수정")
    @Transactional
    @Rollback(value = false)
    void edit() {
        accompanyService.edit(buildPostId,new PostBuildDto("수정완료","수정내용","2023-06-11T17:50:00","","",""));
        Accompany accompany = accompanyRepository.findById(buildPostId).orElseThrow(PostNotFoundException::new);
        assertTrue(accompany.getTitle().equals("수정완료"));
        assertTrue(accompany.getContent().equals("수정내용"));
    }
}