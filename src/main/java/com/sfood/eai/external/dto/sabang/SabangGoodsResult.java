package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SabangGoodsResult {

    @JacksonXmlProperty(localName = "RESULT")
    private String result;

    @JacksonXmlProperty(localName = "MODE")
    private String mode;

    @JacksonXmlProperty(localName = "PRODUCT_ID")
    private String productId;

    @JacksonXmlProperty(localName = "RESULT_MSG")
    private String resultMsg;

    @JacksonXmlProperty(localName = "COMPAYNY_GOODS_CD")
    private String companyGoodsCd;
}