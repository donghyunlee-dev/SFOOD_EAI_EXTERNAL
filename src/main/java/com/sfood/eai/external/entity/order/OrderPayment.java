package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.entity.id.order.OrderPaymentId;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.util.CustomBigDecimalUtils;
import com.sfood.eai.external.util.CustomStringUtils;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 총 결제 정보
 */
@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@Entity
@Table(name="TBL_ORDER_PAYMENT")
@IdClass(OrderPaymentId.class)
public class OrderPayment extends BaseEntity {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 회사코드
	
	@Id
	@Column(name = "MARKET_MALL_CODE")
	private String marketMallCode; // 연동몰코드(주문 데이터 정의.xlsx : 쇼핑몰명)
	
	@Id
	@Column(name = "ORDER_MERGE_NO")
	private String orderMergeNo ; // 주문통합번호	
	
	@Column(name = "SETTLE_KIND_CODE")
	private String settleKindCode; // 결제방법코드(주문 데이터 정의.xlsx : 결제방법, 사방넷 api 데이터에는 없음)
	
	@Column(name = "TOTAL_ORDER_COST")
	private BigDecimal totalOrderCost; // 총 주문금액(주문 데이터 정의.xlsx : 총 주문금액)

	@Column(name = "TOTAL_DELIVERY_COST")
	private BigDecimal totalDeliveryCost;

	@Column(name = "TOTAL_DC_COST")
	private BigDecimal totalDcCost; // 총 할인금액(주문 데이터 정의.xlsx : 총 할인금액, 사방넷 api 데이터에는 없음)
	
	@Column(name = "TOTAL_GOODS_DC_COST")
	private BigDecimal totalGoodsDcCost; // 총 상품할인금액(주문 데이터 정의.xlsx : 총 할인금액 > 상품할인 총합, 사방넷 api 데이터에는 없음)
	
	@Column(name = "TOTAL_MEMBER_DC_COST")
	private BigDecimal totalMemberDcCost; // 총 회원할인금액(주문 데이터 정의.xlsx : 총 할인금액 > 회원할인 총합, 사방넷 api 데이터에는 없음)

	@Column(name = "TOTAL_MEMBER_DELIVERY_DC_COST")
	private BigDecimal totalMemberDeliveryDcCost; //
	
	@Column(name = "TOTAL_COUPON_GOODS_DC_COST")
	private BigDecimal totalCouponGoodsDcCost; // 총 상품쿠폰할인금액(주문 데이터 정의.xlsx : 총 할인금액 > 쿠폰할인 총합의 일부, 사방넷 api 데이터에는 없음)
	
	@Column(name = "TOTAL_COUPON_ORDER_DC_COST")
	private BigDecimal totalCouponOrderDcCost; // 총 주문쿠폰할인금액(주문 데이터 정의.xlsx : 총 할인금액 > 쿠폰할인 총합의 일부, 사방넷 api 데이터에는 없음)
	
	@Column(name = "TOTAL_COUPON_DELIVERY_DC_COST")
	private BigDecimal totalCouponDeliveryDcCost; // 총 배송쿠폰할인금액(주문 데이터 정의.xlsx : 총 할인금액 > 쿠폰할인 총합의 일부, 사방넷 api 데이터에는 없음)
	
	@Column(name = "TOTAL_USE_MILEAGE")
	private BigDecimal totalUseMileage; // 총 마일리지 사용금액(주문 데이터 정의.xlsx : 마일리지 사용금액, 사방넷 api 데이터에는 없음)

	@Column(name = "TOTAL_USE_DEPOSIT")
	private BigDecimal totalUseDeposit; // 총 예치금 사용금액

	@Column(name = "TOTAL_ENURI_DC_PRICE")
	private BigDecimal totalEnuriDcPrice; // 총 운영자 할인금액
	
	@Column(name = "TOTAL_PAY_COST")
	private BigDecimal totalPayCost; // 총 결제금액(주문 데이터 정의.xlsx : 실 결제금액)
	
	@Column(name = "TOTAL_WON_COST")
	private BigDecimal totalWonCost; // 공급가(주문 데이터 정의.xlsx : 공급가, 고도몰 api 데이터에 안보임, 사방넷 api 공급단가)
	
	@Column(name = "TOTAL_TAX_VAT_COST")
	private BigDecimal totalTaxVatCost; // 부가세(주문 데이터 정의.xlsx : 부가세, 고도몰 api realTaxVatPrice 실제 총 부가세(환불제외), 사방넷 api 데이터에는 없음)

	@Column(name = "PAY_DATE")
	private LocalDateTime payDate; // 결제일시(주문 데이터 정의.xlsx : 결제일시)
	
	@Column(name = "PAYMENT_MSG")
	private String paymentMsg; // 결제 및 erp 관련 메모

	@Transient
	private boolean isChanged;

	public void update(OrderPayment updatePayment) {

		isChanged = false;

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.settleKindCode, updatePayment.getSettleKindCode())) {
			this.settleKindCode = updatePayment.getSettleKindCode();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalOrderCost, updatePayment.getTotalOrderCost())) {
			this.totalOrderCost = updatePayment.getTotalOrderCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalDeliveryCost, updatePayment.getTotalDeliveryCost())) {
			this.totalDeliveryCost = updatePayment.getTotalDeliveryCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalDcCost, updatePayment.getTotalDcCost())) {
			this.totalDcCost = updatePayment.getTotalDcCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalGoodsDcCost, updatePayment.getTotalGoodsDcCost())) {
			this.totalGoodsDcCost = updatePayment.getTotalGoodsDcCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalMemberDcCost, updatePayment.getTotalMemberDcCost())) {
			this.totalMemberDcCost = updatePayment.getTotalMemberDcCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalMemberDeliveryDcCost, updatePayment.getTotalMemberDeliveryDcCost())) {
			this.totalMemberDeliveryDcCost = updatePayment.getTotalMemberDeliveryDcCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalCouponGoodsDcCost, updatePayment.getTotalCouponGoodsDcCost())) {
			this.totalCouponGoodsDcCost = updatePayment.getTotalCouponGoodsDcCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalCouponOrderDcCost, updatePayment.getTotalCouponOrderDcCost())) {
			this.totalCouponOrderDcCost = updatePayment.getTotalCouponOrderDcCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalCouponDeliveryDcCost, updatePayment.getTotalCouponDeliveryDcCost())) {
			this.totalCouponDeliveryDcCost = updatePayment.getTotalCouponDeliveryDcCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalUseMileage, updatePayment.getTotalUseMileage())) {
			this.totalUseMileage = updatePayment.getTotalUseMileage();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalUseDeposit, updatePayment.getTotalUseDeposit())) {
			this.totalUseDeposit = updatePayment.getTotalUseDeposit();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalPayCost, updatePayment.getTotalPayCost())) {
			this.totalPayCost = updatePayment.getTotalPayCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalWonCost, updatePayment.getTotalWonCost())) {
			this.totalWonCost = updatePayment.getTotalWonCost();
			this.isChanged = true;
		}

		if (CustomBigDecimalUtils.notEquals(this.totalTaxVatCost, updatePayment.getTotalTaxVatCost())) {
			this.totalTaxVatCost = updatePayment.getTotalTaxVatCost();
			this.isChanged = true;
		}

		if (!Objects.equals(this.payDate, updatePayment.getPayDate())) {
			this.payDate = updatePayment.getPayDate();
			this.isChanged = true;
		}
	}

	public boolean validate() {
		if (MallOrderType.SABANGNET.code().equals(this.marketMallCode)) {
			return true;
		}

		return StringUtils.hasText(this.settleKindCode);
	}
}
