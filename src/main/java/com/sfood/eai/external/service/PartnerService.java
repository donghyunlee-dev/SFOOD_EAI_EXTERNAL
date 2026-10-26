package com.sfood.eai.external.service;

import com.sfood.eai.external.entity.partner.MallPartnerInfo;
import com.sfood.eai.external.entity.partner.Partner;

import java.time.LocalDateTime;
import java.util.List;

public interface PartnerService {

    Partner getPartner(final String companyCode, final String partnerCode);

    Partner getMallPartner(final String companyCode, final String mallCode, final String mallUserId);

    Partner getNhPartner(String companyCode);

    List<MallPartnerInfo> findMallPartnerList(final String companyCode, final String mallCode);

    LocalDateTime getCloseDateTime(String companyCode, String partnerCode, LocalDateTime closeDate);
}
