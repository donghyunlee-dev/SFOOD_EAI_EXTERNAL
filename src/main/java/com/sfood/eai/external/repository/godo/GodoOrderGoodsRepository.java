package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoOrderGoods;
import com.sfood.eai.external.entity.id.godo.GodoOrderGoodsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GodoOrderGoodsRepository extends JpaRepository <GodoOrderGoods, GodoOrderGoodsId> {
    List<GodoOrderGoods> findByOrderNo(String orderNo);
}
