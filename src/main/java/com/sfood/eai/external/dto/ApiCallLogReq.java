package com.sfood.eai.external.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfood.eai.external.type.InterfaceType;
import com.sfood.eai.external.type.MallOrderStatus;
import com.sfood.eai.external.type.MallOrderType;
import com.sfood.eai.external.type.OrderStatus;
import com.sfood.eai.external.type.godo.GodoOrderStatus;
import com.sfood.eai.external.type.sabang.SabangOrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;

@Slf4j
@ToString
@Getter
@Builder
public class ApiCallLogReq {
    private String id;
    private InterfaceType type;
    private Map<String, Object> data;
    private String clientIp;

    public String getLogType() {
        if (data.containsKey("userId") && data.get("userId") != null) {
            if ("scheduler".equals(data.get("userId").toString())) {
                return "A";
            } else {
                return "M";
            }
        }

        return "A";
    }

    public String getCompanyCode() {
        if (!data.containsKey("companyCode") || data.get("companyCode") == null) {
            return null;
        }

        return data.get("companyCode").toString();
    }

    public String getMallCode() {
        if (!data.containsKey("mallCode") || data.get("mallCode") == null) {
            return null;
        }

        return data.get("mallCode").toString();
    }

    public String getMallId() {
        if (!data.containsKey("mallId") || data.get("mallId") == null) {
            return null;
        }

        return data.get("mallId").toString();
    }

    public String getStatusCode() {
        try {
            if (!data.containsKey("mallCode") || data.get("mallCode") == null || !data.containsKey("orderStatus") || data.get("orderStatus") == null) {
                return null;
            }

            if (data.get("mallCode").toString().equals(MallOrderType.SABANGNET.code())) {
                return SabangOrderStatus.fromCode(data.get("orderStatus").toString()).value();
            } else if (data.get("mallCode").toString().equals(MallOrderType.SFOOD.code())
                    || data.get("mallCode").toString().equals(MallOrderType.JHONCOOK.code())) {
                return GodoOrderStatus.fromCode(data.get("orderStatus").toString()).value();
            } else {
                return data.get("orderStatus").toString();
            }
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public String getStatusName() {
        if (!data.containsKey("orderStatus") || data.get("orderStatus") == null) {
            return null;
        }
        return switch (getStatusCode()) {
            case "OS8030" -> OrderStatus.OS1020.desc();
            case "OS8040" -> OrderStatus.OS1030.desc();
            default -> OrderStatus.valueOf(getStatusCode()).desc();
        };
    }

    public String getUserId() {
        if (!data.containsKey("userId") || data.get("userId") == null) {
            return null;
        }

        return data.get("userId").toString();
    }

    public Long getReqTime() {
        return System.currentTimeMillis();
    }

    public String getData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this.data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
