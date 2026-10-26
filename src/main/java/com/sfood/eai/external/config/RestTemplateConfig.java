package com.sfood.eai.external.config;

import com.sfood.eai.external.exception.CustomResponseErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * RestTemplate 빈 설정
 */
@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {

        SocketConfig socketConfig = SocketConfig
                .custom()
                .setSoTimeout(Timeout.ofSeconds(30))
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .build();

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(10))
                .setSocketTimeout(Timeout.ofSeconds(30))
                .build();

        // 커넥션 풀을 관리하는 매니저 설정
        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        poolingConnManager.setDefaultSocketConfig(socketConfig);
        poolingConnManager.setDefaultConnectionConfig(connectionConfig);
        poolingConnManager.setDefaultMaxPerRoute(20);   // 각 호스트에 대해 최대 20개의 연결
        poolingConnManager.setMaxTotal(100);            // 전체 커넥션 수 최대 100개

        RequestConfig requestConfig = org.apache.hc.client5.http.config.RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(10))
                .setResponseTimeout(Timeout.ofSeconds(30))
                .build();

        // Apache HttpClient 생성
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(poolingConnManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        // HttpComponentsClientHttpRequestFactory 생성
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // BufferingClientHttpRequestFactory 감싸기
        BufferingClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(httpRequestFactory);
        // RestTemplate 생성 및 설정
        RestTemplate restTemplate = new RestTemplate(bufferingFactory);

        List<ClientHttpRequestInterceptor> interceptors = List.of(retryTemplateInterceptor(), printLogInterceptor());

        restTemplate.setInterceptors(interceptors);

        // StringHttpMessageConverter (UTF-8 & EUC-KR 지원)
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("text", "html", StandardCharsets.UTF_8),
                new MediaType("text", "html", Charset.forName("EUC-KR")),
                new MediaType("application", "xml", StandardCharsets.UTF_8),
                new MediaType("application", "xml", Charset.forName("EUC-KR")),
                new MediaType("application", "json", StandardCharsets.UTF_8),
                new MediaType("application", "json", Charset.forName("EUC-KR"))
        ));

        // XML 변환을 위한 MappingJackson2XmlHttpMessageConverter
        MappingJackson2XmlHttpMessageConverter xmlConverter = new MappingJackson2XmlHttpMessageConverter();
        xmlConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("text", "html", StandardCharsets.UTF_8),
                new MediaType("text", "html", Charset.forName("EUC-KR")),
                new MediaType("application", "xml", StandardCharsets.UTF_8),
                new MediaType("application", "xml", Charset.forName("EUC-KR")),
                new MediaType("text", "xml", StandardCharsets.UTF_8),
                new MediaType("text", "xml", Charset.forName("EUC-KR"))
        ));

        // JSON 변환을 위한 MappingJackson2HttpMessageConverter
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "json", StandardCharsets.UTF_8),
                new MediaType("application", "json", Charset.forName("EUC-KR"))
        ));

        restTemplate.setMessageConverters(Arrays.asList(stringConverter,xmlConverter, jsonConverter));
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        
        return restTemplate;
    }

    private ClientHttpRequestInterceptor printLogInterceptor() {
        return (request, body, execution) -> {
            // 요청 로깅
            log.info("Request URI: {}", request.getURI());
            // log.info("Request Method: {}", request.getMethod());
            // log.info("Request Headers: {}", request.getHeaders());
            // log.info("Request Body: {}", new String(body));

            // 실제 요청 실행
            var response = execution.execute(request, body);

            // 응답 로깅
            log.info("Response Status Code: {}", response.getStatusCode());
            // log.info("Response Headers: {}", response.getHeaders());
            return response;
        };
    }

    private ClientHttpRequestInterceptor retryTemplateInterceptor() {
        return (request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(2));
            FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
            backOffPolicy.setBackOffPeriod(1000);
            retryTemplate.setBackOffPolicy(backOffPolicy);

            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (Exception e) {
                log.error("RestTemplate retry failed", e);
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException("RestTemplate execution failed", e);
            }
        };
    }
}
