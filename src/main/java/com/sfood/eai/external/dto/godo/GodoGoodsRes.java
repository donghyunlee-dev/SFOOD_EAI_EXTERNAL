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
public class GodoGoodsRes {

    @JacksonXmlProperty(localName = "header")
    private GodoResponseHeader header;

    @JacksonXmlProperty(localName = "return")
    private ReturnData returnData;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ReturnData
    {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "goods_data")
        List<GoodsData> goodsData;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GoodsData {

        @JacksonXmlProperty(localName = "code")
        private String resultCode;

        @JacksonXmlProperty(localName = "idx")
        private String idx;

        @JacksonXmlProperty(localName = "msg")
        private String message;

        @JacksonXmlProperty(localName = "data")
        private Data data;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Data {
        @JacksonXmlProperty(localName = "goodsno")
        private String goodsNo;

        @JacksonXmlProperty(localName = "goodscd")
        private String goodsCd;
    }
}
