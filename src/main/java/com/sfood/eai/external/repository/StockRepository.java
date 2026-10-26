package com.sfood.eai.external.repository;

import com.sfood.eai.external.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query(nativeQuery = true, value = "SELECT NVL(FN_GET_INV_AV_QTY(:companyCode, :warehouseCode, :goodsCode),0) AS quantity FROM DUAL")
    int getAvailableQuantity(@Param("companyCode") String companyCode, @Param("warehouseCode") String warehouseCode, @Param("goodsCode") String goodsCode);
}
