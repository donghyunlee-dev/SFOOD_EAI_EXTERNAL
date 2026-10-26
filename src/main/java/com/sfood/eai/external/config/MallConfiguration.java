package com.sfood.eai.external.config;

import com.sfood.eai.external.dto.CommonSearchReq;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@Builder
public class MallConfiguration {
     /**
     * 조회 조건
     */
    private CommonSearchReq condition;
    /**
     * 몰 연동 키 데이터
     */
    private ConcurrentHashMap<String, String> apiKeys;
    /**
     * 연동 몰 서비스 주소
     */
    private String mallApiUrl;
    /**
     * 참조 정보 1 - XML 파일 이름
     */
    private String xmlFileName;
    /**
     * 참조 정보 2 - OCI 스토리지 파일 경로
     */
    private String storageDirPath;

    public String getServiceUrl() {
        StringBuffer sb = new StringBuffer();

        // SabangNet
        if (ObjectUtils.isEmpty(apiKeys.get("partner_key"))) {
            sb.append(mallApiUrl);
            sb.append("?");
            return sb.toString();
        }
        // GodoMall
        sb.append(mallApiUrl)
                .append("?partner_key=")
                .append(apiKeys.get("partner_key"))
                .append("&key=")
                .append(apiKeys.get("key"));
        return sb.toString();
    }

    public String getApiKey(String key) {
        return apiKeys.get(key);
    }

    public String getStorageFileName() {
        return storageDirPath + xmlFileName;
    }
}
