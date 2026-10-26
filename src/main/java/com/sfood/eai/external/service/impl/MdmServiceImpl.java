package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.entity.MdmCodeDetail;
import com.sfood.eai.external.entity.Warehouse;
import com.sfood.eai.external.repository.MdmCodeDetailRepository;
import com.sfood.eai.external.repository.MdmCodeMasterRepository;
import com.sfood.eai.external.repository.WarehouseRepository;
import com.sfood.eai.external.service.MdmService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MdmServiceImpl implements MdmService {

    private final MdmCodeMasterRepository mdmCodeMasterRepository;
    private final MdmCodeDetailRepository mdmCodeDetailRepository;
    private final WarehouseRepository warehouseRepository;

    @Transactional(readOnly = true)
    @Override
    public List<MdmCodeDetail> getDetailCodes(String companyCode, String groupCode) {
        return mdmCodeDetailRepository.findByCompanyCodeAndMdmGroupCode(companyCode, groupCode);
    }

    @Transactional(readOnly = true)
    @Override
    public Warehouse getReturnWarehouse(String companyCode) {
        List<Warehouse> returnWarehousList = warehouseRepository.findReturnWarehouseByCompanyCode(companyCode);

        if (CollectionUtils.isNotEmpty(returnWarehousList)) {
            return returnWarehousList.getFirst();
        } else {
            return null;
        }
    }
}
