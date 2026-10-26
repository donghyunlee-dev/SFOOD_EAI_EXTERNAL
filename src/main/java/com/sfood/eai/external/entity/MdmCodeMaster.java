package com.sfood.eai.external.entity;

import com.sfood.eai.external.entity.id.MdmCodeMstId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@IdClass(MdmCodeMstId.class)
@Table(name = "TBL_MDM_CODE_MST")
@Entity
public class MdmCodeMaster {
    @Id
    @Column(name = "MDM_GROUP_CODE")
    private String mdmGroupCode;

    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "PARENT_MDM_GROUP_CODE")
    private String parentMdmGroupCode;

    @Column(name = "MDM_GROUP_NAME")
    private String mdmGroupName;

    @Column(name = "MDM_GROUP_DETAIL")
    private String mdmGroupDetail;

    @Column(name = "EDIT_YN")
    private String editYn;

    @Column(name = "REMOVE_YN")
    private String removeYn;

    @Column(name = "DISPLAY_YN")
    private String displayYn;

    @Column(name = "SORT_NO")
    private String sortNo;

    @Column(name = "USE_YN")
    private String useYn;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "CREATE_DATE")
    private String createDate;

    @Column(name = "UPDATE_ID")
    private String updateId;

    @Column(name = "UPDATE_DATE")
    private String updateDate;
}
