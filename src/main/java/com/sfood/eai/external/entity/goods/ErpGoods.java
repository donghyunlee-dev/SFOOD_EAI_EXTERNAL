package com.sfood.eai.external.entity.goods;

import com.sfood.eai.external.entity.id.goods.ErpGoodsId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="TBL_GOODS_ERP")
@IdClass(ErpGoodsId.class)
public class ErpGoods {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode ; // 회사코드

    @Id
    @Column(name = "ERP_GOODS_CODE")
    private String erpGoodsCode; // 품목코드

    @Column(name = "ERP_GOODS_NAME")
    private String erpGoodsName; // 품목명
}
