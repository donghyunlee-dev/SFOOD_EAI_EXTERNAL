package com.sfood.eai.external.component.document.godo.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GodoTextOptionData {
    @JacksonXmlProperty(isAttribute = true, localName = "idx")
    private String idx;

    @JacksonXmlProperty(localName = "optionName")
    private String optionName;

    @JacksonXmlProperty(localName = "mustFl")
    private String mustFl;
}
