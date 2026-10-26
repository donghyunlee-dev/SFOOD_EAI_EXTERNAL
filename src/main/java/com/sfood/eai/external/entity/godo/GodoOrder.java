package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.entity.id.godo.GodoOrderId;
import com.sfood.eai.external.entity.order.BaseEntity;
import com.sfood.eai.external.util.CustomBigDecimalUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@NoArgsConstructor
@IdClass(GodoOrderId.class)
@Entity
@Table(name="TBL_GODO_ORDER")
public class GodoOrder extends BaseEntity {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode; // 법인코드

	@Id
	@Column(name = "MARKET_MALL_CODE")
	private String marketMallCode; // 연동몰코드

	@Id
	@Column(name = "ORDER_CHANNEL_CODE")
	private String orderChannelCode; // 주문채널코드

	@Id
	@Column(name = "ORDER_NO")
	private String orderNo; // 주문번호
	
	@Column(name = "MEM_NO")
	private Integer memNo; // 회원번호
	
	@Column(name = "API_ORDER_GOODS_NO")
	private String apiOrderGoodsNo; // 외부채널품목고유번호
	
	@Column(name = "ORDER_STATUS")
	private String orderStatus; // 주문상태코드
	
	@Column(name = "ORDER_IP")
	private String orderIp; // 주문자IP
	
	@Column(name = "ORDER_CHANNEL_FL")
	private String orderChannelFl; // 주문채널
	
	@Column(name = "ORDER_TYPE_FL")
	private String orderTypeFl; // 주문유형코드
	
	@Column(name = "ORDER_EMAIL")
	private String orderEmail; // 이메일
	
	@Column(name = "ORDER_GOODS_NM")
	private String orderGoodsNm; // 주문상품명
	
	@Column(name = "ORDER_GOODS_CNT")
	private Integer orderGoodsCnt; // 주문상품갯수
	
	@Column(name = "SETTLE_PRICE")
	private BigDecimal settlePrice; // 총 주문금액
	
	@Column(name = "TAX_SUPPLY_PRICE")
	private BigDecimal taxSupplyPrice; // 최초 총 과세금액

	@Column(name = "TAX_VAT_PRICE")
	private BigDecimal taxVatPrice; // 최초 총 부가세 금액

	@Column(name = "TAX_FREE_PRICE")
	private BigDecimal taxFreePrice; // 최초 총 면세금액

	@Column(name = "REAL_TAX_SUPPLY_PRICE")
	private BigDecimal realTaxSupplyPrice; // 실제 총 과세금액(환불제외)

	@Column(name = "REAL_TAX_VAT_PRICE")
	private BigDecimal realTaxVatPrice; // 실제 총 부가세(환불제외)

	@Column(name = "REAL_TAX_FREE_PRICE")
	private BigDecimal realTaxFreePrice; // 실제 총 면세금액(환불제외)

	@Column(name = "USE_MILEAGE")
	private BigDecimal useMileage; // 주문시 사용한 마일리지
	
	@Column(name = "USE_DEPOSIT")
	private BigDecimal useDeposit; // 주문시 사용한 예치금
	
	@Column(name = "TOTAL_GOODS_PRICE")
	private BigDecimal totalGoodsPrice; // 총 상품 금액
	
	@Column(name = "TOTAL_DELIVERY_CHARGE")
	private BigDecimal totalDeliveryCharge; // 총 배송비
	
	@Column(name = "TOTAL_GOODS_DC_PRICE")
	private BigDecimal totalGoodsDcPrice; // 총 상품 할인 금액
	
	@Column(name = "TOTAL_MEMBER_DC_PRICE")
	private BigDecimal totalMemberDcPrice; // 총 회원 할인 금액
	
	@Column(name = "TOTAL_MEMBER_OVERLAP_DC_PRICE")
	private BigDecimal totalMemberOverlapDcPrice; // 총 그룹별 회원 중복할인 금액
	
	@Column(name = "TOTAL_COUPON_GOODS_DC_PRICE")
	private BigDecimal totalCouponGoodsDcPrice; // 총 상품쿠폰 할인 금액
	
	@Column(name = "TOTAL_COUPON_ORDER_DC_PRICE")
	private BigDecimal totalCouponOrderDcPrice; // 총 주문쿠폰 할인 금액
	
	@Column(name = "TOTAL_COUPON_DELIVERY_DC_PRICE")
	private BigDecimal totalCouponDeliveryDcPrice; // 총 배송쿠폰 할인금액
	
	@Column(name = "TOTAL_MILEAGE")
	private BigDecimal totalMileage; // 총 적립 마일리지
	
	@Column(name = "TOTAL_GOODS_MILEAGE")
	private BigDecimal totalGoodsMileage; // 총 상품 상품적립 마일리지

	@Column(name = "TOTAL_MEMBER_MILEAGE")
	private BigDecimal totalMemberMileage; // 총 회원적립 마일리지
	
	@Column(name = "TOTAL_COUPON_GOODS_MILEAGE")
	private BigDecimal totalCouponGoodsMileage; // 총 상품쿠폰 적립 마일리지
	
	@Column(name = "TOTAL_COUPON_ORDER_MILEAGE")
	private BigDecimal totalCouponOrderMileage; // 총 주문쿠폰 적립 마일리지
	
	@Column(name = "FIRST_SALE_FL")
	private String firstSaleFl; // 첫구매 여부 코드

	@Column(name = "SETTLE_KIND")
	private String settleKind; // 주문방법 코드
	
	@Column(name = "MULTI_SHIPPING_FL")
	private String multiShippingFl; // 복수배송지 사용여부 (y = 사용, n = 미사용)
	
	@Column(name = "PAYMENT_DT")
	private String paymentDt; // 입금일자

	@Column(name = "ORDER_DATE")
	private String orderDate;

	@Column(name = "BANK_NAME")
	private String bankName; // 입금계좌 은행명
	
	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber; // 입금계좌 계좌번호, ※ bankName이 있는 경우 같이 전달
	
	@Column(name = "DEPOSITOR")
	private String depositor; // 입금계좌 예금주, ※ bankName이 있는 경우 같이 전달
	
	@Column(name = "MEM_ID")
	private String memId; // 회원아이디
	
	@Column(name = "MEM_GROUP_NM")
	private String memGroupNm; // 회원그룹명
	
	@Column(name = "MALL_SNO")
	private String mallSno; // 상점번호 코드표 참조
	
	@Column(name = "APP_OS")
	private String appOs; // 앱 주문시 휴대폰 OS
	
	@Column(name = "PUSH_CODE")
	private String pushCode; // 앱 주문시 푸시 코드
	
	@Column(name = "STATISTICS_APP_ORDER_CNT_FL")
	private String statisticsAppOrderCntFl; // 주문건수 앱 통계 처리 여부
	
	@Column(name = "ORDER_GOODS_NM_STANDARD")
	private String orderGoodsNmStandard; // 주문 상품명(기준몰)
	
	@Column(name = "OVERSEAS_SETTLE_CURRENCY")
	private String overseasSettleCurrency; // 해외PG 승인금액 적용 환율 코드
	
	@Column(name = "OVERSEAS_SETTLE_PRICE")
	private BigDecimal overseasSettlePrice; // 해외PG 승인금액 (환율변환 적용)
	
	@Column(name = "TOTAL_DELIVERY_INSURANCE_FEE")
	private BigDecimal totalDeliveryInsuranceFee; // 해외배송 EMS 보험료
	
	@Column(name = "TOTAL_MEMBER_BANK_DC_PRICE")
	private BigDecimal totalMemberBankDcPrice; // 총 회원등급 브랜드 무통장 할인 금액
	
	@Column(name = "TOTAL_MEMBER_DELIVERY_DC_PRICE")
	private BigDecimal totalMemberDeliveryDcPrice; // 총 회원 배송비 할인
	
	@Column(name = "TOTAL_MYAPP_DC_PRICE")
	private BigDecimal totalMyappDcPrice; // 총 마이앱 할인 금액
	
	@Column(name = "TOTAL_ENURI_DC_PRICE")
	private BigDecimal totalEnuriDcPrice; // 총 운영자추가할인
	
	@Column(name = "TOTAL_DELIVERY_WEIGHT")
	private BigDecimal totalDeliveryWeight; // 배송 총 무게
	
	@Column(name = "CURRENCY_POLICY")
	private String currencyPolicy; // 주문당시의 상점통화 기본정책
	
	@Column(name = "EXCHANGE_RATE_POLICY")
	private String exchangeRatePolicy; // 주문당시의 환율 기본정책
	
	@Column(name = "MYAPP_POLICY")
	private String myappPolicy; // 주문당시의 마이앱 기본정책
	
	@Column(name = "ADMIN_MEMO")
	private String adminMemo; // 관리자 메모
	
	@Column(name = "PG_REAL_TAX_SUPPLY_PRICE")
	private BigDecimal pgRealTaxSupplyPrice; // 실제 총 PG 과세금액
	
	@Column(name = "PG_REAL_TAX_VAT_PRICE")
	private BigDecimal pgRealTaxVatPrice; // 실제 총 PG 부가세
	
	@Column(name = "PG_REAL_TAX_FREE_PRICE")
	private BigDecimal pgRealTaxFreePrice; // 실제 총 PG 면세금액
	
	@Column(name = "BANKDA_MANUAL_NO")
	private String bankdaManualNo; // 뱅크다 번호
	
	@Column(name = "BANKDA_MANUAL_FL")
	private String bankdaManualFl; // 자동입금수동여부
	
	@Column(name = "BANKDA_MANUAL_MANGER_ID")
	private String bankdaManualMangerId; // 자동입금수동처리관리자아이디
	
	@Column(name = "TRACKING_KEY")
	private String trackingKey; // 페이코 쇼핑 트래킹키
	
	@Column(name = "USER_HANDLE_PROCESS")
	private String userHandleProcess; // 주문 환불/반품/교환 자동 승인 처리 여부
	
	@Column(name = "PG_CHARGE_BACK")
	private String pgChargeBack; // 차지백여부
	
	@Column(name = "FB_PIXEL_KEY")
	private String fbPixelKey; // 페이스북 픽셀 쿠키값

	@Column(name = "STATUS")
	private String status;

	@Column(name = "RECEIPT_FL")
	private String receiptFl;

	public BigDecimal getTotalDcCost() {
		return CustomBigDecimalUtils.nullIfZero(this.totalGoodsDcPrice)
				.add(CustomBigDecimalUtils.nullIfZero(this.totalMemberDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalMemberDeliveryDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalCouponGoodsDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalCouponOrderDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalCouponDeliveryDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalEnuriDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.useMileage))
				.add(CustomBigDecimalUtils.nullIfZero(this.useDeposit));
	}

	public BigDecimal getTotalDcExceptDeliveryCost() {
		return CustomBigDecimalUtils.nullIfZero(this.totalGoodsDcPrice)
				.add(CustomBigDecimalUtils.nullIfZero(this.totalMemberDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalCouponGoodsDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalCouponOrderDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.totalEnuriDcPrice))
				.add(CustomBigDecimalUtils.nullIfZero(this.useMileage))
				.add(CustomBigDecimalUtils.nullIfZero(this.useDeposit));
	}

	public BigDecimal getTotalDeliveryCost() {
		return CustomBigDecimalUtils.nullIfZero(this.totalDeliveryCharge)
				.subtract(CustomBigDecimalUtils.nullIfZero(this.totalMemberDeliveryDcPrice))
				.subtract(CustomBigDecimalUtils.nullIfZero(this.totalCouponDeliveryDcPrice));
	}
}
