package com.sfood.eai.external.repository.sabang;

import com.sfood.eai.external.entity.sabang.SabangGoods;
import com.sfood.eai.external.entity.id.sabang.SabangGoodsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SabangGoodsRepository extends JpaRepository<SabangGoods, SabangGoodsId> {
}
