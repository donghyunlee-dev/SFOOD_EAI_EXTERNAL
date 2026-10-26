package com.sfood.eai.external.controller;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.ResponseMessage;
import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.dto.order.*;
import com.sfood.eai.external.service.GodoOrchestratorService;
import com.sfood.eai.external.service.GodoOrderService;
import com.sfood.eai.external.type.godo.GodoDateType;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 고도몰 API Controller
 * 설명 : 고도몰 OPEN API를 활용하여 주문 관련 데이터를 수집하고 상태를 동기화한다.
 * 연동몰 유형 : JohnCook, Sfood, Medisola
 * API 타입 :
 * 1. 주문 수집
 * 2. 클레임 수집
 * 3. 주문 배송 송장 정보 전달
 * 4. 주문 상태 변경 전달
 * 호출 방식 :
 * 1. Internal API 호출
 * 2. Batch Service API 호출 (개발 미진행)
 * 2. 수동 API 호출 (서버 호출 불가)
 */
@Slf4j
@RequestMapping("/godo")
@RestController
public class GodoOrderController {

    private final GodoOrderService godoOrderService;
    private final GodoOrchestratorService godoOrchestratorService;

    public GodoOrderController(GodoOrderService godoOrderService, GodoOrchestratorService godoOrchestratorService) {
        this.godoOrderService = godoOrderService;
        this.godoOrchestratorService = godoOrchestratorService;
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "주문 수집", description = "존쿡 주문 데이터를 수집한다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/order-collect")
    public ResponseEntity<?> collectOrder(HttpServletRequest request, @RequestBody GodoOrderSearchReq orderSearch) throws Exception {
        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();
        CommonDataRes resData = new CommonDataRes();
        CommonDataRes resultData;

        if (StringUtils.hasText(orderSearch.getOrderStatus())) {
            resData = godoOrchestratorService.processOrder(orderSearch.addParams(request));
            log.info(orderSearch.toString());
        } else {
            orderSearch.setOrderStatus(GodoOrderStatus.O1.code());
            resultData = godoOrchestratorService.processOrder(orderSearch.addParams(request));
            resData.addSuccessCnt(resultData.getSuccessCnt());
            log.info(orderSearch.toString());

            Thread.sleep(500);

            orderSearch.setOrderStatus(GodoOrderStatus.P1.code());
            resultData = godoOrchestratorService.processOrder(orderSearch.addParams(request));
            resData.addSuccessCnt(resultData.getSuccessCnt());
            log.info(orderSearch.toString());

            List<MallOrderId> mallOrderIds = godoOrderService.retrieveUnSettledOrderIds(orderSearch);

            if (CollectionUtils.isNotEmpty(mallOrderIds)) {
                for (MallOrderId mallOrderId : mallOrderIds) {
                    orderSearch.setOrderNo(mallOrderId.getOrderNo());
                    resultData = godoOrchestratorService.processOrder(orderSearch);
                    resData.addSuccessCnt(resultData.getSuccessCnt());
                }
            }
        }

        response.setData(resData);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "클레임 수집", description = "존쿡 클레임 데이터를 수집한다")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/claim-collect")
    public ResponseEntity<?> collectClaim(HttpServletRequest request, @RequestBody GodoOrderSearchReq claimSearch) throws Exception {
        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();
        CommonDataRes resData = new CommonDataRes();

        if (StringUtils.hasText(claimSearch.getOrderStatus())) {
            claimSearch.setDateType(GodoDateType.MODIFY.code());
            resData = godoOrchestratorService.processClaim(claimSearch.addParams(request));
            log.info(claimSearch.toString());
        } else {

            for (GodoOrderStatus status : GodoOrderStatus.values()) {
                if (status.isClaimStatus()) {
                    claimSearch.setDateType(GodoDateType.MODIFY.code());
                    claimSearch.setOrderStatus(status.code());
                    resData = godoOrchestratorService.processClaim(claimSearch.addParams(request));
                    resData.addSuccessCnt(resData.getSuccessCnt());
                    Thread.sleep(500);
                    log.info(claimSearch.toString());
                }
            }
        }

        response.setData(resData);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "주문 송장 전송", description = "송장 정보를 수신 받아 상태와 송장 정보를 고도몰에 전송한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/invoice-modify")
    public ResponseEntity<?> sendInvoice(HttpServletRequest request, @RequestBody OrderInvoiceReq orderInvoice) throws Exception {
        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();

        godoOrchestratorService.processInvoice(orderInvoice.addParams(request));
        // 비동기 처리로 인해 수신 받은 리스트 확인만 가능하게 응답 데이터 리턴
        CommonDataRes commonDataRes = new CommonDataRes();
        commonDataRes.setSuccessCnt(orderInvoice.getMallInvoiceList().stream().mapToInt(invoice -> invoice.getOrderInvoiceList().size()).sum());
        response.setData(commonDataRes);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "주문 상태 변경", description = "변경된 주문 상태를 고도몰에 전송한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/order-modify")
    public ResponseEntity<?> sendOrderStatus(HttpServletRequest request, @RequestBody OrderStatusReq orderStatus) throws Exception {
        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();

        godoOrchestratorService.processOrderStatus(orderStatus.addParams(request));

        CommonDataRes commonDataRes = new CommonDataRes();
        commonDataRes.setSuccessCnt(orderStatus.getMallOrderList().stream().mapToInt(statusInfo -> statusInfo.getOrderstatusList().size()).sum());
        response.setData(commonDataRes);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "주문 클레임 등록", description = "주문 클레임 정보를 고도몰에 전송한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/claim-add")
    public ResponseEntity<?> sendOrderClaim(HttpServletRequest request, @RequestBody OrderClaimReq orderClaim) throws Exception {
        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();

        CommonDataRes resData = godoOrderService.sendOrderClaim(orderClaim.addParams(request));
        log.info(orderClaim.toString());
        response.setData(resData);

        return new ResponseEntity<>(response, response.getStatus());
    }

    @Tag(name = "고도몰 주문수집", description = "고도몰 OPEN API 통신")
    @Operation(summary = "주문 재처리", description = "Raw 데이터 기반으로 주문을 다시 생성한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/order-reprocess")
    public ResponseEntity<?> reprocessOrder(HttpServletRequest request, @RequestBody MallReprocessOrderReq reprocessOrderReq) throws Exception {
        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();

        CommonDataRes resData = godoOrderService.reCreateOrder(reprocessOrderReq.addParams(request));
        log.info(reprocessOrderReq.toString());
        response.setData(resData);

        return new ResponseEntity<>(response, response.getStatus());
    }
}
