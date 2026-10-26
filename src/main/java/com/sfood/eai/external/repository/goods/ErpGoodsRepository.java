package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.ErpGoods;
import com.sfood.eai.external.entity.id.goods.ErpGoodsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErpGoodsRepository extends JpaRepository<ErpGoods, ErpGoodsId> {
}
