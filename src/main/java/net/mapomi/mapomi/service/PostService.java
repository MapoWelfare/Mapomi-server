package net.mapomi.mapomi.service;

import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.PropertyUtil;
import net.mapomi.mapomi.common.UserUtils;
import net.mapomi.mapomi.common.error.PostNotFoundException;
import net.mapomi.mapomi.common.error.UserNotFoundException;
import net.mapomi.mapomi.domain.Post;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.domain.user.Disabled;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.request.PostBuildDto;
import net.mapomi.mapomi.dto.response.DetailPostForm;
import net.mapomi.mapomi.repository.PostRepository;
import net.mapomi.mapomi.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    @Transactional(rollbackFor = {Exception.class})
    public JSONObject build(PostBuildDto buildDto){   // 글 생성
        Disabled user = userRepository.findDisabledById(userUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        Post post = Post.builder()
                .title(buildDto.getTitle())
                .content(buildDto.getContent())
                .author(user.getNickName())
                .schedule(buildDto.getSchedule())
                .duration(buildDto.getDuration())
                .departure(buildDto.getDeparture())
                .destination(buildDto.getDestination())
                .type(user.getType())
                .build();
        post.setDisabled(user);
        Long productId = postRepository.save(post).getId();
        return PropertyUtil.response(productId);
    }


    @Transactional(rollbackFor = {Exception.class})
    public JSONObject edit(Long productId, PostBuildDto editDto){
        User user = userUtils.getCurrentUser();
        Post post = postRepository.findById(productId).orElseThrow(PostNotFoundException::new);
        if(!user.getId().equals(post.getDisabled().getId()) && user.getRole() != Role.DISABLED)
            return PropertyUtil.responseMessage("글 작성자가 아닙니다.");

        post.editPost(editDto);
        postRepository.save(post);
        return PropertyUtil.response(true);
    }

    @Transactional(rollbackFor = {Exception.class})
    public JSONObject delete(Long productId){
        Disabled user = userRepository.findDisabledById(userUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(productId).orElseThrow(PostNotFoundException::new);
        if(!user.getId().equals(post.getDisabled().getId()) && user.getRole() != Role.DISABLED)
            return PropertyUtil.responseMessage("글 작성자가 아닙니다.");
        postRepository.delete(post);
        return PropertyUtil.response(true);
    }


    @Transactional
    public JSONObject showDetail(Long id){
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        DetailPostForm detailForm = makePostDetail(post);
        detailForm.setUserInfo(post.getDisabled());
        post.addViews();
        return PropertyUtil.response(detailForm);
    }

    private DetailPostForm makePostDetail(Post post) {
        return DetailPostForm.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .schedule(post.getSchedule())
                .duration(post.getDuration())
                .departure(post.getDeparture())
                .destination(post.getDestination())
                .complete(post.isComplete())
                .build();
    }
}
