package com.sfood.eai.external.mapper.sabang;

import com.sfood.eai.external.component.document.sabang.data.SabangInvRegister;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import com.sfood.eai.external.entity.sabang.SabangInvoiceLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SabangInvoiceLogMapper {
    SabangInvoiceLogMapper INSTANCE = Mappers.getMapper(SabangInvoiceLogMapper.class);

    @Mapping(target = "companyCode", source = "companyCode")
    @Mapping(target = "marketMallCode", source = "mallCode")
    @Mapping(target = "orderMergeNo", source = "invoiceInfo.orderMergeNo")
    @Mapping(target = "orderDetailSeq", source = "invoiceInfo.orderDetailSeq")
    @Mapping(target = "idx", source = "data.sabangnetIdx")
    @Mapping(target = "delvCode", source = "data.takCode")
    @Mapping(target = "invoiceNo", source = "invoiceInfo.invoiceNo")
    @Mapping(target = "hopeDelvDate", source = "data.hopeDelvDate")
    SabangInvoiceLog toEntity(String companyCode, String mallCode, OrderInvoiceReq.OrderInvoiceInfo invoiceInfo, SabangInvRegister.Data data);
}
