package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@JacksonXmlRootElement(localName ="SABANG_RESULT")
public class SabangInvoiceRes {

    @JacksonXmlProperty(localName = "HEADER")
    private SabangHeaderInfo sabangHeaderInfo;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "DATA")
    private List<SabangInvoiceResult> sabangInvoiceResultList;

}
