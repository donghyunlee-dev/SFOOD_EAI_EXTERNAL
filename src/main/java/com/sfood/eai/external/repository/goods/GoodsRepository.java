package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.dto.PartnerGoodsPrice;
import com.sfood.eai.external.entity.goods.Goods;
import com.sfood.eai.external.entity.id.goods.GoodsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoodsRepository extends JpaRepository <Goods, GoodsId>, GoodsRepositoryCustom {

    Optional<Goods> findById(GoodsId id);

    @Query(value = "SELECT * FROM TABLE(FN_GET_GOODS_PRICE_LIST(:companyCode, :partnerCode, :goodsCode, NULL, SYSDATE))", nativeQuery = true)
	List<PartnerGoodsPrice> findMallGoodsPrice(@Param("companyCode") String companyCode, @Param("partnerCode") String partnerCode, @Param("goodsCode") String goodsCode);
}
