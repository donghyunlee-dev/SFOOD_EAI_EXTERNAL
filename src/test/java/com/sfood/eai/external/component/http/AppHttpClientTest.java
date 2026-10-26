package com.sfood.eai.external.component.http;

import com.sfood.eai.external.dto.stock.StockMovementCloseReq;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AppHttpClientTest {

    @Autowired
    private AppHttpClient client;

    @Test
    @DisplayName("원격 서버의 서비스 상태 체크 테스트")
    public void healthCheckTest() {

        // Given
        // 테스트 실행 시 SpringBoot로 서비스가 실행되어 PORT가 8093이 되기 때문에 중복되어 문제가 발생한다.
        // 테스트 진행 시 메인 서비스의 port를 8092로 실행한다. (구성 편집 -> VM옵션 설정)
        String url = "127.0.0.1:8092";

        // When
        ResponseEntity<String> response = client.checkRemoteServiceStatus(url);

        // Then
        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("미출 마감 API 호출 테스트")
    void closeStockMovement() {

        StockMovementCloseReq req = StockMovementCloseReq
                .builder()
                .companyCode("C00000001")
                .maxDate("2025-08-06")
                .userId("external")
                .build();

        ResponseEntity<?> response = client.closeStockMovement(req);

        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}