package com.sfood.eai.external.entity.godo;

import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.MallOrderStatus;
import com.sfood.eai.external.util.CustomDateUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 고도몰 주문 수집 데이터
 */
@ToString
@Builder
@Getter
public class GodoDataEntity {
    /**
     * 회사코드
     */
    private String companyCode;
    /**
     * 연동 채널 구분
     */
    private ChannelType channelType;
    /**
     * 연동몰 코드
     */
    private String mallCode;
    /**
     * 수집 일자
     */
    private String collectDate;
    /**
     * 주문 마스터
     */
    private GodoOrder order;
    /**
     * 주문 리스트
     */
    private List<GodoOrderInfo> info;
    /**
     * 배송 리스트
     */
    private List<GodoOrderDelivery> delivery;
    /**
     * 품목 리스트
     */
    private List<GodoOrderGoods> goods;
    /**
     * 품목별 옵션
     */
    private List<GodoOrderGoodsOption> option;
    /**
     * 품목의 클레임 데이터
     */
    private List<GodoOrderClaim> claim;
    /**
     * 메디솔라 식품 품목 데이터
     */
    private List<GodoAddGoods> addGoods;

    private List<GodoGift> gift;

    public String getChannelCode() {
        return channelType.code();
    }

    public String getCollectDate() {
        if (!StringUtils.hasText(this.collectDate)) {
            return CustomDateUtils.now();
        }
        return this.collectDate;
    }

    public String getGodoOrderNo() {
        return this.order.getOrderNo();
    }

    public void updateStatus(MallOrderStatus status) {
        order.setStatus(status.code());
    }

    public boolean isMultipleShipping() {
        return !this.order.getMultiShippingFl().equals("n");
    }

    public void changeOrderType(GodoOrderType type) {
        if (GodoOrderType.NORMAL.equals(type)) {
            this.option = null;
        }
    }

    public GodoOrderType getOrderType() {
        if (CollectionUtils.isEmpty(this.option)) {
            return GodoOrderType.NORMAL;
        } else {
            return GodoOrderType.SUBSCRIPTION;
        }
    }

    public enum GodoOrderType {
        NORMAL, SUBSCRIPTION
    }
}
