package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.GoodsOption;
import com.sfood.eai.external.entity.id.goods.GoodsOptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsOptionRepository extends JpaRepository<GoodsOption, GoodsOptionId> {
}
