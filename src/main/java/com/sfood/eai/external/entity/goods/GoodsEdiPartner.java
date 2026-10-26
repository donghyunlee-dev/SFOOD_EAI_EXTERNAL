package com.sfood.eai.external.entity.goods;


import com.sfood.eai.external.entity.id.goods.GoodsEdiPartnerId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="TBL_GOODS_EDI_PARTNER")
@IdClass(GoodsEdiPartnerId.class)
public class GoodsEdiPartner {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "MALL_CODE")
    private String mallCode;

    @Id
    @Column(name = "PARTNER_GOODS_CODE")
    private String partnerGoodsCode;

    @Column(name = "PARTNER_GOODS_NAME")
    private String partnerGoodsName;

    @Id
    @Column(name = "GOODS_CODE")
    private String goodsCode;

    @Column(name = "EXCHANGE_FIGURE")
    private Integer exchangeFigure;

    @Column(name = "SALES_TYPE")
    private String salesType;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "UPDATE_ID")
    private String updateId;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}
