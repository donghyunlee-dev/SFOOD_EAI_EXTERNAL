package com.sfood.eai.external.component.http;

import com.sfood.eai.external.aop.annotation.HttpLog;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.godo.*;
import com.sfood.eai.external.exception.APIException;
import com.sfood.eai.external.util.CustomMapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.sfood.eai.external.type.InterfaceType.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Slf4j
@RequiredArgsConstructor
@Component
public class GodoHttpClient {

    private final RestTemplate restTemplate;

    @HttpLog(GODO_ORDER_SEARCH_001)
    public ResponseEntity<GodoOrderRes> retrieveOrder(final MallConfiguration mallConfiguration) throws APIException {
        return getGodoOrderResponseEntity(mallConfiguration);
    }

    @HttpLog(GODO_CLAIM_SEARCH_001)
    public ResponseEntity<GodoOrderRes> retrieveClaim(final MallConfiguration mallConfiguration) throws APIException {
        return getGodoOrderResponseEntity(mallConfiguration);
    }

    @HttpLog(GODO_STATUS_UPDATE_001)
    public ResponseEntity<GodoOrderStatusRes> sendOrderStatus(final MallConfiguration mallConfiguration) throws APIException {
        return getGodoOrderStatusResponseEntity(mallConfiguration);
    }

    @HttpLog(GODO_STATUS_UPDATE_002)
    public ResponseEntity<GodoOrderStatusRes> sendOrderInvoice(final MallConfiguration mallConfiguration) throws APIException {
        return getGodoOrderStatusResponseEntity(mallConfiguration);
    }

    @HttpLog(GODO_STATUS_UPDATE_002)
    public ResponseEntity<GodoOrderStatusRes> sendOrderInvoice(final MallConfiguration mallConfiguration, final String xmlFileName) throws APIException {
        return restTemplate.exchange(getUri(mallConfiguration, xmlFileName), POST, getHttpEntity(), GodoOrderStatusRes.class);
    }

    @HttpLog(GODO_STATUS_UPDATE_003)
    public ResponseEntity<GodoOrderStatusRes> sendOrderClaim(final MallConfiguration mallConfiguration) throws APIException {
        return getGodoOrderStatusResponseEntity(mallConfiguration);
    }

    @HttpLog(GODO_GOODS_UPDATE_001)
    public ResponseEntity<GodoGoodsRes> sendNewGoods(final MallConfiguration mallConfiguration, final String xmlFileName) throws APIException {
        return restTemplate.exchange(getUri(mallConfiguration, xmlFileName), POST, getHttpEntity(), GodoGoodsRes.class);
    }

    @HttpLog(GODO_GOODS_UPDATE_002)
    public ResponseEntity<GodoGoodsRes> sendUpdateGoods(final MallConfiguration mallConfiguration, final String xmlFileName) throws APIException {
        return restTemplate.exchange(getUri(mallConfiguration, xmlFileName), POST, getHttpEntity(), GodoGoodsRes.class);
    }

    @HttpLog(GODO_RECEIPT_SEARCH_001)
    public ResponseEntity<GodoReceiptRes> retrieveReceipt(final MallConfiguration mallConfiguration) throws APIException {

        GodoReceiptReq request = GodoReceiptReq.builder()
                .startDate(mallConfiguration.getCondition().getCollectDate())
                .endDate(mallConfiguration.getCondition().getCollectDate())
                .receiptFl("t")
                .build();

        URI apiUri = UriComponentsBuilder.fromUriString(GODO_RECEIPT_SEARCH_001.getServiceUri())
                .queryParams(CustomMapUtils.convert(request))
                .build().encode().toUri();

        return restTemplate.exchange(apiUri, GET, getHttpEntity(), GodoReceiptRes.class);
    }

    private ResponseEntity<GodoOrderRes> getGodoOrderResponseEntity(MallConfiguration mallConfiguration) throws APIException {
        return restTemplate.exchange(getUri(mallConfiguration), POST, getHttpEntity(), GodoOrderRes.class);
    }

    private ResponseEntity<GodoOrderStatusRes> getGodoOrderStatusResponseEntity(MallConfiguration mallConfiguration) throws APIException {
        return restTemplate.exchange(getUri(mallConfiguration), POST, getHttpEntity(), GodoOrderStatusRes.class);
    }

    private HttpEntity<String> getHttpEntity() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(header);
        return httpEntity;
    }

    private URI getUri(MallConfiguration mallConfiguration) {
        return UriComponentsBuilder.fromUriString(mallConfiguration.getServiceUrl())
                .queryParams(CustomMapUtils.convert(mallConfiguration.getCondition()))
                .build().encode().toUri();
    }

    private URI getUri(MallConfiguration config, String xmlFileName) {
        String encodedDataUrl = URLEncoder.encode(xmlFileName.replace("%2F", "/"), StandardCharsets.UTF_8);
        URI apiUri = UriComponentsBuilder.fromUriString(config.getServiceUrl()).build().encode().toUri();
        String joinUrl = apiUri + "&data_url=" + encodedDataUrl;

        return URI.create(joinUrl);
    }
}
