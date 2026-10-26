package com.sfood.eai.external.dto.godo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GodoReceiptRes {

    private String status;
    private String message;
    private List<ReceiptData> data;

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ReceiptData {
        private String orderNo;
        private String orderStatus;
        private String receiptFl;
        private String orderDate;
    }
}
