package com.sfood.eai.external.dto.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GodoResponseReturnData {

    @JacksonXmlProperty(localName = "success")
    private ReturnData success;

    @JacksonXmlProperty(localName = "fail")
    private ReturnData fail;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "statusData")
    private List<GodoStatusData> statusData;

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReturnData {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "statusData")
        private List<GodoStatusData> statusData;
    }
}

