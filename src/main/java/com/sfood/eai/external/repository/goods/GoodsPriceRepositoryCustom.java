package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.GoodsPrice;

import java.util.List;

public interface GoodsPriceRepositoryCustom {

    List<GoodsPrice> findGoodsPrice(String companyCode, String goodsCode);
}
