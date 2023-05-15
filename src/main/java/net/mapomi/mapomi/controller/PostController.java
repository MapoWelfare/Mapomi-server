package net.mapomi.mapomi.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.ApiDocumentResponse;
import net.mapomi.mapomi.dto.request.PostBuildDto;
import net.mapomi.mapomi.service.PostService;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ApiDocumentResponse
    @ApiOperation(value = "작품 판매 글 생성",notes = "{\"success\":true, \"id\":52}\n해당 글의 id를 전해드리니 이 /products/{id}/image 에 넘겨주세요\n" +
            "category = painting,orient,sculpture,print,craft,other")
    @PostMapping(value = "/products/build", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public JSONObject makePost(@RequestBody PostBuildDto buildDto) {
        return postService.build(buildDto);
    }


    @ApiDocumentResponse
    @ApiOperation(value = "작품 수정")
    @PostMapping(value = "/products/{id}/edit")
    public JSONObject editPost(@PathVariable("id") Long productId, @RequestBody PostBuildDto editDto) {
        return postService.edit(productId, editDto);
    }

    @ApiDocumentResponse
    @ApiOperation(value = "작품 삭제")
    @PostMapping(value = "/products/{id}/delete")
    public JSONObject deletePost(@PathVariable("id") Long productId) {
        return postService.delete(productId);
    }


//    @PostMapping("/products/{id}")
//    @ApiOperation(value = "작품 상세 조회")
//    public JSONObject showProducts(@PathVariable Long id) {
//        try{
//            Long currentUserId = UserUtils.getContextHolderId();
//            return productService.showDetailForUser(currentUserId, id);
//        }
//        catch (UserNotFoundException | UserNotLoginException e){
//            return productService.showDetailForGuest(id); /** 비회원용 **/
//        }
//    }


//    @ApiOperation(value = "마켓 작품")
//    @PostMapping("/products")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
//                    value = "파라미터 형식으로 전달해주세요 (0..N) \nex) http://localhost:8080/api/products?page=3&size=5&stacks=orient,western\nhttp://localhost:8080/api/products?page=0&size=5&stacks=orient&stacks=western&sale=true  둘 다 가능합니다", defaultValue = "0"),
//            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
//                    value = "3", defaultValue = "5"),
//            @ApiImplicitParam(name = "align", dataType = "string", paramType = "query",
//                    value = "정렬 기준\nrecommend - 신작추천순\n" +
//                            "popular - 인기순(좋아요)\n" +
//                            "recent - 최신순\n" +
//                            "low - 낮은가격순\n" +
//                            "high - 높은가격순", defaultValue = "recommend"),
//            @ApiImplicitParam(name = "categories", dataType = "string", paramType = "query",
//                    value = "categories(최대 3개)\n" +
//                            "painting - 회화일반\n" +
//                            "orient - 동양화\n" +
//                            "sculpture - 조소\n" +
//                            "print - 판화\n" +
//                            "craft - 공예\n" +
//                            "other - 기타", defaultValue = ""),
//            @ApiImplicitParam(name = "sale", dataType = "boolean", paramType = "query",
//                    value = "sale = false -> 모든 작품(디폴트)" +
//                            "sale = true -> 판매중인 작품만(거래중도 포함, 거래완료는 X)" +
//                            "생략시 판매중인 작품만 보기 버튼이 체크 되지 않은 상태라고 생각하시면 됩니다  어떤 파라미터명을써도 딱 맞아떨어지는게 없어서 sale로 갈게요", defaultValue = "false"),
//            @ApiImplicitParam(name = "search", dataType = "string", paramType = "query",
//                    value = "String 값으로 주시고 최소 2글자 이상은 받아야 합니다. contain 메서드로 db에서 검색할 예정.")
//    })
//    public PageImpl<ShowForm> showMarketProduct(@RequestParam(required=false, defaultValue="") String search, @RequestParam(required=false, defaultValue="") List<String> categories, @RequestParam(required=false, defaultValue="recommend") String align, @RequestParam(required=false, defaultValue="false") Boolean sale, @ApiIgnore Pageable pageable) {
//        try{
//            UserUtils.getContextHolderId();
//            return productService.productListForUser(search, categories, align, sale, pageable);
//        }
//        catch (UserNotFoundException | UserNotLoginException e){
//            return productService.productListForGuest(search, categories, align, sale, pageable);
//        }
//    }
}
