package com.sfood.eai.external.component.http;

import com.sfood.eai.external.aop.annotation.HttpLog;
import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.dto.CommonDataRes;
import com.sfood.eai.external.dto.stock.StockMovementCloseReq;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.sfood.eai.external.type.Constants.EXTERNAL_USER_ID;
import static com.sfood.eai.external.type.InterfaceType.EAI_INVENTORY_MOVE_CLOSE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class AppHttpClient {

    private final RestTemplate restTemplate;

    public AppHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> checkRemoteServiceStatus(String url) {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(header);

        URI uri = URI.create("http://" + url + "/management/health");
        return restTemplate.exchange(uri, GET, httpEntity, String.class);
    }

    public ResponseEntity<?> closeStockMovement(StockMovementCloseReq requestDto) {
        String url;

        if (StringUtils.hasText(AppConfig.EAI_PORT)) {
            url = AppConfig.EAI_HOST + ":" + AppConfig.EAI_PORT + EAI_INVENTORY_MOVE_CLOSE.getServiceUri();
        } else {
            url = AppConfig.EAI_HOST + "/eai" + EAI_INVENTORY_MOVE_CLOSE.getServiceUri();
        }

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("company-code", requestDto.getCompanyCode());
        header.set("user-id", EXTERNAL_USER_ID);
        header.set("api-key", AppConfig.EAI_API_KEY);
        header.set("user-key", AppConfig.EAI_USER_KEY);
        HttpEntity<StockMovementCloseReq> httpEntity = new HttpEntity<>(requestDto, header);

        return restTemplate.exchange(URI.create(url), POST, httpEntity, CommonDataRes.class);
    }
}
