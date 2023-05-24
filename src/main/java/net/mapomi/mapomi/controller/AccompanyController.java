package net.mapomi.mapomi.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.ApiDocumentResponse;
import net.mapomi.mapomi.dto.request.PostBuildDto;
import net.mapomi.mapomi.dto.response.ShowForm;
import net.mapomi.mapomi.service.AccompanyService;
import org.json.simple.JSONObject;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequiredArgsConstructor
public class AccompanyController {

    private final AccompanyService accompanyService;

    @ApiDocumentResponse
    @ApiOperation(value = "동행 글 생성")
    @PostMapping(value = "/accompany/build", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public JSONObject makePost(@RequestBody PostBuildDto buildDto) {
        return accompanyService.build(buildDto);
    }


    @ApiDocumentResponse
    @ApiOperation(value = "글 수정")
    @PostMapping(value = "/accompany/{id}/edit")
    public JSONObject editPost(@PathVariable("id") Long postId, @RequestBody PostBuildDto editDto) {
        return accompanyService.edit(postId, editDto);
    }

    @ApiDocumentResponse
    @ApiOperation(value = "글 삭제")
    @PostMapping(value = "/accompany/{id}/delete")
    public JSONObject deletePost(@PathVariable("id") Long postId) {
        return accompanyService.delete(postId);
    }


    @PostMapping("/accompany/{id}")
    @ApiOperation(value = "글 상세 조회")
    public JSONObject showPost(@PathVariable Long id) {
        return accompanyService.showDetail(id);
    }


    @ApiOperation(value = "동행 요청 글 목록")
    @GetMapping("/accompanies")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "파라미터 형식으로 전달해주세요 (1..N) \nex) http://localhost:8080/accompanies?page=3&size=5", defaultValue = "1"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "3", defaultValue = "5"),
            @ApiImplicitParam(name = "search", dataType = "string", paramType = "query",
                    value = "String 값으로 주시고 최소 2글자 이상은 받아야 합니다. contain 메서드로 db에서 검색할 예정.")
    })
    public PageImpl<ShowForm> showPosts(@RequestParam(required=false, defaultValue="") String search, @ApiIgnore Pageable pageable) {
        return accompanyService.showPostList(search, pageable);
    }
}
