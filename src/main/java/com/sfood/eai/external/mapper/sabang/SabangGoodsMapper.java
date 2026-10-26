package com.sfood.eai.external.mapper.sabang;

import com.sfood.eai.external.component.document.sabang.data.SabangGoodsLiteRegister;
import com.sfood.eai.external.component.document.sabang.data.SabangGoodsRegister;
import com.sfood.eai.external.dto.sabang.SabangGoodsLiteReq;
import com.sfood.eai.external.dto.sabang.SabangGoodsReq;
import com.sfood.eai.external.util.CustomStringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SabangGoodsMapper {

    SabangGoodsMapper INSTANCE = Mappers.getMapper(SabangGoodsMapper.class);

    @Mapping(target = "goodsNm", source = "goods.goodsName")
    @Mapping(target = "companyGoodsCod", source = "goods.goodsCode")
    @Mapping(target = "goodsSearch", source = "goods.search")
    @Mapping(target = "maker", source = "goods.supplier")
    @Mapping(target = "origin", source = "goods.origin")
    @Mapping(target = "status", source = "goods.status", qualifiedByName = "getStatusAfterRemoveChar")
    @Mapping(target = "goodsCost", source = "goods.cost")
    @Mapping(target = "goodsPrice", source = "goods.salesPrice")
    @Mapping(target = "goodsConsumerPrice", source = "goods.consumerPrice")
    @Mapping(target = "taxYn", source = "goods.taxYn", qualifiedByName = "convertToTaxType")
    SabangGoodsRegister.Data toDataModel(SabangGoodsReq.SabangGoodsInfo goods);

    @Mapping(target = "goodsNm", source = "goods.goodsName")
    @Mapping(target = "companyGoodsCd", source = "goods.goodsCode")
    @Mapping(target = "status", source = "goods.status", qualifiedByName = "getStatusAfterRemoveChar")
    @Mapping(target = "goodsPrice", source = "goods.salesPrice")
    @Mapping(target = "goodsConsumerPrice", source = "goods.salesPrice")
    @Mapping(target = "skuInfo", source = "goods.skuInfo")
    SabangGoodsLiteRegister.Data toDataModel(SabangGoodsLiteReq.SabangGoodsInfo goods);

    @Named("getStatusAfterRemoveChar")
    default String getStatusAfterRemoveChar(String value) {
        return String.valueOf(CustomStringUtils.extractNumbers(value)[0]);
    }

    @Named("convertToTaxType")
    default String convertToTaxType(String taxYn) {
        if("Y".toUpperCase().equals(taxYn)) {
            return "1"; // 과세
        } else if("N".toUpperCase().equals(taxYn)) {
            return "2"; // 비과세
        } else {
            return "3"; // 자료없음
        }
    }
}
