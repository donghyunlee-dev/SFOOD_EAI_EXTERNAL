package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.dto.godo.GodoOrderGoodsData;
import com.sfood.eai.external.entity.id.godo.GodoOrderGoodsOptionId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@IdClass(GodoOrderGoodsOptionId.class)
@Entity
@Table(name="TBL_GODO_ORDER_GOODS_OPTION")
public class GodoOrderGoodsOption extends BaseEntity {
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

    @Id
    @Column(name = "GOODS_NO")
    private String goodsNo;

    @Id
    @Column(name = "OPTION_SNO")
    private Integer optionSno;

    @Id
    @Column(name = "OPTION_TYPE")
    private String optionType;

    @Id
    @Column(name = "OPTION_NAME")
    private String optionName;

    @Column(name = "OPTION_CODE")
    private String optionCode;

    @Column(name = "OPTION_PRICE")
    private BigDecimal optionPrice;

    @Column(name = "MEMO")
    private String memo;

    public static GodoOrderGoodsOption toEntity(GodoOrder order, GodoOrderGoodsData orderGoods, List<Object> options) {
        GodoOrderGoodsOption instance = new GodoOrderGoodsOption();
        instance.companyCode = order.getCompanyCode();
        instance.marketMallCode = order.getMarketMallCode();
        instance.orderNo = orderGoods.getOrderNo();
        instance.sno = orderGoods.getSno();
        instance.goodsNo = orderGoods.getGoodsNo();
        instance.optionSno = orderGoods.getOptionSno();
        instance.optionType = options.get(0).toString().replace("*", "");
        instance.optionName = options.get(1).toString();
        instance.optionCode = options.get(2) == null ? null : options.get(2).toString();
        instance.optionPrice = options.get(3) == null ? null : BigDecimal.valueOf(orderGoods.getOptionPrice());
        instance.memo = options.get(4) == null ? null : options.get(4).toString();
        instance.setCreateId("external");
        instance.setUpdateId("external");

        return instance;
    }
}
