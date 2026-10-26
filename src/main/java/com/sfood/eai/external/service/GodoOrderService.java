package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.dto.godo.GodoOrderData;
import com.sfood.eai.external.dto.godo.GodoOrderSearchReq;
import com.sfood.eai.external.dto.godo.GodoOrderStatusRes;
import com.sfood.eai.external.dto.order.*;
import com.sfood.eai.external.entity.godo.GodoDataEntity;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.exception.APIException;
import com.sfood.eai.external.type.APIError;
import com.sfood.eai.external.type.godo.GodoOrderStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface GodoOrderService {
    /**
     * 고도몰 주문 수집
     * 입금대기, 결제완료 상태의 주문을 수집한다.
     * @param orderSearch 고도몰 API 조회 항목
     * @return 수집한 고도몰 주문 리스트
     * @throws Exception 오류 발생 시 예외 처리
     */
    List<GodoOrderData> retrieveGodoOrders(GodoOrderSearchReq orderSearch) throws Exception;

    /**
     * 고도몰 클레임 수집
     * @param orderSearch 고도몰 API 조회 항목
     * @return 수집한 고도몰 클레임 주문 리스트
     * @throws Exception 오류 발생 시 예외 처리
     */
    List<GodoOrderData> retrieveGodoClaims(GodoOrderSearchReq orderSearch) throws Exception;

    /**
     * 고도몰 주문의 세금계산서 발행 여부를 조회
     * 고도몰 커스텀 API를 통해 날짜별 세금계산서 발행 여부를 조회
     * @param orderSearch 고도몰 API 조회 항목
     * @return 고도몰 주문번호 , 발행 여부
     * @throws APIException 오류 발생 시 예외 처리
     */
    Map<String, String> retrieveOrderReceipts(GodoOrderSearchReq orderSearch) throws APIException;

    /**
     * 고도몰 주문을 저장
     * @param orderSearch 고도몰 API 조회 항목
     * @param godoData 수신한 고도몰 저장 객체
     * @return 저장 후 고도몰 Raw Entity
     */
    GodoDataEntity saveGodoOrder(GodoOrderSearchReq orderSearch, GodoOrderData godoData);

    /**
     * 고도몰 주문 송장 정보와 상태 전송
     * @throws Exception 오류 발생 시 예외 처리
     */
    GodoOrderStatusRes sendInvoice(CommonSearchReq searchReq, OrderInvoiceReq.OrderInvoiceInfo invoice) throws APIException;

    /**
     * XML 파일을 활용한 고도몰 주문 송장 정보 상태 전송
     * @param searchReq 요청 파라미터
     * @param invoice 주문 송장 정보
     * @return 고도몰 응답 데이터
     * @throws Exception 오류 발생 시 예외 처리
     */
    GodoOrderStatusRes sendInvoicesWithXmlFile(CommonSearchReq searchReq, List<OrderInvoiceReq.OrderInvoiceInfo> invoice) throws Exception;

    /**
     * 고도몰 주문 상태 전송
     * @param searchReq 요청 파라미터
     * @param orderStatusInfo 주문 상태 정보
     * @param status 고도몰 변환 상태 정보
     * @return 고도몰 응답 데이터
     * @throws APIException 오류 발생 시 예외 처리
     */
    GodoOrderStatusRes sendOrderStatus(CommonSearchReq searchReq, OrderStatusReq.OrderStatusInfo orderStatusInfo, GodoOrderStatus status) throws APIException;

    /**
     * 고도몰 상태, 송장 전송 로그 저장
     * @param goodsId OMS 주문번호 데이터
     * @param orderStatusResponse 고도몰 응답 데이터
     */
    void saveStatusResponseLog(OrderGoodsId goodsId, GodoOrderStatusRes orderStatusResponse);

    /**
     * 고도몰 상태, 송장 전송 로그 저장
     * @param searchReq 요청 파라미터
     * @param invoice 주문 송장 정보
     * @param orderStatusResponse 고도몰 응답 데이터
     */
    void saveStatusResponseLog(CommonSearchReq searchReq, OrderInvoiceReq.MallOrderInvoice invoice, GodoOrderStatusRes orderStatusResponse);

    /**
     * 고도몰 주문 클레임 등록
     * @param orderClaimReq 수신 받은 주문 클레임 정보
     * @throws Exception 오류 발생 시 예외 처리
     */
    CommonDataRes sendOrderClaim(OrderClaimReq orderClaimReq) throws Exception;

    /**
     * 고도몰 주문 재 생성
     * 주문 정보를 삭제하고 Raw Data 기준으로 다시 생성한다.
     * 연동몰 API를 호출하지 않는다.
     * @param reprocessOrderReq 고도몰 주문번호
     * @throws Exception 오류 발생 시 예외 처리
     */
    CommonDataRes reCreateOrder(MallReprocessOrderReq reprocessOrderReq) throws Exception;

    /**
     * 고도몰 입금대기 주문 조회
     * 생성된 주문 중에 입금대기인 주문의 주문번호 리스트를 조회한다.
     * @param searchReq 조회 조건
     * @return 고도몰 입금대기 주문번호
     */
    List<MallOrderId> retrieveUnSettledOrderIds(GodoOrderSearchReq searchReq);
}
