package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.GwApprovalId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_GW_APPROVAL_INFO")
@Entity
public class GwApproval {

    @EmbeddedId
    private GwApprovalId id;

    @Column(name = "GW_DOCUMENT_NO")
    private String gwDocumentNo;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "APPROVAL_DATE")
    private String approvalDate;

    @Column(name = "REJECT_COMMENT")
    private String rejectComment;

    @Column(name = "APPROVAL_ID")
    private String approvalId;

    @Column(name = "EXT3")
    private String ext3;
}
