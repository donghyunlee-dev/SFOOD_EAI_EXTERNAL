package com.sfood.eai.external.service.impl;

import com.sfood.eai.external.entity.MdmCodeDetail;
import com.sfood.eai.external.entity.id.partner.PartnerId;
import com.sfood.eai.external.entity.partner.MallPartnerInfo;
import com.sfood.eai.external.entity.partner.Partner;
import com.sfood.eai.external.repository.partner.MallPartnerInfoRepository;
import com.sfood.eai.external.repository.partner.PartnerMasterRepository;
import com.sfood.eai.external.service.MdmService;
import com.sfood.eai.external.service.PartnerService;
import com.sfood.eai.external.type.MallOrderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.sfood.eai.external.type.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartnerServiceImpl implements PartnerService {

    private final MallPartnerInfoRepository mallPartnerInfoRepository;
    private final PartnerMasterRepository partnerMasterRepository;
    private final MdmService mdmService;

    @Transactional(readOnly = true)
    @Override
    public Partner getMallPartner(final String companyCode, final String mallCode, final String mallUserId) {

        Partner partner;
        MallPartnerInfo opMallPartner = findMallPartner(companyCode, mallCode, mallUserId).orElseGet(
                () -> findMallPartner(companyCode, mallCode, getDefaultMallUserId(companyCode, mallCode)).orElse(null));

        if (ObjectUtils.isEmpty(opMallPartner)) {
            return null;
        }

        partner = getPartner(companyCode, opMallPartner.getPartnerCode());

        return partner;
    }

    @Transactional(readOnly = true)
    @Override
    public List<MallPartnerInfo> findMallPartnerList(String companyCode, String mallCode) {
        return mallPartnerInfoRepository.findByCompanyCodeAndMallCode(companyCode, mallCode);
    }

    @Transactional(readOnly = true)
    @Override
    public Partner getPartner(String companyCode, String partnerCode) {

        if (!StringUtils.hasText(partnerCode)) {
            return null;
        }

        PartnerId id = new PartnerId(companyCode, partnerCode);
        return partnerMasterRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Partner getNhPartner(String companyCode) {
        List<MdmCodeDetail> edi04Codes = mdmService.getDetailCodes(companyCode, "EDI04");

        if (ObjectUtils.isEmpty(edi04Codes)) {
            return null;
        }

        String orderPartnerCode = edi04Codes
                .stream()
                .filter(detail -> detail.getSubCode1().equals("BUYER"))
                .map(MdmCodeDetail::getMdmDetailCode)
                .findFirst()
                .orElse(null);

        return getPartner(companyCode, orderPartnerCode);
    }

    @Override
    public LocalDateTime getCloseDateTime(String companyCode, String partnerCode, LocalDateTime orderDate) {
        Partner partner = getPartner(companyCode, partnerCode);
        LocalTime localTime = getCloseTime(partner.getOrderCloseTime());
        LocalDateTime closeDateTime = orderDate.toLocalDate().atStartOfDay().plusHours(localTime.getHour()).plusMinutes(localTime.getMinute());
        return closeDateTime;
    }

    private Optional<MallPartnerInfo> findMallPartner(final String companyCode, final String mallCode, final String mallUserId) {
        return mallPartnerInfoRepository.findByCompanyCodeAndMallCodeAndInMallCode(companyCode, mallCode, mallUserId);
    }

    private String getDefaultMallUserId(String companyCode, String mallCode) {
        MallOrderType mallOrderType = MallOrderType.fromByCode(companyCode, mallCode);

        if (MallOrderType.JHONCOOK.equals(mallOrderType)) {
            return GODOMALL_JOHNCOOK_DEFAULT_USER_ID;
        } else if (MallOrderType.SFOOD.equals(mallOrderType)) {
            return GODOMALL_SFOOD_DEFAULT_USER_ID;
        } else if (MallOrderType.MEDISOLA_JHONCOOK.equals(mallOrderType)) {
            return GODOMALL_MEDISOLA_DEFAULT_USER_ID;
        }

        return null;
    }

    private LocalTime getCloseTime(String closeTime) {

        try {
            if (!StringUtils.hasText(closeTime)) {
                return LocalTime.of(17, 0);
            }
            return LocalTime.of(Integer.parseInt(closeTime.substring(0, 2)), Integer.parseInt(closeTime.substring(2, 4)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return LocalTime.of(17, 0);
        }
    }
}
