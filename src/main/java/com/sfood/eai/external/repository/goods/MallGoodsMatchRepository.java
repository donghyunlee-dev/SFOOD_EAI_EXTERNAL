package com.sfood.eai.external.repository.goods;

import com.sfood.eai.external.entity.goods.MallGoodsMatch;
import com.sfood.eai.external.entity.id.goods.MallGoodsMatchId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MallGoodsMatchRepository extends JpaRepository<MallGoodsMatch, MallGoodsMatchId> {

    @Query("SELECT a FROM MallGoodsMatch a where a.id.companyCode = :companyCode and a.id.mallCode = :mallCode and a.id.mallGoodsCode = :mallGoodsCode and a.id.optionCode = :optionCode")
    List<MallGoodsMatch> findByMallGoods(@Param("companyCode") String companyCode, @Param("mallCode") String mallCode, @Param("mallGoodsCode") String mallGoodsCode, @Param("optionCode") String optionCode);
}
