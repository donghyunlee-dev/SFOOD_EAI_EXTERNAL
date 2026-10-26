package com.sfood.eai.external.component.document.sabang.header;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sfood.eai.external.config.MallConfiguration;
import lombok.Getter;

@Getter
public class SabangInvHeader extends SabangHeader {
    @JacksonXmlProperty(localName = "SEND_INV_EDIT_YN")
    private final String sendInvEditYn = "N";

    public SabangInvHeader(MallConfiguration config) {
        super(config);
    }
}
