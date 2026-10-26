package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.entity.id.godo.GodoOrderDeliveryId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@IdClass(GodoOrderDeliveryId.class)
@Entity
@Table(name="TBL_GODO_ORDER_DELIVERY")
public class GodoOrderDelivery extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode; // 법인코드

    @Id
    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode; // 연동몰코드

    @Id
    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "SCM_NO")
    private Integer scmNo;

    @Column(name = "COMMISSION")
    private Integer commission;

    @Column(name = "SCM_ADJUST_NO")
    private String scmAdjustNo;

    @Column(name = "DELIVERY_CHARGE")
    private BigDecimal deliveryCharge;

    @Column(name = "DELIVERY_POLICY_CHARGE")
    private BigDecimal deliveryPolicyCharge;

    @Column(name = "DELIVERY_AREA_CHARGE")
    private BigDecimal deliveryAreaCharge;

    @Column(name = "DELIVERY_FIX_FL")
    private String deliveryFixFl;

    @Column(name = "DELIVERY_COLLECT_FL")
    private String deliveryCollectFl;

    @Column(name = "ORDER_INFO_SNO")
    private Integer orderInfoSno;

    @Id
    @Column(name = "SNO")
    private Integer sno;

    @Column(name = "SCM_ADJUST_AFTER_NO")
    private Integer scmAdjustAfterNo;

    @Column(name = "DIVISION_DELIVERY_USE_DEPOSIT")
    private BigDecimal divisionDeliveryUseDeposit;

    @Column(name = "DIVISION_DELIVERY_USE_MILEAGE")
    private BigDecimal divisionDeliveryUseMileage;

    @Column(name = "DIVISION_DELIVERY_CHARGE")
    private BigDecimal divisionDeliveryCharge;

    @Column(name = "DIVISION_MEMBER_DELIVERY_DC_PRICE")
    private BigDecimal divisionMemberDeliveryDcPrice;

    @Column(name = "DELIVERY_INSURANCE_FEE")
    private BigDecimal deliveryInsuranceFee;

    @Column(name = "DELIVERY_WEIGHT_INFO")
    private String deliveryWeightInfo;

    @Column(name = "OVERSEAS_DELIVERY_POLICY")
    private String overseasDeliveryPolicy;

    @Column(name = "DELIVERY_COLLECT_PRICE")
    private BigDecimal deliveryCollectPrice;

    @Column(name = "DELIVERY_WHOLE_FREE_PRICE")
    private BigDecimal deliveryWholeFreePrice;

    @Column(name = "STATISTICS_ORDER_FL")
    private String statisticsOrderFl;
}
