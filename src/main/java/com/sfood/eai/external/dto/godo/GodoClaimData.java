package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GodoClaimData {

    @JacksonXmlProperty(localName = "beforeStatus")
    private String beforeStatus;	// 이전 주문상태 주문상태코드 참조

    @JacksonXmlProperty(localName = "handleMode")
    private String handleMode;	//	클레임 모드 코드표 참조

    @JacksonXmlProperty(localName = "handleCompleteFl")
    private String handleCompleteFl; //	처리완료여부 코드표 참조

    @JacksonXmlProperty(localName = "handleReason")
    private String handleReason; // 클레임 사유

    @JacksonXmlProperty(localName = "handleDetailReason")
    private String handleDetailReason; //	클레임 상세사유

    @JacksonXmlProperty(localName = "handleDt")
    private String handleDt; // 처리완료일자

    @JacksonXmlProperty(localName = "refundPrice")
    private BigDecimal refundPrice; // 결제 환불금액

    @JacksonXmlProperty(localName = "refundUseDeposit")
    private BigDecimal refundUseDeposit; //	예치금 환불금액

    @JacksonXmlProperty(localName = "refundUseMileage")
    private BigDecimal refundUseMileage; //	적립금 환원금액

    @JacksonXmlProperty(localName = "refundDeliveryCharge")
    private BigDecimal refundDeliveryCharge; //	배송비 환불금액

    @JacksonXmlProperty(localName = "refundCharge")
    private BigDecimal refundCharge; // 환불수수료

    @JacksonXmlProperty(localName = "handleDetailReasonShowFl")
    private String handleDetailReasonShowFl; //	처리상세사유 출력여부

    @JacksonXmlProperty(localName = "refundDeliveryUseDeposit")
    private BigDecimal refundDeliveryUseDeposit; //	배송비에 할당된 사용 예치금 환원

    @JacksonXmlProperty(localName = "refundDeliveryUseMileage")
    private BigDecimal refundDeliveryUseMileage; //	배송비에 할당된 사용 마일리지 환원

    @JacksonXmlProperty(localName = "refundDeliveryInsuranceFee")
    private BigDecimal refundDeliveryInsuranceFee; //	환불 해외배송 보험료

    @JacksonXmlProperty(localName = "refundDeliveryCoupon")
    private BigDecimal refundDeliveryCoupon; // 취소 배송비쿠폰금액

    @JacksonXmlProperty(localName = "refundUseDepositCommission")
    private BigDecimal refundUseDepositCommission; // 사용예치금 수수료

    @JacksonXmlProperty(localName = "refundUseMileageCommission")
    private BigDecimal refundUseMileageCommission; //사용마일리지 수수료

    @JacksonXmlProperty(localName = "handleGroupCd")
    private Integer handleGroupCd; //	INTEGER	클레임일회처리당 그룹

    @JacksonXmlProperty(localName = "regDt")
    private String regDt; //	등록일
}
