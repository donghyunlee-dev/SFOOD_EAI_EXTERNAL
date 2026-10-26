package com.sfood.eai.external.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import jakarta.annotation.PreDestroy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationProperties(prefix = "oms.bastion.ssh")
@Setter
@Component
public class SshTunnelingInitializer {
	
	private String host;
	private String user;
	private Integer port;
	private String databaseHost;
	private Integer databasePort;
	private Session session;
	
	@PreDestroy
	public void closeSSH() {
		if (session.isConnected()) session.disconnect();
	}
	
	public Integer buildSshConnection() {

		Integer forwardedPort = null;
		
		try {
			log.info("{}@{}:{}:{} with privateKey", user, host, port, databasePort);
			JSch jSch = new JSch();
			jSch.addIdentity("src/main/resources/key/oci.key");  // 개인키
			session = jSch.getSession(user, host, port);  // 세션 설정
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();  // ssh 연결
			forwardedPort = session.setPortForwardingL(0, databaseHost, databasePort);
		} catch (Exception e){
			log.error("fail to make ssh tunneling");
			this.closeSSH();
			System.exit(1);
		}

		return forwardedPort;
	}
}
