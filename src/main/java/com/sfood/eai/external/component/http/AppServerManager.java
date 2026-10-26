package com.sfood.eai.external.component.http;

import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.entity.OmsServerInfo;
import com.sfood.eai.external.repository.OmsServerInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <P>서버 순서 및 상태 체크</P>
 * Priority 로 서버의 Health CHeck 우선 순위 순서 정하기
 * 반복해서 Health Check 하기
 * - IP,Port 를 비교해서 서버의 app 인지 판단
 * - 서버의 app 이면 스케쥴 실행
 * - 서버의 app 이 아니면 Health check 실행
 * - 성공이면 스케쥴 실행하지 않음.
 * - 실패면 다음 서버에 Health Check 실행
 * 반복하면서 스케쥴 실행 여부 판단.
 */

@Slf4j
@Component
public final class AppServerManager {

    private static final String SERVER_APPLICATION_NAME = "SFOOD_EAI_EXTERNAL";
    private final OmsServerInfoRepository omsServerInfoRepository;
    private final AppHttpClient appHttpClient;
    private final String url;
    private final List<OmsServerInfo> servers;

    public AppServerManager(OmsServerInfoRepository omsServerInfoRepository, AppHttpClient appHttpClient) {
        this.appHttpClient = appHttpClient;
        this.omsServerInfoRepository = omsServerInfoRepository;
        this.servers = findExternalServerInfo();
        this.url = AppConfig.APP_HOST + ":" + AppConfig.APP_PORT;
    }

    public boolean isActive() {

        for (OmsServerInfo server : this.servers) {
            // 우선 순위에 의해 External 서버 정보가 해당 서버이면 서버 체크 없이 Active 상태
            if (this.url.equals(server.getPrivateV4Url())) {
                return true;
            }

            log.info("Server Check : this={}, remote={} ",  this.url, server.getPrivateV4Url());

            try {
                ResponseEntity<String> response = appHttpClient.checkRemoteServiceStatus(server.getPrivateV4Url());
                // 우선 순위 높은 서버의 상태가 정상이면 해당 서버는 비활성화
                if (response.getStatusCode().is2xxSuccessful()) {
                    return false;
                }
            } catch (Exception e) {
                log.error("Check server status failed : {}", server.getPrivateV4Url(), e);
                throw e;
            }
        }
        // Active 서버를 찾지 못하면 해당 서버로 실행
        return true;
    }

    private List<OmsServerInfo> findExternalServerInfo() {
        return omsServerInfoRepository.findByNameInUse(SERVER_APPLICATION_NAME);
    }
}
