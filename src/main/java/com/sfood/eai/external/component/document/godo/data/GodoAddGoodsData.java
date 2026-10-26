package com.sfood.eai.external.component.document.godo.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GodoAddGoodsData {
    @JacksonXmlProperty(isAttribute = true, localName = "idx")
    private String idx;

    @JacksonXmlProperty(localName = "title")
    private String title;       // 추가옵션명

    @JacksonXmlProperty(localName = "mustFl")
    private String mustFl;      // 필수여부 (y = 필수, n = 비필수)

    @JacksonXmlProperty(localName = "goodsNoData")
    private String goodsNoData; // 추가상품번호
}
