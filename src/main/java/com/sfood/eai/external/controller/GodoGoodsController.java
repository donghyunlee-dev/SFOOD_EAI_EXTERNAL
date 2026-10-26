package com.sfood.eai.external.controller;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.ResponseMessage;
import com.sfood.eai.external.dto.godo.GodoGoodsReq;
import com.sfood.eai.external.service.GodoGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 고도몰 API Controller
 * 설명 : 고도몰 OPEN API를 활용하여 품목 정보를 전송한다.
 * 연동몰 유형 : JohnCook, Sfood, Medisola
 * API 타입 :
 * 1. 상품등록 (goods_insert)
 * 2. 상품수정 (goods_update)
 * 3. 상품수정 (goods_update)
 * 호출 방식 :
 * Internal API 호출
 */
@Slf4j
@RequestMapping("/godo")
@RestController
public class GodoGoodsController {

    private final GodoGoodsService godoGoodsService;

    public GodoGoodsController(GodoGoodsService godoGoodsService) {
        this.godoGoodsService = godoGoodsService;
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "고도몰 상품 등록", description = "고도몰 상품 정보를 등록한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/goods-add")
    public ResponseEntity<?> addNewGoods(HttpServletRequest request, @RequestBody GodoGoodsReq goodsReq) throws Exception {
        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();
        CommonDataRes res;

        res = godoGoodsService.sendNewGoods(goodsReq.addParams(request));

        log.info(goodsReq.toString());

        message.setData(res);

        return new ResponseEntity<>(message, message.getStatus());
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "고도몰 상품 수정", description = "고도몰 상품 정보를 수정한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/goods-modify")
    public ResponseEntity<?> modifyGoods(HttpServletRequest request, @RequestBody GodoGoodsReq goodsReq) throws Exception {
        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();
        CommonDataRes res;

        res = godoGoodsService.sendGoodsUpdateInfo(goodsReq.addParams(request));

        log.info(goodsReq.toString());

        message.setData(res);

        return new ResponseEntity<>(message, message.getStatus());
    }
}
