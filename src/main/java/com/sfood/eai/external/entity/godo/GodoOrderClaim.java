package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.entity.id.godo.GodoOrderClaimId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@IdClass(GodoOrderClaimId.class)
@Entity
@Table(name="TBL_GODO_ORDER_CLAIM")
public class GodoOrderClaim extends BaseEntity {
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
    private Integer sno;

    @Column(name = "BEFORE_STATUS")
    private String beforeStatus;

    @Column(name = "HANDLE_MODE")
    private String handleMode;

    @Column(name = "HANDLE_COMPLETE_FL")
    private String handleCompleteFl;

    @Column(name = "HANDLE_REASON")
    private String handleReason;

    @Column(name = "HANDLE_DETAIL_REASON")
    private String handleDetailReason;

    @Column(name = "HANDLE_DT")
    private String handleDt;

    @Column(name = "REFUND_PRICE")
    private BigDecimal refundPrice;

    @Column(name = "REFUND_USE_DEPOSIT")
    private BigDecimal refundUseDeposit;

    @Column(name = "REFUND_USE_MILEAGE")
    private BigDecimal refundUseMileage;

    @Column(name = "REFUND_DELIVERY_CHARGE")
    private BigDecimal refundDeliveryCharge;

    @Column(name = "REFUND_CHARGE")
    private BigDecimal refundCharge;

    @Column(name = "HANDLE_DETAIL_REASON_SHOW_FL")
    private String handleDetailReasonShowFl;

    @Column(name = "REFUND_DELIVERY_USE_DEPOSIT")
    private BigDecimal refundDeliveryUseDeposit;

    @Column(name = "REFUND_DELIVERY_USE_MILEAGE")
    private BigDecimal refundDeliveryUseMileage;

    @Column(name = "REFUND_DELIVERY_INSURANCE_FEE")
    private BigDecimal refundDeliveryInsuranceFee;

    @Column(name = "REFUND_DELIVERY_COUPON")
    private BigDecimal refundDeliveryCoupon;

    @Column(name = "REFUND_USE_DEPOSIT_COMMISSION")
    private BigDecimal refundUseDepositCommission;

    @Column(name = "REFUND_USE_MILEAGE_COMMISSION")
    private BigDecimal refundUseMileageCommission;

    @Column(name = "HANDLE_GROUP_CD")
    private String handleGroupCd;

    @Column(name = "REG_DT")
    private String regDt;
}
