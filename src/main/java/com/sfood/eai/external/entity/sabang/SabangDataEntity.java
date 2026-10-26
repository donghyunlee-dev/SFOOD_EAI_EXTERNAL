package com.sfood.eai.external.entity.sabang;

import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.List;

@ToString
@Builder
@Getter
public class SabangDataEntity {

    private SabangOrderSearchReq condition;

    private List<SabangOrder> orders;

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
