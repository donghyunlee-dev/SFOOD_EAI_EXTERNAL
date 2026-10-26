package com.sfood.eai.external.mapper.godo;

import com.sfood.eai.external.component.document.godo.data.GodoOrderStatusData;
import com.sfood.eai.external.dto.order.OrderInvoiceReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GodoOrderInvoiceMapper {

    GodoOrderInvoiceMapper INSTANCE = Mappers.getMapper(GodoOrderInvoiceMapper.class);

    @Mapping(target = "idx", source = "invoice.idx")
    @Mapping(target = "orderNo", source = "marketOrderNo")
    @Mapping(target = "sno", source = "marketOrderSubNo")
    @Mapping(target = "orderStatus", constant = "d1")
    @Mapping(target = "invoiceNo", source = "invoiceNo")
    @Mapping(target = "invoiceCompanySno", constant = "18")
    GodoOrderStatusData toDataModel(OrderInvoiceReq.OrderInvoiceInfo invoice);
}
