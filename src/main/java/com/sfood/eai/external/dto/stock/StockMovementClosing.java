package com.sfood.eai.external.dto.stock;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementClosing implements Serializable {
    private String companyCode;
    private LocalDateTime maxTransDate;
}
