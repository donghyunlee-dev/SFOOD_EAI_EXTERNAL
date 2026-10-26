package com.sfood.eai.external.entity.order;

import java.time.LocalDateTime;
import java.util.Date;

import com.sfood.eai.external.entity.id.order.OrderMasterId;

import com.sfood.eai.external.entity.partner.Partner;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.Constants;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.type.godo.GodoSettleKind;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

/**
 * 주문 통합 기본 정보
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "TBL_ORDER_MST")
@IdClass(OrderMasterId.class)
public class OrderMaster extends BaseEntity {
    @Id
    @Column(name = "COMPANY_CODE")
    private String companyCode; // 회사코드

    @Id
    @Column(name = "MARKET_MALL_CODE")
    private String marketMallCode; // 연동몰코드(주문 데이터 정의.xlsx : 쇼핑몰명)

    @Id
    @Column(name = "ORDER_MERGE_NO")
    private String orderMergeNo; // 주문통합번호

    @Column(name = "ORDER_DATE")
    private LocalDateTime orderDate; // 주문통합번호

    @Column(name = "ORDER_NO")
    private String orderNo; // 주문통합번호

    @Column(name = "MARKET_ORDER_NO")
    private String marketOrderNo; // 연동몰 메인주문번호(주문 데이터 정의.xlsx : 마켓주문번호)

    @Column(name = "ORDER_CHANNEL_CODE")
    private String orderChannelCode; // 주문채널코드(주문 데이터 정의.xlsx : 주문채널)

    @Column(name = "ORDER_MERGE_TITLE")
    private String orderMergeTitle; // 주문(통합)명(주문 데이터 정의.xlsx : 없음, 외 4건 같은 문장이 필요할 수 있을 것 같아 생성함)

    @Column(name = "IN_MALL_CODE")
    private String inMallCode; // 연동거래처코드

    @Column(name = "ORDER_PARTNER_CODE")
    private String orderPartnerCode; // 주문처코드(주문 데이터 정의.xlsx : 주문처코드 / 주문처명)

    @Column(name = "SSH_PARTNER_CODE")
    private String sshPartnerCode; // 납품처코드(주문 데이터 정의.xlsx : 납품처코드 / 납품처명)

    @Column(name = "SBI_PARTNER_CODE")
    private String sbiPartnerCode; // 발행처코드(주문 데이터 정의.xlsx : 일단은 없음)

    @Column(name = "SPA_PARTNER_CODE")
    private String spaPartnerCode; // 수금처코드(주문 데이터 정의.xlsx : 일단은 없음)

    @Column(name = "SALES_GROUP_CODE")
    private String salesGroupCode;

    @Column(name = "DELV_STORE_CODE")
    private String delvStoreCode; // 점포코드(리테일 특수 데이터)

    @Column(name = "TO_WAREHOUSE_CODE")
    private String toWarehouseCode; // 점포코드(리테일 특수 데이터)

    @Column(name = "EMERGENCY_YN")
    private String emergencyYn; // 긴급여부

    @Column(name = "EMERGENCY_REQ_DATE")
    private Date emergencyReqDate; // 긴급요청일자

    @Column(name = "ORDER_USER_NAME")
    private String orderUserName; // 주문자명(주문 데이터 정의.xlsx : 주문자 정보 > 주문자명)

    @Column(name = "ORDER_USER_TEL")
    private String orderUserTel; // 주문자 전화번호(주문 데이터 정의.xlsx : 주문자 정보 > 전화번호)

    @Column(name = "ORDER_USER_CEL")
    private String orderUserCel; // 주문자 휴대폰번호(주문 데이터 정의.xlsx : 주문자 정보 > 휴대폰번호)

    @Column(name = "ORDER_USER_EMAIL")
    private String orderUserEmail; // 주문자 이메일(주문 데이터 정의.xlsx : 주문자 정보 > 이메일)

    @Column(name = "ORDER_USER_ADDRESS")
    private String orderUserAddress; // 주문자 주소(주문 데이터 정의.xlsx : 주문자 정보 > 주소)

    @Column(name = "ORDER_USER_ZIPCODE")
    private String orderUserZipcode; // 주문자 우편번호(주문 데이터 정의.xlsx : 없으나 필요할 것 같아서 넣어둠)

    @Column(name = "MAX_ORDER_DETAIL_SEQ")
    private Integer maxOrderDetailSeq; // 최근 주문상세번호

    @Column(name = "TRANS_CLOSE_DATE")
    private Date transCloseDate; // 수불마감일자

    @Column(name = "SALES_CLOSE_DATE")
    private Date salesCloseDate; // 매출마감일자

    @Column(name = "ORDER_MST_MSG")
    private String orderMstMsg; // 주문 관련 메모

    @Transient
    private String receiptFl;

    @Transient
    private String settleKindCode;

    @Transient
    private String orderStatus;

    public OrderMasterId getId() {
        return new OrderMasterId(this.companyCode, this.marketMallCode, this.orderMergeNo);
    }

    public void update(OrderMaster updateOrder) {
        this.orderMergeTitle = updateOrder.getOrderMergeTitle();
        this.orderUserName = updateOrder.getOrderUserName();
        this.orderUserEmail = updateOrder.getOrderUserEmail();
        this.orderUserTel = updateOrder.getOrderUserTel();
        this.orderUserCel = updateOrder.getOrderUserCel();
        this.orderUserAddress = updateOrder.getOrderUserAddress();
        this.orderUserZipcode = updateOrder.getOrderUserZipcode();
        this.orderMstMsg = updateOrder.getOrderMstMsg();
    }

    public void updatePartner(Partner partner) {

        this.orderPartnerCode = partner.getPartnerCode();

        // 에쓰푸드몰 거래처 설정 && 주문처가 에쓰푸드몰이 아니면 ... (미등록 거래처일 경우)
        if (partner.getCompanyCode().equals(Constants.SFOOD_COMPANY_CODE) && MallOrderType.SFOOD.code().equals(this.marketMallCode)) {

            if (!Constants.SFOOD_MALL_DEFAULT_PARTNER_CODE.equals(this.orderPartnerCode)) {
                this.sshPartnerCode = partner.getPartnerCode();
                this.spaPartnerCode = Constants.SFOOD_MALL_DEFAULT_PARTNER_CODE;
                // 신용카드 외 (신용카드 , 에스크로 신용카드, 간편결제 신용카드)
                if (isPaymentByCreditCard()) {
                    this.sbiPartnerCode = Constants.SFOOD_MALL_DEFAULT_PARTNER_CODE;
                } else {
                    // 무통장
                    if (this.receiptFl == null) {
                        this.sbiPartnerCode = Constants.SFOOD_MALL_DEFAULT_PARTNER_CODE;
                    } else {
                        this.sbiPartnerCode = partner.getPartnerCode();
                    }
                }
            } else {
                this.sshPartnerCode = Constants.SFOOD_MALL_DEFAULT_PARTNER_CODE;
                this.sbiPartnerCode = Constants.SFOOD_MALL_DEFAULT_PARTNER_CODE;
                this.spaPartnerCode = Constants.SFOOD_MALL_DEFAULT_PARTNER_CODE;
            }
        } else {
            this.sshPartnerCode = partner.getSshPartnerCode();
            this.sbiPartnerCode = partner.getSbiPartnerCode();
            this.spaPartnerCode = partner.getSpaPartnerCode();
        }

        this.salesGroupCode = partner.getSalesGroupCode();
    }

    public boolean validate() {

        if (!StringUtils.hasText(this.orderPartnerCode)
                || !StringUtils.hasText(this.sshPartnerCode)
                || !StringUtils.hasText(this.sbiPartnerCode)
                || !StringUtils.hasText(this.spaPartnerCode)) {
            return false;
        }

        if (ChannelType.RETAIL.code().equals(this.orderChannelCode) && !StringUtils.hasText(this.delvStoreCode)) {
            return false;
        }

        return true;
    }

    public int increaseDetailMaxSequence() {
        return ++this.maxOrderDetailSeq;
    }

    private boolean isPaymentByCreditCard() {

        if (!StringUtils.hasText(this.settleKindCode)) {
            return false;
        }

        return GodoSettleKind.valueOf(this.settleKindCode.toUpperCase()).value().equals(GodoSettleKind.PC.value());
    }
}
