package com.sfood.eai.external.repository;

import com.sfood.eai.external.entity.Warehouse;
import com.sfood.eai.external.entity.id.WarehouseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, WarehouseId> {

    @Query(value = "SELECT wh FROM Warehouse wh WHERE wh.id.companyCode = :companyCode AND wh.useYn = 'Y' AND wh.returnWarehouseYn = 'Y'")
    List<Warehouse> findReturnWarehouseByCompanyCode(@Param("companyCode") String companyCode);
}
