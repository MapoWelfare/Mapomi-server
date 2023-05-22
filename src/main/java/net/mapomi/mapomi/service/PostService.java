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
                .schedule(buildDto.getSchedule())
                .author(user.getNickName())
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

//
//    @Transactional
//    public JSONObject showDetailForGuest(Long id){   // 비회원 글 보기
//        Product product = productRepository.findByIdFetchImages(id).orElseThrow(PostNotFoundException::new);
//        DetailProductForm detailForm = makeProductDetailForm(product, product.getImages());
//        if(!product.getUser().isDelete()){
//            User postUser = product.getUser();
//            detailForm.setUserInfo(postUser.getId(),postUser.getNickName(),postUser.getPicture(),postUser.getUniv(),postUser.isCert_uni(),postUser.isCert_author(), postUser.getFollowerNum());
//        }
//        else{
//            detailForm.setUserInfo(null, "탈퇴한 회원", null, "??", false, false, "0");
//        }
//        detailForm.setUserAction(false,false,false);
//        product.addViews();
//        return PropertyUtil.response(detailForm);
//    }
//
//
//    @Transactional
//    public PageImpl<ShowForm> productListForUser(String keyword, List<String> categories, String align, boolean complete, Pageable pageable){
//        User user  = userRepository.findByIdFetchHistoryAndLikesList(userUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
//        if(!keyword.isEmpty())
//            saveSearchHistory(keyword, user);
//        Page<Product> productList = QDSLRepository.findAllByCompleteAndCategoriesAligned(complete, keyword, categories, align, pageable);
//
//        List<ShowForm> showList = makeDetailHomeShowForms(user.getProductLikesList(), productList.getContent());
//        return new PageImpl<>(showList, pageable, productList.getTotalElements());
//    }




}
