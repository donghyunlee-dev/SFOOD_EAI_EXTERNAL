package com.sfood.eai.external.dto.sabang;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Getter;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@ToString
@JacksonXmlRootElement(localName = "SABANG_ORDER_LIST")
public class SabangOrderRes {

	@JacksonXmlProperty(localName = "HEADER")
    private SabangHeaderInfo sabangHeaderInfo;

    @JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "DATA")
    private List<SabangOrderData> sabangOrderList;
}
