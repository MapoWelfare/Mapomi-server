package net.mapomi.mapomi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.ApiDocumentResponse;
import net.mapomi.mapomi.dto.request.JoinDto;
import net.mapomi.mapomi.dto.request.LoginDto;
import net.mapomi.mapomi.dto.request.NickNameDto;
import net.mapomi.mapomi.service.UserCommandService;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "유저-명령")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;


    @ApiDocumentResponse
    @ApiOperation(value = "회원가입", notes = "쿼리 파라미터에 /join?type=abled 이런식으로 주기. disabled가 디폴트고, abled, observer는 쿼리파라미터로 줘야함. + JoinDto")
    @PostMapping("/join")
    public JSONObject join(@RequestParam(name = "type", defaultValue = "disabled") String type,  @Valid @RequestBody JoinDto dto) {
        return userCommandService.signup(type, dto);
    }

    @ApiDocumentResponse
    @ApiOperation(value = "닉네임 체크", notes = "이미 존재하는 닉네임일때 -> success : false, message : 이미 존재하는 닉네임입니다.")
    @PostMapping("/check/nickname")
    public JSONObject checkNickName(@RequestBody NickNameDto nickNameDto) {
        return userCommandService.checkNickName(nickNameDto.getNickName());
    }

    @ApiDocumentResponse
    @ApiOperation(value = "이메일 중복체크", notes = "이메일 중복 시 에는 하단과 같이 반환됩니다 false면 가입 불가능한 이메일인거로 생각하시면 됩니다 \n{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"이미 가입된 이메일입니다.\"\n" +
            "}", hidden = true)
    @PostMapping("/check/id")
    public JSONObject checkEmail(@RequestBody LoginDto dto) {
        return userCommandService.checkId(dto.getId());
    }

    @ApiOperation(value = "로그인테스트", notes = "비밀번호 오류, 등등은 후에 추가할게")
    @PostMapping("/login")
    public JSONObject login(@RequestBody LoginDto dto) {
        return userCommandService.login(dto);
    }

//    @ApiOperation(value = "토큰 만료시 재발급 토큰들은 필요없고 헤더에 Authorization만 있으면 됩니다.")
//    @PostMapping("/reissue")
//    public TokenDto reissue() {
//        return securityService.reissue();
//    }
//
//    @ApiDocumentResponse
//    @ApiOperation(value = "유저 정보변경", notes = "이름, 한줄 소개")
//    @PostMapping(value = "/users/edit")
//    public JSONObject updateUser(@Valid @RequestBody UpdateUserDto dto) {
//        return userCommandService.updateUser(dto);
//    }
//
//    @ApiDocumentResponse
//    @ApiOperation(value = "프로필 이미지 변경")
//    @PostMapping(value = "/users/edit/image")
//    public JSONObject updateUserImage(@RequestPart MultipartFile multipartFile) {
//        return userCommandService.updateUserImage(multipartFile);
//    }
}
