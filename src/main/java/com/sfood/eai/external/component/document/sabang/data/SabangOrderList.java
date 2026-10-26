package com.sfood.eai.external.component.document.sabang.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sfood.eai.external.component.document.sabang.SabangXmlTemplate;
import com.sfood.eai.external.component.document.sabang.header.SabangHeader;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.sabang.SabangOrderSearchReq;
import lombok.Builder;
import lombok.Getter;

@JacksonXmlRootElement(localName = "SABANG_ORDER_LIST")
public class SabangOrderList extends SabangXmlTemplate<SabangOrderList.Data> {

    @Builder
    public SabangOrderList(MallConfiguration config, SabangOrderSearchReq orderSearch) {
        super(new SabangHeader(config), new Data(orderSearch));
    }

    @Getter
    public static class Data {
        @JacksonXmlProperty(localName = "ORD_FIELD")
        private final String ordField = "IDX|ORDER_ID|MALL_ID|MALL_USER_ID|ORDER_STATUS|USER_ID|USER_NAME|USER_TEL|USER_CEL|USER_EMAIL|RECEIVE_TEL|RECEIVE_CEL|RECEIVE_EMAIL|DELV_MSG|RECEIVE_NAME|RECEIVE_ZIPCODE|RECEIVE_ADDR|TOTAL_COST|PAY_COST|ORDER_DATE|PARTNER_ID|DPARTNER_ID|MALL_PRODUCT_ID|PRODUCT_ID|SKU_ID|P_PRODUCT_NAME|P_SKU_VALUE|PRODUCT_NAME|SALE_COST|MALL_WON_COST|WON_COST|SKU_VALUE|SALE_CNT|DELIVERY_METHOD_STR|DELV_COST|COMPAYNY_GOODS_CD|SKU_ALIAS|BOX_EA|JUNG_CHK_YN|MALL_ORDER_SEQ|MALL_ORDER_ID|ETC_FIELD3|ORDER_GUBUN|P_EA|REG_DATE|ORDER_ETC_1|ORDER_ETC_2|ORDER_ETC_3|ORDER_ETC_4|ORDER_ETC_5|ORDER_ETC_6|ORDER_ETC_7|ORDER_ETC_8|ORDER_ETC_9|ORDER_ETC_10|ORDER_ETC_11|ORDER_ETC_12|ORDER_ETC_13|ORDER_ETC_14|ord_field2|copy_idx|GOODS_NM_PR|GOODS_KEYWORD|ORD_CONFIRM_DATE|RTN_DT|CHNG_DT|DELIVERY_CONFIRM_DATE|CANCEL_DT|CLASS_CD1|CLASS_CD2|CLASS_CD3|CLASS_CD4|BRAND_NM|DELIVERY_ID|INVOICE_NO|HOPE_DELV_DATE|FLD_DSP|INV_SEND_MSG|MODEL_NO|SET_GUBUN|ETC_MSG|DELV_MSG1|MUL_DELV_MSG|BARCODE|INV_SEND_DM|DELIVERY_METHOD_STR2|FREE_GIFT|ACNT_REGS_SRNO|MODEL_NAME";

        @JacksonXmlProperty(localName = "LANG")
        private final String lang = "UTF-8";

        @JacksonXmlProperty(localName = "ORDER_ID")
        private String orderId;

        @JacksonXmlProperty(localName = "ORDER_STATUS")
        private String orderStatus;

        @JacksonXmlProperty(localName = "ORD_ST_DATE")
        private String ordStDate;

        @JacksonXmlProperty(localName = "ORD_ED_DATE")
        private String ordEnDate;

        @JacksonXmlProperty(localName = "MALL_ID")
        private String mallId;

        public Data(SabangOrderSearchReq orderSearch) {
            this.ordStDate = orderSearch.getStartDate().replace("-", "");
            this.ordEnDate = orderSearch.getEndDate().replace("-", "");
            this.mallId = orderSearch.getMallId();
            this.orderId = orderSearch.getOrderNo();
            this.orderStatus = orderSearch.getOrderStatus();
        }
    }
}
