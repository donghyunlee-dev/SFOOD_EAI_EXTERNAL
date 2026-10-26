package com.sfood.eai.external.repository.godo;

import com.sfood.eai.external.entity.godo.GodoOrderGoodsOption;
import com.sfood.eai.external.entity.id.godo.GodoOrderGoodsOptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GodoOrderGoodsOptionRepository extends JpaRepository<GodoOrderGoodsOption, GodoOrderGoodsOptionId> {
    List<GodoOrderGoodsOption> findByOrderNo(String orderNo);
}
