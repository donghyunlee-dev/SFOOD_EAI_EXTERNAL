package com.sfood.eai.external.component.document.godo.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JacksonXmlRootElement(localName = "statusData")
public class GodoOrderStatusData {
    @JacksonXmlProperty(isAttribute = true, localName = "idx")
    private String idx;

    @JacksonXmlProperty(localName = "orderNo")
    private String orderNo;

    @JacksonXmlProperty(localName = "sno")
    private Integer sno;

    @JacksonXmlProperty(localName = "orderStatus")
    private String orderStatus;

    @JacksonXmlProperty(localName = "invoiceNo")
    private String invoiceNo;

    @JacksonXmlProperty(localName = "invoiceCompanySno")
    private Integer invoiceCompanySno;
}
