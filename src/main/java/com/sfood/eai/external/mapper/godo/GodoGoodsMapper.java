package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.component.document.godo.data.GodoGoodsData;
import com.sfood.eai.external.dto.godo.GodoGoodsReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoGoodsMapper {

    GodoGoodsMapper INSTANCE = Mappers.getMapper(GodoGoodsMapper.class);

    @Mapping(target = "idx", source = "goodsInfo.idx")
    @Mapping(target = "fixedPrice", source = "goodsInfo.fixedPrice")
    @Mapping(target = "costPrice", source = "goodsInfo.cost")
    @Mapping(target = "goodsPrice", source = "goodsInfo.salesPrice")
    @Mapping(target = "makerNm", source = "goodsInfo.supplier")
    @Mapping(target = "originNm", source = "goodsInfo.origin")
    @Mapping(target = "taxFreeFl", source = "goodsInfo.taxYn", qualifiedByName = "convertToTaxType")
    @Mapping(target = "goodsSearchWord", source = "goodsInfo.search")
    GodoGoodsData toDataModel(GodoGoodsReq.GodoGoodsInfo goodsInfo);

    @Named("convertToTaxType")
    default String convertToTaxType(String taxYn) {

        if(StringUtils.hasText(taxYn)) {
           return taxYn.toUpperCase().equals("Y") ? "t" : "f";
        } else {
            return "t";
        }
    }
}
