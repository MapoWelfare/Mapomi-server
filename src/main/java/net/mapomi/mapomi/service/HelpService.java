package net.mapomi.mapomi.service;

import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.PropertyUtil;
import net.mapomi.mapomi.common.UserUtils;
import net.mapomi.mapomi.common.error.PostNotFoundException;
import net.mapomi.mapomi.common.error.UserNotFoundException;
import net.mapomi.mapomi.domain.Help;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.domain.user.Disabled;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.request.PostBuildDto;
import net.mapomi.mapomi.dto.response.DetailPostForm;
import net.mapomi.mapomi.dto.response.ShowForm;
import net.mapomi.mapomi.repository.HelpRepository;
import net.mapomi.mapomi.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelpService {
    private final HelpRepository helpRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    @Transactional(rollbackFor = {Exception.class})
    public JSONObject build(PostBuildDto buildDto){   // 글 생성
        Disabled user = userRepository.findDisabledById(userUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        Help help = Help.builder()
                .title(buildDto.getTitle())
                .content(buildDto.getContent())
                .author(user.getNickName())
                .schedule(buildDto.getSchedule())
                .duration(buildDto.getDuration())
                .departure(buildDto.getDeparture())
                .destination(buildDto.getDestination())
                .type(user.getType())
                .build();
        help.setDisabled(user);
        Long productId = helpRepository.save(help).getId();
        return PropertyUtil.response(productId);
    }


    @Transactional(rollbackFor = {Exception.class})
    public JSONObject edit(Long postId, PostBuildDto editDto){
        User user = userUtils.getCurrentUser();
        Help help = helpRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if(!user.getId().equals(help.getDisabled().getId()) && user.getRole() != Role.DISABLED)
            return PropertyUtil.responseMessage("글 작성자가 아닙니다.");

        help.editPost(editDto);
        helpRepository.save(help);
        return PropertyUtil.response(true);
    }

    @Transactional(rollbackFor = {Exception.class})
    public JSONObject delete(Long postId){
        Disabled user = userRepository.findDisabledById(userUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        Help help = helpRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if(!user.getId().equals(help.getDisabled().getId()) && user.getRole() != Role.DISABLED)
            return PropertyUtil.responseMessage("글 작성자가 아닙니다.");
        helpRepository.delete(help);
        return PropertyUtil.response(true);
    }


    @Transactional
    public JSONObject showDetail(Long id){
        Help help = helpRepository.findById(id).orElseThrow(PostNotFoundException::new);
        DetailPostForm detailForm = makePostDetail(help);
        detailForm.setUserInfo(help.getDisabled());
        help.addViews();
        return PropertyUtil.response(detailForm);
    }

    private DetailPostForm makePostDetail(Help help) {
        return DetailPostForm.builder()
                .postId(help.getId())
                .title(help.getTitle())
                .content(help.getContent())
                .views(help.getViews())
                .schedule(help.getSchedule())
                .duration(help.getDuration())
                .departure(help.getDeparture())
                .destination(help.getDestination())
                .complete(help.isComplete())
                .build();
    }

    @Transactional(readOnly = true)
    public PageImpl<ShowForm> showPostList(String keyword, Pageable pageable){
        Page<Help> posts = helpRepository.findSearchedPageable(keyword, pageable);
        List<ShowForm> showList = posts.getContent()
                .stream()
                .map(post -> new ShowForm(post.getId(), post.getTitle(), post.getCreatedDate().toString(), post.getSchedule(), post.getDeparture(), post.getDestination(), post.getDisabled().getPicture(), post.isComplete()))
                .collect(Collectors.toList());
        return new PageImpl<>(showList, pageable, showList.size());
    }
}
