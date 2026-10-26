package com.sfood.eai.external.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sfood.eai.external.entity.goods.SimpleGoods;
import com.sfood.eai.external.entity.id.order.OrderGoodsId;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.util.CustomStringUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;

@ToString
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="TBL_ORDER_GOODS")
@IdClass(OrderGoodsId.class)
public class OrderGoods extends BaseEntity {
	@Id
	@Column(name = "COMPANY_CODE")
	private String companyCode ; // 회사코드
	
	@Id
	@Column(name = "MARKET_MALL_CODE")
	private String marketMallCode; // 연동몰코드
	
	@Id
	@Column(name = "ORDER_MERGE_NO")
	private String orderMergeNo; // 주문통합번호
	
	@Id
	@Column(name = "ORDER_DETAIL_SEQ")
	private Integer orderDetailSeq; // 주문상세번호
	
	@Column(name = "MARKET_ORDER_NO")
	private String marketOrderNo; // 연동몰 메인주문번호(주문 데이터 정의.xlsx : 마켓주문번호)
	
	@Column(name = "MARKET_ORDER_SUB_NO")
	private String marketOrderSubNo; // 연동몰 주문상세번호

	@Column(name = "ORDER_STATUS_CODE")
	private String orderStatusCode; // 주문상태코드
	
	@Column(name = "SO_NO")
	private String soNo; // 수주번호
	
	@Column(name = "SO_TYPE_CODE")
	private String soTypeCode; // 주문유형( ERP 에서는 수주형태로 쓰임 )
	
	@Column(name = "ORDER_GOODS_CODE")
	private String orderGoodsCode; // OMS 품목코드(주문 데이터 정의.xlsx : OMS 품목코드)
	
	@Column(name = "ORDER_GOODS_NAME")
	private String orderGoodsName; // OMS 품목명
	
	@Column(name = "BUNDLE_YN")
	private String bundleYn; // 묶음품목여부
	
	@Column(name = "ERP_GOODS_CODE")
	private String erpGoodsCode; // ERP품목코드(주문 데이터 정의.xlsx : OMS 품목코드, 주문당시 ERP 품목코드 필요
	
	@Column(name = "ERP_GOODS_NAME")
	private String erpGoodsName; // ERP품목명
	
	@Column(name = "MARKET_GOODS_CODE")
	private String marketGoodsCode; // 마켓상품코드 - 고도몰, 사방넷 혹은 그 연동몰에서 쓰이는 상품코드(주문 데이터 정의.xlsx : 마켓 상품코드&거래처 품목명)
	
	@Column(name = "MARKET_GOODS_NAME")
	private String marketGoodsName; // 마켓상품명

	@Column(name = "MARKET_OPTION_CODE")
	private String marketOptionCode;
	
	@Column(name = "GOODS_WMS_API_YN")
	private String goodsWmsApiYn; // WMS연동여부 - 품목마스터 테이블에 세팅된 값이며 주문수집 혹은 주문등록시 저장, 이 값이 N일 경우 주문데이터를 WMS로 보내지 않는다.(새벽배송 품목 등등)
	
	@Column(name = "FROM_WAREHOUSE_CODE")
	private String fromWarehouseCode; // 창고코드(주문 데이터 정의.xlsx : 창고코드 / 창고명)
	
	@Column(name = "RECV_STOCK_CODE")
	private String recvStockCode; // 납품(직납)창고코드(수기입력시 예외적인 주문(실물없는, 역발행/매출이월 등)의 경우 필요)
	
	@Column(name = "ORDER_CNT")
	private Double orderCnt; // 주문수량(주문 데이터 정의.xlsx : 주문수량)
	
	@Column(name = "ASSIGN_ORDER_CNT")
	private Double assignOrderCnt;// 할당수량
	
	@Column(name = "REAL_DELIVERY_CNT")
	private Double realDeliveryCnt;// 실출고수량
	
	@Column(name = "ISSUE_DELIVERY_CNT")
	private Double issueDeliveryCnt;// 미출수량

	@Column(name = "ISSUE_DELIVERY_REASON_CODE")
	private String issueDeliveryReasonCode;// WMS미출사유코드
	
	@Column(name = "ISSUE_DELIVERY_REASON_MEMO")
	private String issueDeliveryReasonMemo;// WMS미출사유비고
	
	@Column(name = "RETURN_REQ_DATE")
	private LocalDate returnReqDate;// 반품요청일자
	
	@Column(name = "RETURN_REAL_YN")
	private String returnRealYn;// 반품실물여부

	@Column(name = "CANCEL_CNT")
	private Double cancelCnt; // 취소수량

	@Column(name = "CANCEL_REASON_MST_CODE")
	private String cancelReasonMstCode; // 취소 사유 코드

	@Column(name = "CANCEL_REASON_DETAIL_CODE")
	private String cancelReasonDetailCode; // 취소 상세 사유 코드

	@Column(name = "CANCEL_REASON_CONTENT")
	private String cancelReasonContent; // 취소 설명

	@Column(name = "ORIGIN_NAME")
	private String originName;// 원산지명
	
	@Column(name = "SALES_UNIT")
	private String salesUnit;// 판매단위
	
	@Column(name = "SALES_ORDER_PRICE")
	private BigDecimal salesOrderPrice; // 판매가
	
	@Column(name = "SALES_SUPPLY_PRICE")
	private BigDecimal salesSupplyPrice; // 공급가
	
	@Column(name = "SALES_VAT_PRICE")
	private BigDecimal salesVatPrice; // 부가세
	
	@Column(name = "SALES_UNIT_PRICE")
	private BigDecimal salesUnitPrice; // 판매단가
	
	@Column(name = "SALES_SUPPLY_UNIT_PRICE")
	private BigDecimal salesSupplyUnitPrice; // 공급단가
	
	@Column(name = "SALES_VAT_UNIT_PRICE")
	private BigDecimal salesVatUnitPrice; // 단가당부가세

	@Column(name = "DC_COST")
	private BigDecimal dcCost; // 할인금액(총 금액 비중으로 계산)

	@Column(name = "GOODS_DC_COST")
	private BigDecimal goodsDcCost; // 상품할인금액(총 금액 비중으로 계산)

	@Column(name = "MEMBER_DC_COST")
	private BigDecimal memberDcCost; // 회원할인금액(총 금액 비중으로 계산)

	@Column(name = "COUPON_GOODS_DC_COST")
	private BigDecimal couponGoodsDcCost; // 상품쿠폰할인금액(총 금액 비중으로 계산)

	@Column(name = "COUPON_ORDER_DC_COST")
	private BigDecimal couponOrderDcCost; // 주문쿠폰할인금액(총 금액 비중으로 계산)

	@Column(name = "COUPON_DELIVERY_DC_COST")
	private BigDecimal couponDeliveryDcCost; // 배송쿠폰할인금액(총 금액 비중으로 계산)

	@Column(name = "USE_MILEAGE")
	private BigDecimal useMileage; // 마일리지 사용금액(총 금액 비중으로 계산)

	@Column(name = "USE_DEPOSIT")
	private BigDecimal useDeposit; // 사용 예치금

	@Column(name = "ENURI_DC_PRICE")
	private BigDecimal enuriDcPrice;

	@Column(name = "PARENT_ORDER_DETAIL_SEQ")
	private Integer parentOrderDetailSeq; // 상위주문상세번호 - 교환시 신규 주문이 생성될 경우 세팅됨 ( 원주문 상태 : 교한승인 )

	@Column(name = "GOODS_OPTION")
	private String goodsOption;	 // 품목 옵션

	@Column(name = "PART_ORDER_DETAIL_SEQ")
	private Integer partOrderDetailSeq; // 분할 주문 상세 번호

	@Column(name = "PART_REASON_CODE")
	private String partReasonCode; // 분할 사유 코드

	@Column(name = "PART_REASON_CONTENT")
	private String partReasonContent; // 분할 사유 설명

	@Column(name = "E_CENTER_CODE")
	private String eCenterCode; // 센터코드

	@Column(name = "E_CENTER_NAME")
	private String eCenterName; // 센터명

	@Column(name = "ORDER_GOODS_MSG")
	private String orderGoodsMsg; // 주문상품관련메모
	
	@Column(name = "ORDER_WMS_MSG")
	private String orderWmsMsg; // 물류메세지 - 배송메세지가 아닙니다 oms에서 물류에게 보내는 메세지가 있을 것을 대비해 생성해둔 컬럼입니다.

	@Column(name = "MAX_ORDER_CLAIM_SEQ")
	private Integer maxOrderClaimSeq;

	@Column(name = "FOOD_TYPE_CODE")
	private String foodTypeCode; // 식품타입코드(식자재,일반)
	
	@Column(name = "BARCODE")
	private String barcode; // 바코드
	
	@Column(name = "WMS_UPDATE_DATE")
	private Date wmsUpdateDate; // WMS 업데이트 일시

	@Column(name = "GIFT_YN")
	private String giftYn;	// 증정 여부

	@Column(name = "CLAIM_ORDER_DETAIL_SEQ")
	private Integer claimOrderDetailSeq;

	@Column(name = "RECEIPT_YN")
	private String receiptYn;

	@Column(name = "SCHEDULE_CODE")
	private String scheduleCode;	// 정기배송코드

	@Transient
	private BigDecimal originSalesUnitPrice; // 판매단가

	@Transient
	private String mallGoodsCode;

	@Transient
	private String preOrderStatusCode;

	@Transient
	private String subscriptionType;

	@Transient
	private boolean isChanged;

	@Transient
	private boolean isNew = false;

	@Transient
	private boolean isExceptionError = false;

	public void setOrderGoodsId(String orderNo, int seqNo) {
		this.orderMergeNo = orderNo;
		this.orderDetailSeq = seqNo;
		this.isChanged = this.isNew = true;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public double increaseOrderQty() {
		return ++this.orderCnt;
	}

	public void update(SimpleGoods goods) {
		this.orderGoodsCode = goods.getGoodsCode();
		this.orderGoodsName = goods.getGoodsName();
		this.erpGoodsCode = goods.getErpGoodsCode();
		this.erpGoodsName = goods.getErpGoodsName();
		this.goodsWmsApiYn = goods.getWmsApiYn();
		this.salesUnit = goods.getGoodsUnit();
		this.fromWarehouseCode = goods.getWarehouseCode();
		this.bundleYn = goods.getBundleYn();
		this.barcode = goods.getBarcode();
		this.subscriptionType = goods.getSubscriptionType();
		this.assignOrderCnt = this.orderCnt; // availableQty >= this.orderCnt ? this.orderCnt : availableQty;

		// EDI인 경우 면세이면 공급가와 부가세를 다시 계산한다.
		if (MallOrderType.RETAIL.code().equals(this.getMarketMallCode())) {
			if (!"Y".equals(goods.getTaxYn())) {
				this.salesOrderPrice = this.salesSupplyPrice;
				this.salesVatPrice = BigDecimal.ZERO;
				this.salesUnitPrice = this.salesSupplyUnitPrice;
				this.salesVatUnitPrice = BigDecimal.ZERO;
			}
		} else {

			if ("Y".equals(goods.getTaxYn())) {

				if (ObjectUtils.isEmpty(this.salesOrderPrice) || this.salesOrderPrice.compareTo(BigDecimal.ZERO) == 0) {
					return;
				}

				this.salesSupplyPrice = this.salesOrderPrice.divide(BigDecimal.valueOf(1.1), 0, RoundingMode.DOWN);
				this.salesVatPrice = this.salesOrderPrice.subtract(this.salesSupplyPrice);
				this.salesSupplyUnitPrice = this.salesUnitPrice.divide(BigDecimal.valueOf(1.1), 4, RoundingMode.DOWN);
				this.salesVatUnitPrice = this.salesUnitPrice.subtract(this.salesSupplyUnitPrice);
			} else {
				this.salesSupplyPrice = this.salesOrderPrice;
				this.salesVatPrice = BigDecimal.ZERO;
				this.salesSupplyUnitPrice = this.salesUnitPrice;
				this.salesVatUnitPrice = BigDecimal.ZERO;
			}
		}
	}

	public void update(OrderGoods updateGoods) {
		this.isChanged = false;

		this.preOrderStatusCode = this.orderStatusCode;

		if (CustomStringUtils.notEqualsIgnoreNullOrEmpty(this.orderStatusCode, updateGoods.getOrderStatusCode())) {
			this.orderStatusCode = updateGoods.getOrderStatusCode();
		}
	}

	public boolean validate() {
		// 취소된 품목이면 체크하지 않음. (삭제로 자동취소된 경우)
		if (OrderStatus.OS3030.code().equals(this.orderStatusCode)) {
			return true;
		}

		if (!isNew) {
			this.preOrderStatusCode = this.orderStatusCode;
		}

		if (StringUtils.hasText(this.orderGoodsCode) && StringUtils.hasText(this.orderGoodsName)) {
			/*
			* 1. 정기배송 스케쥴이 미등록된 경우 남은 주차에 미등록품목으로 설정 시 오류
			* 2. 정기배송 스케쥴이 미동록된 경우 대표품목으로 설정 시 오류
			* 3. 일반 냉동택배의 품목이 상세품목으로 설정된 경우
			*/
			if (isExceptionError) {
				error();
				return false;
			}

			ready();
			return true;
		}

		error();
		return false;
	}

	public void error() {

		try {
			switch (OrderStatus.valueOf(this.orderStatusCode)) {
				case OrderStatus.OS1020, OrderStatus.OS8030:	// EDI Retail OS1020 신규주문으로 넘어옴
					this.orderStatusCode = OrderStatus.OS8010.code();
					break;
				case OrderStatus.OS8040:
					this.orderStatusCode = OrderStatus.OS8020.code();
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void ready() {

		try {
			switch (OrderStatus.valueOf(this.orderStatusCode)) {
				case OrderStatus.OS8010:
					// EDI Retail 이면 신규주문으로 개별 변경 (confirm을 타지 않아도 된다)
					if (MallOrderType.RETAIL.code().equals(this.marketMallCode)) {
						this.orderStatusCode = OrderStatus.OS1020.code();
					} else {
						this.orderStatusCode = OrderStatus.OS8030.code();
					}
					break;
				case OrderStatus.OS8020:
					this.orderStatusCode = OrderStatus.OS8040.code();
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void confirm() {

		try {
			switch (OrderStatus.valueOf(this.orderStatusCode)) {
				case OrderStatus.OS8010, OrderStatus.OS8030:
					this.orderStatusCode = OrderStatus.OS1020.code();
					break;
				case OrderStatus.OS8020, OrderStatus.OS8040:
					this.orderStatusCode = OrderStatus.OS1030.code();
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void markAsExceptionError() {
		this.isExceptionError = true;
	}

	@JsonIgnore
	public boolean isCancelable() {
		return !(isCanceled() || isShipping());
	}

	@JsonIgnore
	public boolean isCanceled() {
		return this.orderStatusCode.equals(OrderStatus.OS3010.code())
				|| this.orderStatusCode.equals(OrderStatus.OS3020.code())
				|| this.orderStatusCode.equals(OrderStatus.OS3030.code());
	}

	@JsonIgnore
	public boolean isReturning() {
		return  this.orderStatusCode.equals(OrderStatus.OS5010.code())
				||this.orderStatusCode.equals(OrderStatus.OS5020.code())
				|| this.orderStatusCode.equals(OrderStatus.OS5030.code())
				|| this.orderStatusCode.equals(OrderStatus.OS5040.code())
				|| this.orderStatusCode.equals(OrderStatus.OS5050.code());
	}

	@JsonIgnore
	public boolean isExchanging() {
		return this.orderStatusCode.equals(OrderStatus.OS4020.code())
				|| this.orderStatusCode.equals(OrderStatus.OS4030.code())
				|| this.orderStatusCode.equals(OrderStatus.OS4040.code())
				|| this.orderStatusCode.equals(OrderStatus.OS4050.code());
	}

	@JsonIgnore
	public boolean isFinished() {
		return this.orderStatusCode.equals(OrderStatus.OS2040.code());
	}

	@JsonIgnore
	public boolean isShipping() {
		return this.orderStatusCode.equals(OrderStatus.OS2030.code()) || this.orderStatusCode.equals(OrderStatus.OS2040.code());
	}

	@JsonIgnore
	public boolean isStatusChanged() {
		return StringUtils.hasText(this.preOrderStatusCode) ? !this.orderStatusCode.equals(this.preOrderStatusCode) : false;
	}

	@JsonIgnore
	public boolean isConfirmed() {

		if (!StringUtils.hasText(this.preOrderStatusCode)) {
			return false;
		}

		return (this.preOrderStatusCode.equals(OrderStatus.OS1020.code()) && this.orderStatusCode.equals(OrderStatus.OS8040.code()));
	}
}
