package com.sfood.eai.external.entity.id.godo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GodoOrderDeliveryId extends GodoBaseId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer sno;
}
