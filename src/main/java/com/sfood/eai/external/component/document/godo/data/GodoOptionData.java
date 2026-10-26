package com.sfood.eai.external.component.document.godo.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GodoOptionData {
    @JacksonXmlProperty(isAttribute = true, localName = "idx")
    private String idx;

    @JacksonXmlProperty(localName = "optionNo")
    private String optionNo;

    @JacksonXmlProperty(localName = "optionValue1")
    private String optionValue1;

    @JacksonXmlProperty(localName = "optionPrice")
    private String optionPrice;

    @JacksonXmlProperty(localName = "optionViewFl")
    private String optionViewFl;

    @JacksonXmlProperty(localName = "optionSellFl")
    private String optionSellFl;

    @JacksonXmlProperty(localName = "stockCnt")
    private Integer stockCnt;
}