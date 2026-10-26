package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoAddGoods;
import com.sfood.eai.external.entity.id.godo.GodoAddGoodsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GodoAddGoodsRepository extends JpaRepository <GodoAddGoods, GodoAddGoodsId> {
    List<GodoAddGoods> findByOrderNo(String orderNo);
}
