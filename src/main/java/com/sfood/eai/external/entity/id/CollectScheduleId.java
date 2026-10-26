package com.sfood.eai.external.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CollectScheduleId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "COLLECT_SCHEDULE_SEQ")
    private Integer collectScheduleSeq;
}
