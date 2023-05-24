package net.mapomi.mapomi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mapomi.mapomi.common.PropertyUtil;
import net.mapomi.mapomi.common.UserUtils;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.response.ProfileForm;
import net.mapomi.mapomi.repository.UserRepository;
import org.json.simple.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    public JSONObject getMyProfile(){
        User loginUser = userUtils.getCurrentUser();
        ProfileForm profileForm = ProfileForm.builder()
                .nickName(loginUser.getNickName())
                .picture(loginUser.getPicture())
                .role(loginUser.getRole())
                .build();
        return PropertyUtil.response(profileForm);
    }


}
