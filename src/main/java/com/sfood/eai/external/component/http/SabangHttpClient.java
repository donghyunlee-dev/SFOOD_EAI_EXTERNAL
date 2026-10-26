package com.sfood.eai.external.component.http;

import com.sfood.eai.external.aop.annotation.HttpLog;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.sabang.*;
import com.sfood.eai.external.util.CustomMapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.sfood.eai.external.type.InterfaceType.*;
import static org.springframework.http.HttpMethod.GET;

@Slf4j
@RequiredArgsConstructor
@Component
public class SabangHttpClient {

    private final RestTemplate restTemplate;

    @HttpLog(SABANG_ORDER_SEARCH_001)
    public ResponseEntity<?> retrieveOrder(final MallConfiguration config, final String xmlFileName) throws Exception {
        return restTemplate.exchange(getUri(config, xmlFileName), GET, getHttpEntity(), SabangOrderRes.class);
    }

    @HttpLog(SABANG_CLAIM_SEARCH_001)
    public ResponseEntity<?> retrieveClaim(final MallConfiguration config, final String xmlFileName) throws Exception {
        return restTemplate.exchange(getUri(config, xmlFileName), GET, getHttpEntity(), SabangClaimRes.class);
    }

    @HttpLog(SABANG_INVOICE_UPDATE_001)
    public ResponseEntity<?> sendInvoice(final MallConfiguration config, final String xmlFileName) throws Exception {
        return restTemplate.exchange(getUri(config, xmlFileName), GET, getHttpEntity(), SabangInvoiceRes.class);
    }

    @HttpLog(SABANG_GOODS_UPDATE_001)
    public ResponseEntity<?> sendGoods(final MallConfiguration config, final String xmlFileName) throws Exception {
        return restTemplate.exchange(getUri(config, xmlFileName), GET, getHttpEntity(), SabangGoodsRes.class);
    }

    @HttpLog(SABANG_GOODS_UPDATE_002)
    public ResponseEntity<?> sendGoodsLite(final MallConfiguration config, final String xmlFileName) throws Exception {
        return restTemplate.exchange(getUri(config, xmlFileName), GET, getHttpEntity(), SabangGoodsRes.class);
    }

    private HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/xml");
        headers.set("Content-Type", "application/xml;charset=EUC-KR");
        return new HttpEntity<>(headers);
    }

    private URI getUri(MallConfiguration config, String xmlFileName) throws Exception {
        URI apiUri = UriComponentsBuilder.fromUriString(config.getServiceUrl())
                .queryParams(CustomMapUtils.convert(new SabangRequestUri(xmlFileName)))
                .build().encode().toUri();
        return apiUri;
    }
}
