package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoGoods;
import com.sfood.eai.external.entity.id.godo.GodoGoodsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GodoGoodsRepository extends JpaRepository<GodoGoods, GodoGoodsId> {
}
