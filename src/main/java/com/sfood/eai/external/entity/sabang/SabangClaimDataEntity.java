package com.sfood.eai.external.entity.sabang;

import com.sfood.eai.external.dto.sabang.SabangClaimSearchReq;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Builder
@Getter
public class SabangClaimDataEntity {

    private SabangClaimSearchReq condition;

    private SabangClaim claim;

    public String getCompanyCode() {
        return condition.getCompanyCode();
    }

    public String getMallCode() {
        return condition.getMallCode();
    }

    public String getCollectDate() {
        if (!StringUtils.hasText(condition.getCollectDate())) {
            return CustomDateUtils.now();
        }
        return condition.getCollectDate();
    }
}
