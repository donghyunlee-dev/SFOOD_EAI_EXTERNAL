package com.sfood.eai.external.entity.retail;

import com.sfood.eai.external.dto.retail.RetailOrderSearchReq;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;

@Builder
@Getter
public class RetailDataEntity {

    private RetailOrderSearchReq condition;

    private List<EdiOrderData> orders;

    public String getCompanyCode() {
        return condition.getCompanyCode();
    }

    public String getMallCode() {
        return condition.getMallCode();
    }

    public String getCollectDate() {
        if (!StringUtils.hasText(condition.getCollectDate())) {
            return CustomDateUtils.now().replace("-", "");
        }
        return condition.getCollectDate();
    }
}
