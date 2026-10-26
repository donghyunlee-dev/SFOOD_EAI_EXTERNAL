package com.sfood.eai.external.entity.order;

import com.sfood.eai.external.entity.goods.SimpleGoods;
import com.sfood.eai.external.entity.id.order.OrderAddGoodsId;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.type.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Data
@IdClass(OrderAddGoodsId.class)
@Entity
@Table(name="TBL_ORDER_ADD_GOODS")
public class OrderAddGoods extends BaseEntity {
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
	
	@Column(name = "GOODS_WMS_API_YN")
	private String goodsWmsApiYn; // WMS연동여부 - 품목마스터 테이블에 세팅된 값이며 주문수집 혹은 주문등록시 저장, 이 값이 N일 경우 주문데이터를 WMS로 보내지 않는다.(새벽배송 품목 등등)
	
	@Column(name = "FROM_WAREHOUSE_CODE")
	private String fromWarehouseCode; // 창고코드(주문 데이터 정의.xlsx : 창고코드 / 창고명)
	
	@Column(name = "RECV_STOCK_CODE")
	private String recvStockCode; // 납품(직납)창고코드(수기입력시 예외적인 주문(실물없는, 역발행/매출이월 등)의 경우 필요)
	
	@Column(name = "ORDER_CNT")
	private Integer orderCnt; // 주문수량(주문 데이터 정의.xlsx : 주문수량)
	
	@Column(name = "ASSIGN_ORDER_CNT")
	private Integer assignOrderCnt;// 할당수량
	
	@Column(name = "REAL_DELIVERY_CNT")
	private Integer realDeliveryCnt;// 실출고수량
	
	@Column(name = "ISSUE_DELIVERY_CNT")
	private Integer issueDeliveryCnt;// 미출수량
	
	@Column(name = "ISSUE_DELIVERY_REASON_CODE")
	private String issueDeliveryReasonCode;// WMS미출사유코드
	
	@Column(name = "ISSUE_DELIVERY_REASON_MEMO")
	private String issueDeliveryReasonMemo;// WMS미출사유비고
	
	@Column(name = "RETURN_REQ_DATE")
	private Date returnReqDate;// 반품요청일자
	
	@Column(name = "RETURN_REAL_YN")
	private String returnRealYn;// 반품실물여부
	
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
	private BigDecimal couponGoodsDcPrice; // 상품쿠폰할인금액(총 금액 비중으로 계산)

	@Column(name = "COUPON_ORDER_DC_COST")
	private BigDecimal couponOrderDcPrice; // 주문쿠폰할인금액(총 금액 비중으로 계산)

	@Column(name = "COUPON_DELIVERY_DC_COST")
	private BigDecimal couponDeliveryDcPrice; // 배송쿠폰할인금액(총 금액 비중으로 계산)

	@Column(name = "USE_MILEAGE")
	private BigDecimal useMileage; // 마일리지 사용금액(총 금액 비중으로 계산)

	@Column(name = "USE_DEPOSIT")
	private BigDecimal useDeposit; // 사용 예치금

	@Column(name = "ENURI_DC_PRICE")
	private BigDecimal enuriDcPrice;

	@Column(name = "PARENT_ORDER_DETAIL_SEQ")
	private Integer parentOrderDetailSeq; // 상위주문상세번호 - 교환시 신규 주문이 생성될 경우 세팅됨 ( 원주문 상태 : 교한승인 )

	@Column(name = "GOODS_OPTION")
	private String goodsOption;
	
	@Column(name = "ORDER_GOODS_MSG")
	private String orderGoodsMsg; // 주문상품관련메모
	
	@Column(name = "ORDER_WMS_MSG")
	private String orderWmsMsg; // 물류메세지 - 배송메세지가 아닙니다 oms에서 물류에게 보내는 메세지가 있을 것을 대비해 생성해둔 컬럼입니다.

	@Column(name = "FOOD_TYPE_CODE")
	private String foodTypeCode; // 식품타입코드(식자재,일반)
	
	@Column(name = "BARCODE")
	private String barcode; // 바코드
	
	@Column(name = "WMS_UPDATE_DATE")
	private Date wmsUpdateDate; // WMS 업데이트 일시

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		OrderAddGoods that = (OrderAddGoods) o;
		return Objects.equals(companyCode, that.companyCode)
				&& Objects.equals(marketMallCode, that.marketMallCode)
				&& Objects.equals(orderMergeNo, that.orderMergeNo)
				&& Objects.equals(orderDetailSeq, that.orderDetailSeq)
				&& Objects.equals(marketOrderNo, that.marketOrderNo)
				&& Objects.equals(marketOrderSubNo, that.marketOrderSubNo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(companyCode, marketMallCode, orderMergeNo, orderDetailSeq, marketOrderNo, marketOrderSubNo);
	}

	public int increaseOrderQty() {
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
	}

	public int validate() {
		if (StringUtils.hasText(this.orderGoodsCode)
				&& StringUtils.hasText(this.orderGoodsName)
				&& StringUtils.hasText(this.erpGoodsCode)
				&& StringUtils.hasText(this.erpGoodsName)) {
			ready();
			return 0;
		}

		error();
		return 1;
	}

	public void confirm() {

		try {
			switch (OrderStatus.valueOf(this.orderStatusCode)) {
				case OrderStatus.OS8010:
				case OrderStatus.OS8030:
					this.orderStatusCode = OrderStatus.OS1020.code();
					break;
				case OrderStatus.OS8020:
				case OrderStatus.OS8040:
					this.orderStatusCode = OrderStatus.OS1030.code();
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void error() {

		try {
			switch (OrderStatus.valueOf(this.orderStatusCode)) {
				case OrderStatus.OS8040:
					this.orderStatusCode = OrderStatus.OS8020.code();
					break;
				case OrderStatus.OS8030:
					this.orderStatusCode = OrderStatus.OS8010.code();
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void ready() {

		try {
			switch (OrderStatus.valueOf(this.orderStatusCode)) {
				case OrderStatus.OS8020:
					this.orderStatusCode = OrderStatus.OS8040.code();
					break;
				case OrderStatus.OS8010:
					this.orderStatusCode = OrderStatus.OS8030.code();
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
