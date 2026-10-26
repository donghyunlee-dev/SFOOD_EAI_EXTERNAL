package com.sfood.eai.external.service;

import com.sfood.eai.external.dto.PartnerGoodsPrice;
import com.sfood.eai.external.entity.goods.*;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import com.sfood.eai.external.entity.id.goods.GoodsOptionId;
import com.sfood.eai.external.entity.id.goods.MallGoodsMatchId;
import com.sfood.eai.external.entity.order.OrderGoods;

import java.util.List;

public interface OrderGoodsService {

    Goods getGoods(GoodsId id);

    GoodsOption getGoodsOption(GoodsOptionId id);

    GoodsPrice findGoodsPrice(GoodsId id);

    SimpleGoods getSimpleGoods(GoodsId id);

    List<PartnerGoodsPrice> findPartnerGoodsPrices(String companyCode, String partnerCode, String goodsCode);

    List<MallGoodsMatch> getMallOrderGoods(String companyCode, String mallCode, String mallGoodsCode, String optionCode);

    SimpleGoods getGoodsByEdiGoodsCode(String companyCode, String mallCode, String ediGoodsCode, int exchangeFigure);

    SimpleGoods getGoodsByMall(String companyCode, String mallCode, String goodsCode);
}

