package com.sfood.eai.external.dto.retail;

import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.type.OrderStatus;
import jakarta.persistence.Transient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper=false)
public class RetailOrderSearchReq extends CommonSearchReq {

    private String inMallCode;

    private String startDate;

    private String endDate;

    @Transient
    private String orderStatus;

    @Transient
    private String mallId;

    public void setInMallCode(String inMallCode) {
        this.inMallCode = inMallCode;
    }

    @Override
    public RetailOrderSearchReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        this.setRequestUri(request.getRequestURI());
        this.setChannelCode(ChannelType.RETAIL.code());
        this.setMallCode(MallOrderType.RETAIL.code());
        this.setCollectDate(getCollectDate().replace("-", ""));

        if (StringUtils.hasText(getCollectDate())) {
            this.startDate = getCollectDate();
            this.endDate = getCollectDate();
        }

        return this;
    }

    public ChannelType getChannelType() {
        return ChannelType.of(this.getChannelCode());
    }
}
