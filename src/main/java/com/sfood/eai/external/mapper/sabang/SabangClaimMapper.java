package com.sfood.eai.external.mapper.sabang;

import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.dto.sabang.SabangClaimData;
import com.sfood.eai.external.entity.sabang.SabangClaim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SabangClaimMapper {
    SabangClaimMapper INSTANCE = Mappers.getMapper(SabangClaimMapper.class);
    @Mapping(target = "companyCode", source = "searchDto.companyCode")
    @Mapping(target = "marketMallCode", source = "searchDto.mallCode")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "userId", source = "sabangClaimData.userId")
    @Mapping(target = "createId", constant = "external")
    @Mapping(target = "updateId", constant = "external")
    SabangClaim toEntity(CommonSearchReq searchDto, SabangClaimData sabangClaimData);
}
