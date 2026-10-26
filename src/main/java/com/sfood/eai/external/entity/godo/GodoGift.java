package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.entity.id.godo.GodoGiftId;
import com.sfood.eai.external.entity.order.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(GodoGiftId.class)
@Entity
@Table(name="TBL_GODO_GIFT")
public class GodoGift extends BaseEntity {
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

    @Column(name = "PRESENT_TITLE")
    private String presentTitle;

    @Column(name = "GIFT_NO")
    private String giftNo;

    @Column(name = "GIFT_CD")
    private String giftCd;

    @Column(name = "GIFT_NM")
    private String giftNm;

    @Column(name = "GIFT_CNT")
    private Integer giftCnt;
}
