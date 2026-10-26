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
public class GodoOrderDeliveryData {
	
	@JacksonXmlProperty(localName = "scmNo")
	private Integer scmNo; // INTEGER	공급사 번호
	
	@JacksonXmlProperty(localName = "commission")
	private BigDecimal commission; // FLOAT	배송비 수수료율
	
	@JacksonXmlProperty(localName = "scmAdjustNo")
	private String scmAdjustNo;	// INTEGER	공급사 정산고유번호
	
	@JacksonXmlProperty(localName = "deliveryCharge")
	private String deliveryCharge; // FLOAT	총 배송비 (지역별 배송비 포함)
	
	@JacksonXmlProperty(localName = "deliveryPolicyCharge")
	private String deliveryPolicyCharge; //	FLOAT	총 배송비 (지역별 배송비 제외)
	
	@JacksonXmlProperty(localName = "deliveryAreaCharge")
	private String deliveryAreaCharge; // FLOAT	총 지역별 배송비
	
	@JacksonXmlProperty(localName = "deliveryFixFl")
	private String deliveryFixFl; // STRING	배송정책 코드표 참조
	
	@JacksonXmlProperty(localName = "deliveryCollectFl")
	private String deliveryCollectFl; // STRING	배송비 결제방법 코드표 참조
	
	@JacksonXmlProperty(localName = "orderInfoSno")
	private String orderInfoSno; //	INTEGER	배송지 정보 고유번호, ※ orderInfoData > sno 와 동일한 값, ※ order_data > multiShippingFl 가 y (복수배송지 사용) 인 경우 변수 존재
	
	@JacksonXmlProperty(localName = "sno")
	private String sno;	// INTEGER	일련번호
	
	@JacksonXmlProperty(localName = "scmAdjustAfterNo")
	private String scmAdjustAfterNo; //	INTEGER	공급사 정산 후 환불의 정산 고유 번호
	
	@JacksonXmlProperty(localName = "divisionDeliveryUseDeposit")
	private String divisionDeliveryUseDeposit; // FLOAT	주문할인 금액의 안분된 예치금
	
	@JacksonXmlProperty(localName = "divisionDeliveryUseMileage")
	private String divisionDeliveryUseMileage; // FLOAT	주문할인 금액의 안분된 마일리지
	
	@JacksonXmlProperty(localName = "divisionDeliveryCharge")
	private String divisionDeliveryCharge; // FLOAT	총 배송비 쿠폰 안분 금액
	
	@JacksonXmlProperty(localName = "divisionMemberDeliveryDcPrice")
	private String divisionMemberDeliveryDcPrice; // FLOAT	회원 배송비 무료 안분 금액
	
	@JacksonXmlProperty(localName = "deliveryInsuranceFee")
	private String deliveryInsuranceFee; //	FLOAT	EMS배송시 적용되는 해외배송 보험료
	
	@JacksonXmlProperty(localName = "deliveryWeightInfo")
	private String deliveryWeightInfo; // JSON	총 배송무게 (상품+박스)
	
	@JacksonXmlProperty(localName = "overseasDeliveryPolicy")
	private String overseasDeliveryPolicy; // STRING	주문당시의 해외 배송정책
	
	@JacksonXmlProperty(localName = "deliveryCollectPrice")
	private String deliveryCollectPrice; //	FLOAT	착불시 발생된 배송비
	
	@JacksonXmlProperty(localName = "deliveryWholeFreePrice")
	private String deliveryWholeFreePrice; // FLOAT	동일 배송비 무료조건시의 배송비
	
	@JacksonXmlProperty(localName = "statisticsOrderFl")
	private String statisticsOrderFl; // STRING	주문/매출 통계 처리 상태
}
