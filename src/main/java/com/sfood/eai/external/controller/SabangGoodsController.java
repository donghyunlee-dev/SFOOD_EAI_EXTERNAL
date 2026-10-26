package com.sfood.eai.external.controller;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.ResponseMessage;
import com.sfood.eai.external.dto.sabang.SabangGoodsLiteReq;
import com.sfood.eai.external.dto.sabang.SabangGoodsReq;
import com.sfood.eai.external.service.SabangGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사방넷 API Controller
 * 설명 : 사방넷 OPEN API를 활용하여 상품 데이터를 전송한다.
 *       OCI Object Storage를 사용하여 XML 파일을 OCI에 업로드하고 고도몰에 파라미터로 제공하여 데이터를 전달하는 방식
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/sabang")
@RestController
public class SabangGoodsController {

    private final SabangGoodsService sabangGoodsService;

    @Tag(name = "사방넷 주문수집", description = "에스푸드 사방넷 주문수집")
    @Operation(summary = "사방넷 상품 등록 & 수정", description = "사방넷에 상품 정보를 업데이트한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/goods-modify")
    public ResponseEntity<?> sendGoodsToMall(HttpServletRequest request, @RequestBody SabangGoodsReq goodsReq) throws Exception {
        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();
        CommonDataRes res;

        res = sabangGoodsService.sendGoodsInfo(goodsReq.addParams(request));

        log.info(goodsReq.toString());

        message.setData(res);

        return new ResponseEntity<>(message, message.getStatus());
    }

    @Tag(name = "사방넷 주문수집", description = "에스푸드 사방넷 주문수집")
    @Operation(summary = "사방넷 상품 요약 수정", description = "사방넷에 요약 상품 정보를 업데이트한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/goods-lite-modify")
    public ResponseEntity<?> sendGoodsLiteToMall(HttpServletRequest request, @RequestBody SabangGoodsLiteReq goodsReq) throws Exception {
        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();
        CommonDataRes res;

        res = sabangGoodsService.sendGoodsLiteInfo(goodsReq.addParams(request));

        log.info(goodsReq.toString());

        message.setData(res);

        return new ResponseEntity<>(message, message.getStatus());
    }
}
