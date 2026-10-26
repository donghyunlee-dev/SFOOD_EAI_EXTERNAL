package com.sfood.eai.external.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
public class ConnectionPoolMonitor {

    private final HikariDataSource hikariDataSource;

    @Autowired
    public ConnectionPoolMonitor(DataSource dataSource) {

        if(dataSource instanceof HikariDataSource) {
            this.hikariDataSource = (HikariDataSource) dataSource;
        } else  {
            throw new IllegalArgumentException("DataSource is not an instance of HikariDataSource");
        }
    }

    //@Scheduled(fixedRate = 60000)
    public void printLog() {
        int total = hikariDataSource.getMaximumPoolSize();
        int active = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        int idle = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
        int waiting = hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();

        log.info("[HikariCP] 총 커넥션: {}, 사용 중: {}, 유휴: {}, 대기 중: {}", total, active, idle, waiting);
    }
}
