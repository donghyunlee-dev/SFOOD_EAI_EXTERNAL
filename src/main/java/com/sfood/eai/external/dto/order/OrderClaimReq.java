package com.sfood.eai.external.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfood.eai.external.dto.CommonSearchReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper=false)
public class OrderClaimReq extends CommonSearchReq {
	
    @JsonProperty("claim_merge_no")
	private String claimMergeNo;
	
    @JsonProperty("claim_detail_seq")
	private Integer claimDetailSeq;
	
    @JsonProperty("order_channel_code")
	private String orderChannelCode;
	
    @JsonProperty("market_mall_code")
	private String marketMallCode;
	
    @JsonProperty("order_merge_no")
	private String orderMergeNo;
	
    @JsonProperty("order_detail_seq")
	private Integer orderDetailSeq;
	
    @JsonProperty("claim_date")
	private String claimDate;
	
    @JsonProperty("claim_status_code")
	private String claimStatusCode;
	
    @JsonProperty("claim_qty")
	private Integer claimQty;
	
    @JsonProperty("claim_reason_mst_code")
	private String claimReasonMstCode; 
	
    @JsonProperty("claim_reason_detail_code")
	private String claimReasonDetailCode; 
	
    @JsonProperty("claim_reason_content")
	private String claimReasonContent;

    @JsonProperty("return_req_date")
    private LocalDateTime returnReqDate;

    @JsonProperty("return_real_yn")
    private String returnRealYn;

    @JsonProperty("wms_out_qty")
    private Integer wmsOutQty;

    @JsonProperty("market_stop_yn")
	private String marketStopYn; 
	
    @JsonProperty("hope_delv_date")
	private String hopeDelvDate;

    @Override
    public OrderClaimReq addParams(HttpServletRequest request) {
        this.setCompanyCode(request.getHeader("company-code"));
        this.setUserId(request.getHeader("user-id"));
        this.setRequestUri(request.getRequestURI());
        return this;
    }
}