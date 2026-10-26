package com.sfood.eai.external.component.document.sabang.header;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.Getter;

@Getter
public class SabangHeader {
    @JacksonXmlProperty(localName = "RESULT_TYPE")
    private final String resultType = "XML";

    @JacksonXmlProperty(localName = "SEND_DATE")
    private final String sendDate = CustomDateUtils.now("yyyyMMdd");

    @JacksonXmlProperty(localName = "SEND_COMPAYNY_ID")
    private String sendCompanyId;

    @JacksonXmlProperty(localName = "SEND_AUTH_KEY")
    private String sendAuthKey;

    public SabangHeader(MallConfiguration config) {
        this.sendCompanyId = config.getApiKey("SEND_COMPANY_ID");
        this.sendAuthKey = config.getApiKey("SEND_AUTH_KEY");
    }
}
