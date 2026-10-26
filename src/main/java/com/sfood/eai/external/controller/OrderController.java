package com.sfood.eai.external.controller;


import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.ErrorOrderUpdateReq;
import com.sfood.eai.external.dto.ResponseMessage;
import com.sfood.eai.external.dto.order.OrderRequest;
import com.sfood.eai.external.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * OMS에서 직접 호출하는 컨트롤러
 * 설명 : 연동몰 외에 주문 관련하여 제공하는 API
 */
@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService<?> orderService;

    @Tag(name = "OMS 주문 API", description = "주문 관련 엔드포인트")
    @Operation(summary = "주문 오류 업데이트", description = "주문의 미맵핑 데이터를 업데이트한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping("/order-error-update")
    public ResponseEntity<?> updateErrorOrders(HttpServletRequest request, @RequestBody ErrorOrderUpdateReq errorOrderUpdateReq) throws Exception {
        ResponseMessage<Object> message = new ResponseMessage<>();

        CompletableFuture<CommonDataRes> res = orderService.resolveErrorOrder(errorOrderUpdateReq.addParams(request));
        message.setData(res.get());

        return new ResponseEntity<>(message, message.getStatus());
    }

    @Tag(name = "OMS 주문 API", description = "주문 관련 엔드포인트")
    @Operation(summary = "기초 주문/클레임 키 값 생성 ", description = "주문과 클레임 번호의 초기 값을 생성한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping("/init")
    public ResponseEntity<?> initOrder(HttpServletRequest request, @RequestBody OrderRequest orderDate) throws Exception {
        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();

        CommonDataRes res = orderService.initOrder(orderDate.addParams(request));
        message.setData(res);

        return new ResponseEntity<>(message, message.getStatus());
    }
}
