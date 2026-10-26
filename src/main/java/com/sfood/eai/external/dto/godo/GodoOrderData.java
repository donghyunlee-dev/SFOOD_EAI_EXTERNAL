package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GodoOrderData {
	
	@JacksonXmlProperty(localName = "orderNo")
	private String orderNo; // 주문번호
	
	@JacksonXmlProperty(localName = "memNo")
	private Integer memNo; // 회원번호
	
	@JacksonXmlProperty(localName = "apiOrderGoodsNo")
	private String apiOrderGoodsNo; // 외부채널품목고유번호
	
	@JacksonXmlProperty(localName = "orderStatus")
	private String orderStatus; // 주문상태코드
	
	@JacksonXmlProperty(localName = "orderIp")
	private String orderIp; // 주문자IP
	
	@JacksonXmlProperty(localName = "orderChannelFl")
	private String orderChannelFl; // 주문채널

	@JacksonXmlProperty(localName = "orderTypeFl")
	private String orderTypeFl; // 주문유형코드

	@JacksonXmlProperty(localName = "orderEmail")
	private String orderEmail; // 이메일

	@JacksonXmlProperty(localName = "orderGoodsNm")
	private String orderGoodsNm; // 주문상품명

	@JacksonXmlProperty(localName = "orderGoodsCnt")
	private Double orderGoodsCnt; // 주문상품갯수

	@JacksonXmlProperty(localName = "settlePrice")
	private Double settlePrice; // 총 주문금액

	@JacksonXmlProperty(localName = "taxSupplyPrice")
	private Double taxSupplyPrice; // 최초 총 과세금액

	@JacksonXmlProperty(localName = "taxVatPrice")
	private Double taxVatPrice; // 최초 총 부가세 금액

	@JacksonXmlProperty(localName = "taxFreePrice")
	private Double taxFreePrice; // 최초 총 면세금액

	@JacksonXmlProperty(localName = "realTaxSupplyPrice")
	private Double realTaxSupplyPrice; // 실제 총 과세금액(환불제외)

	@JacksonXmlProperty(localName = "realTaxVatPrice")
	private Double realTaxVatPrice; // 실제 총 부가세(환불제외)

	@JacksonXmlProperty(localName = "realTaxFreePrice")
	private Double realTaxFreePrice; // 실제 총 면세금액(환불제외)

	@JacksonXmlProperty(localName = "useMileage")
	private Double useMileage; // 주문시 사용한 마일리지
	
	@JacksonXmlProperty(localName = "useDeposit")
	private Double useDeposit; // 주문시 사용한 예치금
	
	@JacksonXmlProperty(localName = "totalGoodsPrice")
	private Double totalGoodsPrice; // 총 상품 금액
	
	@JacksonXmlProperty(localName = "totalDeliveryCharge")
	private Double totalDeliveryCharge; // 총 배송비
	
	@JacksonXmlProperty(localName = "totalGoodsDcPrice")
	private Double totalGoodsDcPrice; // 총 상품 할인 금액
	
	@JacksonXmlProperty(localName = "totalMemberDcPrice")
	private Double totalMemberDcPrice; // 총 회원 할인 금액
	
	@JacksonXmlProperty(localName = "totalMemberOverlapDcPrice")
	private Double totalMemberOverlapDcPrice; // 총 그룹별 회원 중복할인 금액
	
	@JacksonXmlProperty(localName = "totalCouponGoodsDcPrice")
	private Double totalCouponGoodsDcPrice; // 총 상품쿠폰 할인 금액
	
	@JacksonXmlProperty(localName = "totalCouponOrderDcPrice")
	private Double totalCouponOrderDcPrice; // 총 주문쿠폰 할인 금액
	
	@JacksonXmlProperty(localName = "totalCouponDeliveryDcPrice")
	private Double totalCouponDeliveryDcPrice; // 총 배송쿠폰 할인금액
	
	@JacksonXmlProperty(localName = "totalMileage")
	private Double totalMileage; // 총 적립 마일리지
	
	@JacksonXmlProperty(localName = "totalGoodsMileage")
	private Double totalGoodsMileage; // 총 상품 상품적립 마일리지

	@JacksonXmlProperty(localName = "totalMemberMileage")
	private Double totalMemberMileage; // 총 회원적립 마일리지
	
	@JacksonXmlProperty(localName = "totalCouponGoodsMileage")
	private Double totalCouponGoodsMileage; // 총 상품쿠폰 적립 마일리지
	
	@JacksonXmlProperty(localName = "totalCouponOrderMileage")
	private Double totalCouponOrderMileage; // 총 주문쿠폰 적립 마일리지
	
	@JacksonXmlProperty(localName = "firstSaleFl")
	private String firstSaleFl; // 첫구매 여부 코드
	
	@JacksonXmlProperty(localName = "settleKind")
	private String settleKind; // 주문방법 코드
	
	@JacksonXmlProperty(localName = "multiShippingFl")
	private String multiShippingFl; // 복수배송지 사용여부 (y = 사용, n = 미사용)
	
	@JacksonXmlProperty(localName = "paymentDt")
	private String paymentDt; // 입금일자

	@JacksonXmlProperty(localName = "orderDate")
	private String orderDate; // 입금일자

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "orderInfoData")
	private List<GodoOrderInfoData> godoOrderInfoList; // 배송지 정보

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "orderGoodsData")
	private List<GodoOrderGoodsData> godoOrderGoodsList; // 주문상품 정보

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "orderDeliveryData")
	private List<GodoOrderDeliveryData> godoOrderDeliveryList; // 배송비 정보

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "addGoodsData")
	private List<GodoAddGoodsData> godoAddGoodsList; // 추가상품 정보

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "giftData")
	private List<GodoGiftData> godoGiftList; // 사은품 정보

	@JacksonXmlProperty(localName = "bankName")
	private String bankName; // 입금계좌 은행명
	
	@JacksonXmlProperty(localName = "accountNumber")
	private String accountNumber; // 입금계좌 계좌번호, ※ bankName이 있는 경우 같이 전달
	
	@JacksonXmlProperty(localName = "depositor")
	private String depositor; // 입금계좌 예금주, ※ bankName이 있는 경우 같이 전달
	
	@JacksonXmlProperty(localName = "memId")
	private String memId; // 회원아이디
	
	@JacksonXmlProperty(localName = "memGroupNm")
	private String memGroupNm; // 회원그룹명
	
	@JacksonXmlProperty(localName = "mallSno")
	private String mallSno; // 상점번호 코드표 참조
	
	@JacksonXmlProperty(localName = "appOs")
	private String appOs; // 앱 주문시 휴대폰 OS
	
	@JacksonXmlProperty(localName = "pushCode")
	private String pushCode; // 앱 주문시 푸시 코드
	
	@JacksonXmlProperty(localName = "statisticsAppOrderCntFl")
	private String statisticsAppOrderCntFl; // 주문건수 앱 통계 처리 여부
	
	@JacksonXmlProperty(localName = "orderGoodsNmStandard")
	private String orderGoodsNmStandard; // 주문 상품명(기준몰)
	
	@JacksonXmlProperty(localName = "overseasSettleCurrency")
	private String overseasSettleCurrency; // 해외PG 승인금액 적용 환율 코드
	
	@JacksonXmlProperty(localName = "overseasSettlePrice")
	private String overseasSettlePrice; // 해외PG 승인금액 (환율변환 적용)
	
	@JacksonXmlProperty(localName = "totalDeliveryInsuranceFee")
	private String totalDeliveryInsuranceFee; // 해외배송 EMS 보험료
	
	@JacksonXmlProperty(localName = "totalMemberBankDcPrice")
	private String totalMemberBankDcPrice; // 총 회원등급 브랜드 무통장 할인 금액
	
	@JacksonXmlProperty(localName = "totalMemberDeliveryDcPrice")
	private String totalMemberDeliveryDcPrice; // 총 회원 배송비 할인
	
	@JacksonXmlProperty(localName = "totalMyappDcPrice")
	private String totalMyappDcPrice; // 총 마이앱 할인 금액
	
	@JacksonXmlProperty(localName = "totalEnuriDcPrice")
	private String totalEnuriDcPrice; // 총 운영자추가할인
	
	@JacksonXmlProperty(localName = "totalDeliveryWeight")
	private String totalDeliveryWeight; // 배송 총 무게
	
	@JacksonXmlProperty(localName = "currencyPolicy")
	private String currencyPolicy; // 주문당시의 상점통화 기본정책
	
	@JacksonXmlProperty(localName = "exchangeRatePolicy")
	private String exchangeRatePolicy; // 주문당시의 환율 기본정책
	
	@JacksonXmlProperty(localName = "myappPolicy")
	private String myappPolicy; // 주문당시의 마이앱 기본정책
	
	@JacksonXmlProperty(localName = "adminMemo")
	private String adminMemo; // 관리자 메모
	
	@JacksonXmlProperty(localName = "pgRealTaxSupplyPrice")
	private String pgRealTaxSupplyPrice; // 실제 총 PG 과세금액
	
	@JacksonXmlProperty(localName = "pgRealTaxVatPrice")
	private String pgRealTaxVatPrice; // 실제 총 PG 부가세
	
	@JacksonXmlProperty(localName = "pgRealTaxFreePrice")
	private String pgRealTaxFreePrice; // 실제 총 PG 면세금액
	
	@JacksonXmlProperty(localName = "bankdaManualNo")
	private String bankdaManualNo; // 뱅크다 번호
	
	@JacksonXmlProperty(localName = "bankdaManualFl")
	private String bankdaManualFl; // 자동입금수동여부
	
	@JacksonXmlProperty(localName = "bankdaManualMangerId")
	private String bankdaManualMangerId; // 자동입금수동처리관리자아이디
	
	@JacksonXmlProperty(localName = "trackingKey")
	private String trackingKey; // 페이코 쇼핑 트래킹키
	
	@JacksonXmlProperty(localName = "userHandleProcess")
	private String userHandleProcess; // 주문 환불/반품/교환 자동 승인 처리 여부
	
	@JacksonXmlProperty(localName = "pgChargeBack")
	private String pgChargeBack; // 차지백여부
	
	@JacksonXmlProperty(localName = "fbPixelKey")
	private String fbPixelKey; // 페이스북 픽셀 쿠키값

	@Transient
	private String receiptFl;

	public double getTotalCouponGoodsDcPrice() {
		return (this.totalCouponGoodsDcPrice == null ? 0 : this.totalCouponGoodsDcPrice);
	}
}
