package com.sfood.eai.external.component.document.godo.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JacksonXmlRootElement(localName = "GoodsData")
public class GodoGoodsData {
    @JacksonXmlProperty(isAttribute = true)
    private String idx;

    @JacksonXmlProperty(localName = "goodsNo")
    private String goodsNo;

    @JacksonXmlProperty(localName = "goodsNmFl")
    private String goodsNmFl;

    @JacksonXmlProperty(localName = "goodsNm")
    private String goodsNm;

    @JacksonXmlProperty(localName = "goodsDisplayFl")
    private String goodsDisplayFl;

    @JacksonXmlProperty(localName = "goodsDisplayMobileFl")
    private String goodsDisplayMobileFl;

    @JacksonXmlProperty(localName = "goodsSellFl")
    private String goodsSellFl;

    @JacksonXmlProperty(localName = "goodsSellMobileFl")
    private String goodsSellMobileFl;

    @JacksonXmlProperty(localName = "scmNo")
    private String scmNo;

    @JacksonXmlProperty(localName = "goodsCd")
    private String goodsCd;

    @JacksonXmlProperty(localName = "cateCd")
    private String cateCd;

    @JacksonXmlProperty(localName = "goodsSearchWord")
    private String goodsSearchWord;

    @JacksonXmlProperty(localName = "goodsState")
    private String goodsState;

    @JacksonXmlProperty(localName = "makerNm")
    private String makerNm;

    @JacksonXmlProperty(localName = "originNm")
    private String originNm;

    @JacksonXmlProperty(localName = "goodsPermission")
    private String goodsPermission;

    @JacksonXmlProperty(localName = "taxFreeFl")
    private String taxFreeFl;

    @JacksonXmlProperty(localName = "stockFl")
    private String stockFl;

    @JacksonXmlProperty(localName = "soldOutFl")
    private String soldOutFl;

    @JacksonXmlProperty(localName = "imageStorage")
    private String imageStorage;

    @JacksonXmlProperty(localName = "restockFl")
    private String restockFl;

    @JacksonXmlProperty(localName = "mileageFl")
    private String mileageFl;

    @JacksonXmlProperty(localName = "goodsDiscountFl")
    private String goodsDiscountFl;

    @JacksonXmlProperty(localName = "goodsPrice")
    private Integer goodsPrice;

    @JacksonXmlProperty(localName = "costPrice")
    private Integer costPrice;

    @JacksonXmlProperty(localName = "fixedPrice")
    private Integer fixedPrice;

    @JacksonXmlProperty(localName = "payLimitFl")
    private String payLimitFl;

    @JacksonXmlProperty(localName = "optionFl")
    private String optionFl;

    @JacksonXmlProperty(localName = "optionTextFl")
    private String optionTextFl;

    @JacksonXmlProperty(localName = "addGoodsFl")
    private String addGoodsFl;

    @JacksonXmlProperty(localName = "deliverySno")
    private Integer deliverySno;

    @JacksonXmlProperty(localName = "relationFl")
    private String relationFl;

    @JacksonXmlProperty(localName = "imgDetailViewFl")
    private String imgDetailViewFl;

    @JacksonXmlProperty(localName = "externalVideoFl")
    private String externalVideoFl;

    @JacksonXmlProperty(localName = "detailInfoDelivery")
    private String detailInfoDelivery;

    @JacksonXmlProperty(localName = "detailInfoAS")
    private String detailInfoAS;

    @JacksonXmlProperty(localName = "detailInfoRefund")
    private String detailInfoRefund;

    @JacksonXmlProperty(localName = "detailInfoExchange")
    private String detailInfoExchange;

    @JacksonXmlProperty(localName = "allCateCd")
    private String allCateCd;

    @JacksonXmlProperty(localName = "imageUpdate")
    private String imageUpdate;

    @JacksonXmlProperty(localName = "goodsMustInfoData")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GodoGoodsMustInfoData> goodsMustInfoData;

    @JacksonXmlProperty(localName = "optionData")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GodoOptionData> optionData;

    @JacksonXmlProperty(localName = "textOptionData")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GodoTextOptionData> textOptionData;

    public GodoGoodsData() {
        this.goodsNmFl = "D";
        this.optionFl = "N";
        this.deliverySno = 2;
        this.scmNo = "1";
        this.imageUpdate = "N";
        this.goodsState = "N";
    }
 }
