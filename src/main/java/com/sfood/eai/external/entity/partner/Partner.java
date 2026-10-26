package com.sfood.eai.external.entity.partner;

import com.sfood.eai.external.entity.id.partner.PartnerId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@IdClass(PartnerId.class)
@Table(name = "TBL_PARTNER_MST")
@Entity
public class Partner {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Id
    @Column(name = "PARTNER_CODE")
    private String partnerCode;

    @Column(name = "PARTNER_NAME")
    private String partnerName;

    @Column(name = "PARTNER_ALIAS")
    private String partnerAlias;

    @Column(name = "PARTNER_TYPE")
    private String partnerType;

    @Column(name = "DISPLAY_PRICE_YN")
    private String displayPriceYn;

    @Column(name = "ORDER_CLOSE_TIME")
    private String orderCloseTime;

    @Column(name = "DELIVERY_TYPE")
    private String deliveryType;

    @Column(name = "MAIN_WAREHOUSE_CODE")
    private String mainWarehouseCode;

    @Column(name = "PARTNER_PRESIDENT")
    private String partnerPresident;

    @Column(name = "PARTNER_CONTACT_NO")
    private String partnerContactNo;

    @Column(name = "OFFICE_FAX")
    private String officeFax;

    @Column(name = "PARTNER_EMAIL")
    private String partnerEmail;

    @Column(name = "ZIPCODE")
    private String zipcode;

    @Column(name = "ADDRESS_1")
    private String address1;

    @Column(name = "ADDRESS_2")
    private String address2;

    @Column(name = "BUSINESS_TAX_TYPE")
    private String businessTaxType;

    @Column(name = "REGISTRATION_NO")
    private String registrationNo;

    @Column(name = "SALES_GROUP_CODE")
    private String salesGroupCode;

    @Column(name = "CHILD_PARTNER_YN")
    private String childPartnerYn;

    @Column(name = "PARENT_PARTNER_CODE")
    private String parentPartnerCode;

    @Column(name = "PRM_ORDER_YN")
    private String prmOrderYn;

    @Column(name = "REPRESENT_NAME")
    private String representName;

    @Column(name = "REPRESENT_CONTACT_NO")
    private String representContactNo;

    @Column(name = "CONTACT_NO_2")
    private String contactNo2;

    @Column(name = "PARTNER_STOCK_TYPE")
    private String partnerStockType;

    @Column(name = "INDUSTRY_TYPE")
    private String industryType;

    @Column(name = "INDUSTRY_CLASS")
    private String industryClass;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "RECEIPT_YN")
    private String receiptYn;

    @Column(name = "EXT2")
    private String ext2;

    @OneToMany(mappedBy = "partner", fetch = FetchType.EAGER)
    private List<PartnerRole> roles = new ArrayList<>();

    public Partner() {}

    public Partner(String companyCode, String partnerCode) {
        this.companyCode = companyCode;
        this.partnerCode = partnerCode;
    }

    @Transient
    public String getSshPartnerCode() {
        return this.roles.stream()
                .filter(part -> part.getRoleCode().equals("SSH") && part.getDefaultInRoleYn().equals("Y"))
                .findFirst()
                .map(PartnerRole::getRolePartnerCode)
                .orElse(null);
    }

    @Transient
    public String getSbiPartnerCode() {
        return this.roles.stream()
                .filter(part -> part.getRoleCode().equals("SBI") && part.getDefaultInRoleYn().equals("Y"))
                .findFirst()
                .map(PartnerRole::getRolePartnerCode)
                .orElse(null);
    }

    @Transient
    public String getSpaPartnerCode() {
        return this.roles.stream()
                .filter(part -> part.getRoleCode().equals("SPA") && part.getDefaultInRoleYn().equals("Y"))
                .findFirst()
                .map(PartnerRole::getRolePartnerCode)
                .orElse(null);
    }
}
