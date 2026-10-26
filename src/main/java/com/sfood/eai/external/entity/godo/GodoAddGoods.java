package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.entity.id.godo.GodoAddGoodsId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(GodoAddGoodsId.class)
@Table(name="TBL_GODO_ADD_GOODS")
public class GodoAddGoods extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode; // 법인코드

    @Id
    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode; // 연동몰코드

    @Id
    @Column(name = "ORDER_NO")
    private String orderNo;

    @Id
    @Column(name = "SNO")
    private String sno;

    @Column(name = "MALL_SNO")
    private Integer mallSno;

    @Column(name = "API_ORDER_GOODS_NO")
    private String apiOrderGoodsNo;

    @Column(name = "ORDER_CD")
    private Integer orderCd;

    @Column(name = "ORDER_GROUP_CD")
    private String orderGroupCd;

    @Column(name = "EVENT_SNO")
    private Integer eventSno;

    @Column(name = "ORDER_STATUS")
    private String orderStatus;

    @Column(name = "ORDER_DELIVERY_SNO")
    private Integer orderDeliverySno;

    @Column(name = "INVOICE_COMPANY_SNO")
    private Integer invoiceCompanySno;

    @Column(name = "INVOICE_COMPANY")
    private String invoiceCompany;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "SCM_NO")
    private Integer scmNo;

    @Column(name = "PURCHASE_NO")
    private Integer purchaseNo;

    @Column(name = "COMMISSION")
    private Double commission;

    @Column(name = "SCM_ADJUST_AFTER_NO")
    private Integer scmAdjustAfterNo;

    @Column(name = "GOODS_TYPE")
    private String goodsType;

    @Column(name = "TIME_SALE_FL")
    private String timeSaleFl;

    @Column(name = "PARENT_MUST_FL")
    private String parentMustFl;

    @Column(name = "PARENT_GOODS_NO")
    private String parentGoodsNo;

    @Column(name = "LIST_IMAGE_DATA")
    private String listImageData;

    @Column(name = "GOODS_CD")
    private String goodsCd;

    @Column(name = "GOODS_MODEL_NO")
    private String goodsModelNo;

    @Column(name = "GOODS_NM")
    private String goodsNm;

    @Column(name = "GOODS_NM_STANDARD")
    private String goodsNmStandard;

    @Column(name = "GOODS_CNT")
    private Integer goodsCnt;

    @Column(name = "GOODS_PRICE")
    private BigDecimal goodsPrice;

    @Column(name = "DIVISION_USE_DEPOSIT")
    private BigDecimal divisionUseDeposit;

    @Column(name = "DIVISION_USE_MILEAGE")
    private BigDecimal divisionUseMileage;

    @Column(name = "DIVISION_GOODS_DELIVERY_USE_DEPOSIT")
    private BigDecimal divisionGoodsDeliveryUseDeposit;

    @Column(name = "DIVISION_GOODS_DELIVERY_USE_MILEAGE")
    private BigDecimal divisionGoodsDeliveryUseMileage;

    @Column(name = "DIVISION_COUPON_ORDER_DC_PRICE")
    private BigDecimal divisionCouponOrderDcPrice;

    @Column(name = "DIVISION_COUPON_ORDER_MILEAGE")
    private BigDecimal divisionCouponOrderMileage;

    @Column(name = "ADD_GOODS_PRICE")
    private BigDecimal addGoodsPrice;

    @Column(name = "OPTION_PRICE")
    private BigDecimal optionPrice;

    @Column(name = "OPTION_COST_PRICE")
    private BigDecimal optionCostPrice;

    @Column(name = "OPTION_TEXT_PRICE")
    private BigDecimal optionTextPrice;

    @Column(name = "FIXED_PRICE")
    private BigDecimal fixedPrice;

    @Column(name = "COST_PRICE")
    private BigDecimal costPrice;

    @Column(name = "GOODS_DC_PRICE")
    private BigDecimal goodsDcPrice;

    @Column(name = "MEMBER_DC_PRICE")
    private BigDecimal memberDcPrice;

    @Column(name = "MEMBER_OVERLAP_DC_PRICE")
    private BigDecimal memberOverlapDcPrice;

    @Column(name = "COUPON_GOODS_DC_PRICE")
    private BigDecimal couponGoodsDcPrice;

    @Column(name = "TIME_SALE_PRICE")
    private BigDecimal timeSalePrice;

    @Column(name = "BRAND_BANK_SALE_PRICE")
    private BigDecimal brandBankSalePrice;

    @Column(name = "MYAPP_DC_PRICE")
    private BigDecimal myappDcPrice;

    @Column(name = "GOODS_DELIVERY_COLLECT_PRICE")
    private BigDecimal goodsDeliveryCollectPrice;

    @Column(name = "GOODS_MILEAGE")
    private BigDecimal goodsMileage;

    @Column(name = "MEMBER_MILEAGE")
    private BigDecimal memberMileage;

    @Column(name = "COUPON_GOODS_MILEAGE")
    private BigDecimal couponGoodsMileage;

    @Column(name = "GOODS_DELIVERY_COLLECT_FL")
    private String goodsDeliveryCollectFl;

    @Column(name = "MINUS_DEPOSIT_FL")
    private String minusDepositFl;

    @Column(name = "MINUS_RESTORE_DEPOSIT_FL")
    private String minusRestoreDepositFl;

    @Column(name = "MINUS_MILEAGE_FL")
    private String minusMileageFl;

    @Column(name = "MINUS_RESTORE_MILEAGE_FL")
    private String minusRestoreMileageFl;

    @Column(name = "PLUS_MILEAGE_FL")
    private String plusMileageFl;

    @Column(name = "PLUS_RESTORE_MILEAGE_FL")
    private String plusRestoreMileageFl;

    @Column(name = "MINUS_STOCK_FL")
    private String minusStockFl;

    @Column(name = "MINUS_RESTORE_STOCK_FL")
    private String minusRestoreStockFl;

    @Column(name = "OPTION_SNO")
    private Integer optionSno;

    @Column(name = "OPTION_INFO")
    private String optionInfo;

    @Column(name = "OPTION_TEXT_INFO")
    private String optionTextInfo;

    @Column(name = "CATE_ALL_CD")
    private String cateAllCd;

    @Column(name = "HSCODE")
    private String hscode;

    @Column(name = "CANCEL_DT")
    private String cancelDt;

    @Column(name = "PAYMENT_DT")
    private String paymentDt;

    @Column(name = "INVOICE_DT")
    private String invoiceDt;

    @Column(name = "DELIVERY_DT")
    private String deliveryDt;

    @Column(name = "DELIVERY_COMPLETE_DT")
    private String deliveryCompleteDt;

    @Column(name = "FINISH_DT")
    private String finishDt;

    @Column(name = "MILEAGE_GIVE_DT")
    private String mileageGiveDt;

    @Column(name = "STATISTICS_ORDER_FL")
    private String statisticsOrderFl;

    @Column(name = "STATISTICS_GOODS_FL")
    private String statisticsGoodsFl;

    @Column(name = "SEND_SMS_FL")
    private String sendSmsFl;

    @Column(name = "DELIVERY_METHOD_FL")
    private String deliveryMethodFl;

    @Column(name = "ENURI")
    private BigDecimal enuri;

    @Column(name = "GOODS_DISCOUNT_INFO")
    private String goodsDiscountInfo;

    @Column(name = "GOODS_MILEAGE_ADD_INFO")
    private String goodsMileageAddInfo;

    @Column(name = "INFLOW")
    private String inflow;

    @Column(name = "LINK_MAIN_THEME")
    private String linkMainTheme;

    @Column(name = "VISIT_ADDRESS")
    private String visitAddress;

    @Column(name = "GOODS_VOLUME")
    private BigDecimal goodsVolume;

    @Column(name = "COUPON_MILEAGE_FL")
    private String couponMileageFl;

    @Column(name = "EASYPAY_SCM_RECEIPT_FL")
    private String easypayScmReceiptFl;

    @Column(name = "DELIVERY_SCHEDULE_FL")
    private String deliveryScheduleFl;

    @Column(name = "ADD_GOODS_NO")
    private String addGoodsNo;

    @Column(name = "ASSIGN_GOODS_CNT")
    private Integer assignGoodsCnt;

    public Integer decreaseAssignQty() {
        return --this.assignGoodsCnt;
    }
}
