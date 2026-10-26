package com.sfood.eai.external.controller;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.ResponseMessage;
import com.sfood.eai.external.dto.order.MallReprocessOrderReq;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import com.sfood.eai.external.entity.partner.MallPartnerInfo;
import com.sfood.eai.external.service.PartnerService;
import com.sfood.eai.external.service.SabangOrchestratorService;
import com.sfood.eai.external.service.SabangOrderService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 사방넷 API Controller
 * 설명 : 사방넷 OPEN API를 활용하여 주문 데이터를 수집한다.
 *       OCI Object Storage를 사용하여 XML 파일을 OCI에 업로드하고 사방넷 파라미터로 제공하여 API를 호출하는 방식
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/sabang")
@RestController
public class SabangOrderController {

    private final SabangOrderService sabangOrderService;
    private final SabangOrchestratorService sabangOrchestratorService;
    private final PartnerService partnerService;

    @Tag(name = "사방넷 주문수집", description = "에스푸드 사방넷 주문수집")
    @Operation(summary = "사방넷 주문수집", description = "특정 법인 회사에서 사용하는 사방넷의 특정 날짜 주문을 api 통신하여 데이터를 저장한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/order-collect")
    public ResponseEntity<?> collectSabangOrder(HttpServletRequest request, @RequestBody SabangOrderSearchReq orderSearch) throws Exception {
        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();
        CommonDataRes res;
        orderSearch.addParams(request);

        if (StringUtils.hasText(orderSearch.getMallId())) {
            res = sabangOrchestratorService.processOrder(orderSearch);
        } else {
            res = collectOrderAll(orderSearch);
        }

        log.info(orderSearch.toString());
        message.setData(res);

        return new ResponseEntity<>(message, message.getStatus());
    }

    @Tag(name = "사방넷 주문수집", description = "에스푸드 사방넷 주문수집")
    @Operation(summary = "사방넷 송장 등록", description = "사방넷에 주문의 송장 번호를 전송해서 상태를 업데이트한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/invoice-modify")
    public ResponseEntity<?> sendOrderInvoice(HttpServletRequest request, @RequestBody OrderInvoiceReq orderInvoice) {
        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();
        CommonDataRes res;

        orderInvoice.addParams(request);

        try {
            res = sabangOrchestratorService.processInvoice(orderInvoice);

            log.info(orderInvoice.toString());

            message.setData(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new ResponseEntity<>(message, message.getStatus());
    }

    @Tag(name = "사방넷 주문수집", description = "에스푸드 사방넷 주문수집")
    @Operation(summary = "주문 재처리", description = "Raw 데이터 기반으로 주문을 다시 생성한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/order-reprocess")
    public ResponseEntity<?> reprocessOrder(HttpServletRequest request, @RequestBody MallReprocessOrderReq reprocessOrderReq) throws Exception {
        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();

        CommonDataRes resData = sabangOrderService.reprocessOrder(reprocessOrderReq.addParams(request));
        log.info(reprocessOrderReq.toString());
        response.setData(resData);

        return new ResponseEntity<>(response, response.getStatus());
    }

    private CommonDataRes collectOrderAll(SabangOrderSearchReq orderSearch) {
        CommonDataRes res = new CommonDataRes();

        try {
            List<MallPartnerInfo> mallPartners = partnerService.findMallPartnerList(orderSearch.getCompanyCode(), orderSearch.getMallCode());

            for (MallPartnerInfo mallPartner : mallPartners) {
                sabangOrchestratorService.processOrder(orderSearch.setMallId(mallPartner.getInMallCode()));
            }

            res.setSuccessCnt(mallPartners.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return res;
    }
}
