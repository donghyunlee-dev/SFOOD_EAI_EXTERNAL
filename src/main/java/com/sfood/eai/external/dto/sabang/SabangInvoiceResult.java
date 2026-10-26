package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SabangInvoiceResult {

    @JacksonXmlProperty(localName = "RESULT")
    private String result;
    @JacksonXmlProperty(localName = "SABANGNET_IDX")
    private String sabangnetIdx;
    @JacksonXmlProperty(localName = "RESULT_MSG")
    private String resultMsg;

    public SabangInvoiceResult(String result, String resultMsg) {
        this.result = result;
        this.resultMsg = resultMsg;
    }
}
