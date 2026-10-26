package com.sfood.eai.external.service;

import com.sfood.eai.external.component.document.sabang.data.SabangInvRegister;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.order.MallReprocessOrderReq;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.dto.sabang.SabangInvoiceResult;
import com.sfood.eai.external.dto.sabang.SabangOrderData;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import com.sfood.eai.external.entity.sabang.SabangInvoiceLog;
import com.sfood.eai.external.entity.sabang.SabangOrder;

import java.util.List;

public interface SabangOrderService {

    /**
     * 사방넷 주문 조회
     * @param configuration mall 환경설정 데이터
     * @param orderSearch 주문 조회 조건
     * @return 수집한 사방 주문 리턴
     * @throws Exception 예외 처리
     */
    List<SabangOrderData> retrieveSabangOrder(MallConfiguration configuration, SabangOrderSearchReq orderSearch) throws Exception;

    /**
     * 사방넷 Raw 데이터 저장
     * @param configuration mall 환경 설정 데이터
     * @param orderSearch 주문 조회 조건
     * @param sabangOrders 수집한 사방넷 주문 데이터
     * @return 저장된 사방넷 엔티티
     * @throws Exception 예외 처리
     */
    List<SabangOrder> saveSabangOrder(MallConfiguration configuration, SabangOrderSearchReq orderSearch, List<SabangOrderData> sabangOrders) throws Exception;

    /**
     * 사방넷 송장 정보 전송
     * 송장 정보와 배송에정일자를 같이 전송
     * @param orderInvoiceReq 송장 전송 데이터
     * @param dataModels 변환한 사방넷 송장 데이터
     * @return 전송 결과
     * @throws Exception 예외 처리
     */
    List<SabangInvoiceResult> sendInvoice(OrderInvoiceReq orderInvoiceReq, List<SabangInvRegister.Data> dataModels) throws Exception;

    /**
     * 사방넷 송장 전송 결과 저장
     * 송장 정보를 리스트 단위로 전송 후 결과를 필터해서 업데이트하고 리스트 전체를 DB에 저장
     * @param invoiceLogs 송장 전송 대상 데이터
     * @param invoiceResult 송장 전송 결과 데이터
     */
    void saveSabangInvoiceLog(List<SabangInvoiceLog> invoiceLogs, List<SabangInvoiceResult> invoiceResult);

    /**
     * 사방넷 주문 재생성
     * @param reprocessOrderReq 재생성할 사방넷 주문 정보
     * @return 생성 결과 응답
     * @throws Exception 예외 처리
     */
    CommonDataRes reprocessOrder(MallReprocessOrderReq reprocessOrderReq) throws Exception;
}
