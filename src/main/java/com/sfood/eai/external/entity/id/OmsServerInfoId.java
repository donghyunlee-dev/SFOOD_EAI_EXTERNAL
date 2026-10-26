package com.sfood.eai.external.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OmsServerInfoId {

    @Column(name = "INSTANCE_NAME")
    private String instanceName;

    @Column(name = "APPLICATION_NAME")
    private String applicationName;

    @Column(name = "APPLICATION_PORT")
    private String applicationPort;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;
}
