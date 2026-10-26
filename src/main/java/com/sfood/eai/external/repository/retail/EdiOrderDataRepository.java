package com.sfood.eai.external.repository.retail;

import com.sfood.eai.external.entity.id.retail.EdiOrderDataId;
import com.sfood.eai.external.entity.retail.EdiOrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EdiOrderDataRepository extends JpaRepository<EdiOrderData, EdiOrderDataId>, EdiOrderDataRepositoryCustom {
    List<EdiOrderData> findByEdiBpCdAndSoDtBetween(String ediType, String startDate, String endDate);

    @Procedure(procedureName = "PRC_CREATE_EDI_ORDER_DATA_BY_ORDER_DATE")
    String callProcCreateEdiOrderDataByOrderDate(
            @Param("P_COMPANY_ID") String companyId,
            @Param("P_EDI_TYPE") String ediType,
            @Param("P_EDI_BP_TYPE") String ediBpType,
            @Param("P_ORDER_DATE") String orderDate,
            @Param("P_USER_ID") String userId
    );
}
