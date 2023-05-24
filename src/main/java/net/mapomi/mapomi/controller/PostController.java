package net.mapomi.mapomi.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.ApiDocumentResponse;
import net.mapomi.mapomi.common.UserUtils;
import net.mapomi.mapomi.dto.request.MatchRequestDto;
import net.mapomi.mapomi.dto.request.PostBuildDto;
import net.mapomi.mapomi.dto.response.ShowForm;
import net.mapomi.mapomi.service.PostService;
import org.json.simple.JSONObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ApiDocumentResponse
    @ApiOperation(value = "동행 글 생성")
    @PostMapping(value = "/post/build", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public JSONObject makePost(@RequestBody PostBuildDto buildDto) {
        return postService.build(buildDto);
    }


    @ApiDocumentResponse
    @ApiOperation(value = "글 수정")
    @PostMapping(value = "/post/{id}/edit")
    public JSONObject editPost(@PathVariable("id") Long productId, @RequestBody PostBuildDto editDto) {
        return postService.edit(productId, editDto);
    }

    @ApiDocumentResponse
    @ApiOperation(value = "작품 삭제")
    @PostMapping(value = "/post/{id}/delete")
    public JSONObject deletePost(@PathVariable("id") Long productId) {
        return postService.delete(productId);
    }


    @PostMapping("/post/{id}")
    @ApiOperation(value = "작품 상세 조회")
    public JSONObject showProducts(@PathVariable Long id) {
        return postService.showDetail(id);
    }


    @ApiOperation(value = "동행 요청 글 목록")
    @GetMapping("/posts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "파라미터 형식으로 전달해주세요 (1..N) \nex) http://localhost:8080/posts?page=3&size=5", defaultValue = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "3", defaultValue = "5"),
            @ApiImplicitParam(name = "search", dataType = "string", paramType = "query",
                    value = "String 값으로 주시고 최소 2글자 이상은 받아야 합니다. contain 메서드로 db에서 검색할 예정.")
    })
    public PageImpl<ShowForm> showMarketProduct(@RequestParam(required=false, defaultValue="") String search, @ApiIgnore Pageable pageable) {
        return postService.showPostList(search, pageable);
    }

    @ApiDocumentResponse
    @ApiOperation(value = "함께하기")
    @PostMapping(value = "/posts/{id}/match-request")
    public JSONObject matchRequest(@PathVariable Long id){
        return postService.matchRequest(id);
    }

    @ApiDocumentResponse
    @ApiOperation(value = "함께하기 목록 보기")
    @GetMapping(value = "/posts/{id}/match-request")
    public JSONObject getMatchRequest(@PathVariable Long id){
        return postService.getMatchRequest(id);
    }

    @ApiDocumentResponse
    @ApiOperation(value = "수락하기")
    @PostMapping(value = "/posts/{id}/match")
    public JSONObject match(@PathVariable("id") Long postId,@RequestBody MatchRequestDto matchRequestDto){
        return postService.match(postId,matchRequestDto);
    }



}
