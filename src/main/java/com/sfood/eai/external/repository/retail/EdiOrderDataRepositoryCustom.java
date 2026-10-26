package com.sfood.eai.external.repository.retail;

import com.sfood.eai.external.dto.order.ErrorOrderUpdateReq;
import com.sfood.eai.external.entity.retail.EdiOrderData;

import java.util.List;
import java.util.Optional;

public interface EdiOrderDataRepositoryCustom {

    List<String> getRetailPartners(String startDate, String endDate);

    List<EdiOrderData> findOrderIdsNullBy(ErrorOrderUpdateReq request);

    List<EdiOrderData> findOrderByPartnerGroupKey(EdiOrderData.BranchGroupKey groupKey);
}
