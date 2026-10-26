package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.GoodsPriceMall;
import com.sfood.eai.external.entity.id.goods.GoodsPriceMallId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsPriceMallRepository extends JpaRepository<GoodsPriceMall, GoodsPriceMallId> {
}
