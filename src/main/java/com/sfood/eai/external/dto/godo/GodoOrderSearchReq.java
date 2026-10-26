package com.sfood.eai.external.dto.godo;

import com.sfood.eai.external.dto.CommonSearchReq;
import com.sfood.eai.external.type.ChannelType;
import com.sfood.eai.external.type.godo.GodoDateType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GodoOrderSearchReq extends CommonSearchReq {
    // 기간검색 타입(order/modify 중 order만 사용)
    private String dateType;
    // 주문일 기준 조회요청 from
    private String startDate;
    // 주문일 기준 조회요청 to
    private String endDate;
    // 주문 번호
    private String orderNo;
    //주문상태 코드
    private String orderStatus;
    //검색어 검색 타입(사용하지 않음)(전화번호류의 검색만 가능, api 문서 참조)
    private String searchType;
    //검색어(사용하지 않음)
    private String searchKeyword;
    //주문정렬(orderNo desc, orderNo asc 중 orderNo asc만 사용)
    private String sort;
    //상점번호(사용하지 않음)(국내/국외몰 구분, api 문서 참조)
    private String mallSno;

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.orderStatus = null;
        dateClear();
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Request 데이터와 검색 일자 초기화
     * @param request Http Header, Uri 정보
     * @return 고도몰 API 호출 기반 검색 조건
     */
    public GodoOrderSearchReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        this.setUserId(request.getHeader("user-id"));
        this.setRequestUri(request.getRequestURI());
        this.setChannelCode(ChannelType.GODOMALL.code());
        /**
         * 검색 기간이 있을 경우 datatype 필수
         * order(주문일) / modify(수정일)
         */
        if (StringUtils.hasText(getCollectDate())) {
            if (!StringUtils.hasText(this.dateType)) {
                this.dateType = GodoDateType.ORDER.code();
            }
            this.startDate = getCollectDate();
            this.endDate = getCollectDate();
        } else {
            if (!StringUtils.hasText(this.startDate) && !StringUtils.hasText(this.endDate)) {
                if (StringUtils.hasText(this.orderNo)) {
                    dateClear();
                }
            }
        }

        return this;
    }

    private void dateClear() {
        this.dateType = this.startDate = this.endDate = null;
    }
}
