package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JacksonXmlRootElement(localName = "data")
public class GodoOrderStatusRes {

    @JacksonXmlProperty(localName = "header")
    private GodoResponseHeader header;

    @JacksonXmlProperty(localName = "code")
    private Integer code;

    @JacksonXmlProperty(localName = "msg")
    private String msg;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "orderGoodsData")
    private List<GodoOrderGoodsData> orderGoodsDataList;

    @JacksonXmlProperty(localName = "return")
    private GodoResponseReturnData returnData;

}
