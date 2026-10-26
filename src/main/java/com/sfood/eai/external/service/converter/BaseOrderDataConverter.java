package com.sfood.eai.external.service.converter;

import com.sfood.eai.external.entity.goods.Goods;
import com.sfood.eai.external.entity.goods.GoodsOption;
import com.sfood.eai.external.entity.goods.GoodsPrice;
import com.sfood.eai.external.entity.goods.MallGoodsMatch;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import com.sfood.eai.external.entity.id.goods.GoodsOptionId;
import com.sfood.eai.external.entity.order.OrderGoods;
import com.sfood.eai.external.service.OrderGoodsService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class BaseOrderDataConverter {

    private OrderGoodsService orderGoodsService;

    protected Goods getGoods(GoodsId id) {
        return orderGoodsService.getGoods(id);
    }

    protected GoodsOption getGoodsOption(GoodsOptionId id) {
        return orderGoodsService.getGoodsOption(id);
    }

    protected GoodsPrice getGoodsPrice(GoodsId id) {
        return orderGoodsService.findGoodsPrice(id);
    }

    protected List<MallGoodsMatch> getMallGoods(String companyCode, String mallCode, String mallGoodsCode, String optionCode) {
        return orderGoodsService.getMallOrderGoods(companyCode, mallCode, mallGoodsCode, optionCode);
    }
}
