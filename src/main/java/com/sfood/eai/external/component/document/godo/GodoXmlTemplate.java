package com.sfood.eai.external.component.document.godo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "data")
public final class GodoXmlTemplate<T> {

    @JacksonXmlElementWrapper(useWrapping = false)
    private T data;

    public GodoXmlTemplate(T data) {
        this.data = data;
    }
}
