package com.sfood.eai.external.service;

import com.sfood.eai.external.entity.MdmCodeDetail;
import com.sfood.eai.external.entity.Warehouse;

import java.util.List;

public interface MdmService {

    List<MdmCodeDetail> getDetailCodes(String companyCode, String groupCode);

     Warehouse getReturnWarehouse(String companyCode);
}
