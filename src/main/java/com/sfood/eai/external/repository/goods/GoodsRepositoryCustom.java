package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.SimpleGoods;
import com.sfood.eai.external.entity.id.goods.GoodsId;

import java.util.List;

public interface GoodsRepositoryCustom {
    List<SimpleGoods> getGoodsByMall(String companyCode, String mallCode, String goodsCode);

    SimpleGoods getSimpleGoods(GoodsId goodsId);
}
