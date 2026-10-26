package com.sfood.eai.external.dto.sabang;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SabangHeaderInfo {

	@JacksonXmlProperty(localName = "SEND_COMPAYNY_ID")
	private String sendCompanyId;

	@JacksonXmlProperty(localName = "SEND_DATE")
	private String sendDate;

	@JacksonXmlProperty(localName = "TOTAL_COUNT")
	private String totalCount;

}