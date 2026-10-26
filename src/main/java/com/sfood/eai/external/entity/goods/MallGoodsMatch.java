package com.sfood.eai.external.entity.goods;

import com.sfood.eai.external.entity.id.goods.MallGoodsMatchId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "TBL_MALL_GOODS_MATCH_INFO")
@Entity
public class MallGoodsMatch {

    @EmbeddedId
    private MallGoodsMatchId id;

    @Column(name = "MALL_GOODS_NAME")
    private String mallGoodsName;

    @Column(name = "GOODS_NAME")
    private String goodsName;

    @Column(name = "OPTION_NAME")
    private String optionName;

    @Column(name = "ADD_INFO")
    private String addInfo;

    @Column(name = "DESCRIPTION")
    private String description;
}
