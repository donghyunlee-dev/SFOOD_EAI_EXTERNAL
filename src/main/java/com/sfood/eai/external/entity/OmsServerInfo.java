package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.OmsServerInfoId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Table(name = "TBL_OMS_SERVER_INFO")
@Entity
public class OmsServerInfo {

    @EmbeddedId
    private OmsServerInfoId id;

    @Column(name = "PUBLIC_IP_V4")
    private String publicIpV4;

    @Column(name = "PUBLIC_IP_V6")
    private String publicIpV6;

    @Column(name = "PRIVATE_IP_V4")
    private String privateIpV4;

    @Column(name = "PRIVATE_IP_V6")
    private String privateIpV6;

    @Column(name = "USE_YN")
    private String useYn;

    public String getPrivateV4Url() {
        return this.privateIpV4 + ":" + this.id.getApplicationPort();
    }

    public String getPrivateV6Url() {
        return this.privateIpV6 + ":" + this.id.getApplicationPort();
    }

    public String getPublicV4Url() {
        return this.publicIpV4 + ":" + this.id.getApplicationPort();
    }

    public String getPublicV6Url() {
        return this.publicIpV6 + ":" + this.id.getApplicationPort();
    }
}
