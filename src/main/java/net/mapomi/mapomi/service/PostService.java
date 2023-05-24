package net.mapomi.mapomi.service;

import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.PropertyUtil;
import net.mapomi.mapomi.common.UserUtils;
import net.mapomi.mapomi.common.error.PostNotFoundException;
import net.mapomi.mapomi.common.error.UserNotFoundException;
import net.mapomi.mapomi.domain.Enum.MatchRequestStatus;
import net.mapomi.mapomi.domain.MatchRequest;
import net.mapomi.mapomi.domain.Post;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.domain.user.Abled;
import net.mapomi.mapomi.domain.user.Disabled;
import net.mapomi.mapomi.domain.user.User;
import net.mapomi.mapomi.dto.request.MatchRequestDto;
import net.mapomi.mapomi.dto.request.PostBuildDto;
import net.mapomi.mapomi.dto.response.DetailPostForm;
import net.mapomi.mapomi.dto.response.MatchRequestForm;
import net.mapomi.mapomi.dto.response.ShowForm;
import net.mapomi.mapomi.repository.MatchRequestRepository;
import net.mapomi.mapomi.repository.PostRepository;
import net.mapomi.mapomi.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MatchRequestRepository matchRequestRepository;

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

    @Transactional(readOnly = true)
    public PageImpl<ShowForm> showPostList(String keyword, Pageable pageable){
        Page<Post> posts = postRepository.findSearchedPageable(keyword, pageable);
        List<ShowForm> showList = posts.getContent()
                .stream()
                .map(post -> new ShowForm(post.getId(), post.getTitle(), post.getCreatedDate().toString(), post.getSchedule(), post.getDeparture(), post.getDestination(), post.getDisabled().getPicture(), post.isComplete()))
                .collect(Collectors.toList());
        return new PageImpl<>(showList, pageable, showList.size());
    }


    @Transactional
    public JSONObject matchRequest(Long postId){
        Long userId = userUtils.getCurrentUserId();
        Abled abled = userRepository.findAbledByIdFetchMatchRequest(userId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Set<MatchRequest> matchRequests = abled.getMatchRequests();
        for(MatchRequest matchRequest : matchRequests){
            if(matchRequest.getPost().getId().equals(postId)){ //이미 신청한 것
                return PropertyUtil.responseMessage("이미 함께하기를 신청한 글입니다.");
            }
        }
        MatchRequest matchRequest = makeNewMatchRequest(abled, post, matchRequests);
        return PropertyUtil.response(matchRequest.getId());
    }

    private MatchRequest makeNewMatchRequest(Abled abled, Post post, Set<MatchRequest> matchRequests) {
        MatchRequest matchRequest = MatchRequest.builder()
                .matchRequestStatus(MatchRequestStatus.YET)
                .post(post)
                .abled(abled)
                .build();
        matchRequestRepository.save(matchRequest);
        return matchRequest;
    }
    @Transactional
    public JSONObject getMatchRequest(Long postId){
        Long userId = userUtils.getCurrentUserId();
        Post post = postRepository.findByIdFetchMatchRequests(postId).orElseThrow(PostNotFoundException::new);
        if(!post.getDisabled().getId().equals(userId)) return PropertyUtil.responseMessage("글 작성자가 아닙니다.");
        List<MatchRequest> matchRequests = matchRequestRepository.getMatchRequestByPostIdFetchAbled(postId);
        List<MatchRequestForm> matchRequestForms = matchRequests.stream()
                .map(matchRequest-> MatchRequestForm.builder()
                        .matchRequestId(matchRequest.getId())
                        .nickName(matchRequest.getAbled().getNickName())
                        .picture(matchRequest.getAbled().getPicture())
                        .build())
                .collect(Collectors.toList());
        return PropertyUtil.response(matchRequestForms);
    }
    @Transactional
    public JSONObject match(Long postId, MatchRequestDto matchRequestDto){
        Post post = postRepository.findByIdFetchMatchRequests(postId).orElseThrow(PostNotFoundException::new);
        for(MatchRequest matchRequest : post.getMatchRequests()){
            if(matchRequest.getId().equals(matchRequestDto.getMatchRequestId())) {
                matchRequest.setStatus(MatchRequestStatus.MATCH);
                post.setAbled(matchRequest.getAbled());
                continue;
            }
            matchRequest.setStatus(MatchRequestStatus.REJECT);
        }
        return PropertyUtil.response(true);
    }
}
