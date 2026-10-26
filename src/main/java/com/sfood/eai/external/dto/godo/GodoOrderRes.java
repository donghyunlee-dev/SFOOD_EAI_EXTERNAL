package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "data")
public class GodoOrderRes {

    @JacksonXmlProperty(localName = "header")
    private GodoResponseHeader header;

	@JacksonXmlProperty(localName = "return")
    private GodoReturnOrder godoReturnOrder = new GodoReturnOrder();

}
