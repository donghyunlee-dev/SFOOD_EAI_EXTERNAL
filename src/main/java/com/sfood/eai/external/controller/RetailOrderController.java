package com.sfood.eai.external.controller;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.ResponseMessage;
import com.sfood.eai.external.dto.order.ErrorOrderUpdateReq;
import com.sfood.eai.external.dto.retail.RetailOrderSearchReq;
import com.sfood.eai.external.service.RetailOrderService;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 리테일 주문 수집
 * 설명 : 배치로 저장된 리테일 Raw Data 를 조회하여 주문 데이터를 생성한다.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/retail")
@RestController
public class RetailOrderController {

    private final RetailOrderService retailOrderService;

    @Tag(name = "리테일 주문수집", description = "리테일 주문수집")
    @Operation(summary = "레테일 EDI 주문수집", description = "EDI 특정 날짜 주문을 OMS 주문 데이터로 변환한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping("/order-collect")
    public ResponseEntity<?> saveRetailEdiOrder(HttpServletRequest request, @RequestBody RetailOrderSearchReq orderSearch) {

        ResponseMessage<CommonDataRes> message = new ResponseMessage<>();
        CommonDataRes res;

        orderSearch.addParams(request);

        if (StringUtils.hasText(orderSearch.getInMallCode())) {
            res = retailOrderService.saveOrder(orderSearch);
        } else {
            res = saveRetailEdiOrderAll(orderSearch);
        }

        log.info(orderSearch.toString());

        message.setData(res);

        return new ResponseEntity<>(message, message.getStatus());
    }

    @Tag(name = "리테일 주문수집", description = "리테일 주문수집")
    @Operation(summary = "주문 재처리", description = "EDI Raw 데이터 기반으로 주문을 다시 생성한다.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"
            , useReturnTypeSchema = true
            , content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonDataRes.class)))})
    @PostMapping(value = "/order-partner-update")
    public ResponseEntity<?> updateErrorOrderPartner(HttpServletRequest request, @RequestBody ErrorOrderUpdateReq errorOrderUpdateReq) {

        ResponseMessage<CommonDataRes> response = new ResponseMessage<>();

        CommonDataRes resData = retailOrderService.updatePartnerAndGoodsOfErrorOrder(errorOrderUpdateReq.addParams(request));
        log.info(errorOrderUpdateReq.toString());
        response.setData(resData);

        return new ResponseEntity<>(response, response.getStatus());
    }

    public CommonDataRes saveRetailEdiOrderAll(RetailOrderSearchReq orderSearch) {
        CommonDataRes res = new CommonDataRes();

        List<String> partners = retailOrderService.findPartnersInEdiOrders(orderSearch);

        if (ObjectUtils.isEmpty(partners)) {
            return CommonDataRes.noDataResponse();
        }

        for (String partner : partners) {
            orderSearch.setInMallCode(partner);
            retailOrderService.saveOrder(orderSearch);
        }

        log.info(orderSearch.toString());
        res.setSuccessCnt(partners.size());

        return res;
    }
}
