package com.sfood.eai.external.component.document.sabang.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sfood.eai.external.component.document.sabang.SabangXmlTemplate;
import com.sfood.eai.external.component.document.sabang.header.SabangInvHeader;
import com.sfood.eai.external.config.MallConfiguration;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@JacksonXmlRootElement(localName = "SABANG_INV_REGI")
public class SabangInvRegister extends SabangXmlTemplate<List<SabangInvRegister.Data>> {

    @Builder
    public SabangInvRegister(MallConfiguration config, List<Data> invoiceData) {
        super(new SabangInvHeader(config), invoiceData);
    }

    @Builder
    @Getter
    public static class Data {
        @JacksonXmlProperty(localName = "SABANGNET_IDX")
        private String sabangnetIdx;    // 주문번호 사방넷

        @JacksonXmlProperty(localName = "TAK_CODE")
        private String takCode;         // 택배사 코드

        @JacksonXmlProperty(localName = "TAK_INVOICE")
        private String takInvoice;      // 송장 번호

        @JacksonXmlProperty(localName = "DELV_HOPE_DATE")
        private String hopeDelvDate;    // 배송희망일자
    }
}
