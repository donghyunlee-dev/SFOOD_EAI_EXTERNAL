package com.sfood.eai.external.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterfaceType {
    GODO_ORDER_SEARCH_001( "/godo/order-collect", "고도몰 주문 수집 서비스"),
    GODO_CLAIM_SEARCH_001( "/godo/claim-collect", "고도몰 클레임 수집 서비스"),
    GODO_RECEIPT_SEARCH_001("https://api.sfoodmall.co.kr/order/order_info.php","주문 계산서 발행 여부 조회"),
    GODO_STATUS_UPDATE_001( "/godo/order-modify", "고도몰 주문 상태 업데이트 서비스"),
    GODO_STATUS_UPDATE_002( "/godo/invoice-modify", "고도몰 송장 전송 서비스"),
    GODO_STATUS_UPDATE_003( "/godo/claim-add", "고도몰 클레임 등록 서비스"),
    GODO_GOODS_UPDATE_001("/godo/goods-add", "고도몰 상품 등록"),
    GODO_GOODS_UPDATE_002("/godo/goods-modify", "고도몰 상품 수정"),
    GODO_GOODS_UPDATE_003("/godo/goods-remove", "고도몰 상품 삭제"),
    SABANG_ORDER_SEARCH_001( "/sabang/order-collect", "사방넷 주문 수집 서비스"),
    SABANG_CLAIM_SEARCH_001( "/sabang/claim-collect", "사방넷 클레임 수집 서비스"),
    SABANG_INVOICE_UPDATE_001( "/sabang/invoice-modify", "사방넷 송장 전송 서비스"),
    SABANG_GOODS_UPDATE_001( "/sabang/goods-modify", "사방넷 상품 전송 서비스"),
    SABANG_GOODS_UPDATE_002( "/sabang/goods-lite-modify", "사방넷 상품 요약 전송 서비스"),
    EDI_ORDER_COLLECT( "", "라테일 주문 수집 서비스"),
    EAI_INVENTORY_MOVE_CLOSE("/erp/close/move-close", "미지정 재고 이동 마감 처리"),
    NONE( "", ""),
    ;

    private final String serviceUri;
    private final String description;
}
