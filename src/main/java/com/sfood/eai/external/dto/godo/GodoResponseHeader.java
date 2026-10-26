package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.*;

@ToString
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GodoResponseHeader {

    @JacksonXmlProperty(localName = "code")
    private String code;

    @JacksonXmlProperty(localName = "msg")
    private String msg;
}
