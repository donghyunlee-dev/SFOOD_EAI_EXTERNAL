package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

@ToString
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JacksonXmlRootElement(localName = "statusData")
public class GodoStatusData {
    @JacksonXmlProperty(isAttribute = true, localName = "idx")
    private String idx;

    @JacksonXmlProperty(localName = "code")
    private Integer code;

    @JacksonXmlProperty(localName = "msg")
    private String msg;

    @JacksonXmlProperty(localName = "orderNo")
    private String orderNo;

    @JacksonXmlProperty(localName = "sno")
    private Integer sno;

    @JacksonXmlProperty(localName = "orderStatus")
    private String orderStatus;
}
