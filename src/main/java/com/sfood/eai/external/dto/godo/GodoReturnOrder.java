package com.sfood.eai.external.dto.godo;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public class GodoReturnOrder {

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "order_data")
	private List<GodoOrderData> orderDataList;
	
}
