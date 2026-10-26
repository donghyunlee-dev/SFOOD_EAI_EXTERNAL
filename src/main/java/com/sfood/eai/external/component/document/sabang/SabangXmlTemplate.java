package com.sfood.eai.external.component.document.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sfood.eai.external.component.document.sabang.header.SabangHeader;

public class SabangXmlTemplate<T> {
    @JacksonXmlProperty(localName = "HEADER")
    private final SabangHeader header;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "DATA")
    private final T data;

    public SabangXmlTemplate(SabangHeader header, T data) {
        this.header = header;
        this.data = data;
    }
}
