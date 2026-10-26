package com.sfood.eai.external.component.document.godo.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GodoGoodsMustInfoData {
    @JacksonXmlProperty(isAttribute = true, localName = "idx")
    private String idx;

    @JacksonXmlProperty(localName = "stepData")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GodoStepData> stepData;

    @Setter
    @Getter
    public static class GodoStepData {
        @JacksonXmlProperty(isAttribute = true, localName = "idx")
        private String idx;

        @JacksonXmlProperty(localName = "infoTitle")
        private String infoTitle;

        @JacksonXmlProperty(localName = "infoValue")
        private String infoValue;
    }
}
