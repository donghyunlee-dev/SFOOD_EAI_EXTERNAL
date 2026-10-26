package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@ToString
@JacksonXmlRootElement(localName = "SABANG_ORDER_LIST")
public class SabangClaimRes {
    @JacksonXmlProperty(localName = "HEADER")
    private SabangHeaderInfo sabangHeaderInfo;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "DATA")
    private List<SabangClaimData> sabangOrderList;

}
