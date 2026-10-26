package com.sfood.eai.external.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration("AppConfig")
public class AppConfig implements WebMvcConfigurer {

    public static String LOCAL_FILE_PATH;
    public static String STORAGE_PUBLIC_URL;
    public static String STORAGE_PATH_SEPARATOR;
    public static String STORAGE_BUCKET_NAMESPACE;
    public static String CONFIGURATION_FILE_PATH;
    public static String CONFIGURATION_PROFILE;
    public static String PUBLIC_BUCKET_NAME;
    public static String PRIVATE_BUCKET_NAME;
    public static String ACTIVE_PROFILE;
    public static String SEND_EMAIL;
    public static String EAI_HOST;
    public static String EAI_PORT;
    public static String EAI_API_KEY;
    public static String EAI_USER_KEY;
    public static String APP_HOST;
    public static int APP_PORT;
    public static boolean isLocal;

    @PostConstruct
    private void setAppHost() {
        try {

            if (isLocal) {
                this.APP_HOST = "127.0.0.1";
                return;
            }

            InetAddress localAddress = InetAddress.getLocalHost();
            this.APP_HOST = localAddress.getHostAddress();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${server.port}")
    private void setAppPort(int port) {
        this.APP_PORT = port;
    }

    @Value("${sfood.it.sabang.xml.local.file.path}")
    private void setLocalFilePath(String localFilePath) {
        this.LOCAL_FILE_PATH = localFilePath;
    }

    @Value("${sfood.it.oci.storage.public.url}")
    private void setStoragePublicUrl(String storagePublicUrl) {
        this.STORAGE_PUBLIC_URL = storagePublicUrl;
    }

    @Value("${sfood.it.oci.storage.path.separator}")
    private void setStoragePathSeparator(String STORAGE_PATH_SEPARATOR) {
        this.STORAGE_PATH_SEPARATOR = STORAGE_PATH_SEPARATOR;
    }

    @Value("${sfood.it.oci.storage.bucket.namespace}")
    private void setStorageBucketNamespace(String storageBucketNamespace) {
        this.STORAGE_BUCKET_NAMESPACE = storageBucketNamespace;
    }

    @Value("${sfood.it.oci.storage.configuration.file.path}")
    private void setConfigurationFilePath(String configurationFilePath) {
        this.CONFIGURATION_FILE_PATH = configurationFilePath;
    }

    @Value("${sfood.it.oci.storage.configuration.profile}")
    private void setConfigurationProfile(String configurationProfile) {
        this.CONFIGURATION_PROFILE = configurationProfile;
    }

    @Value("${sfood.it.oci.storage.public.bucket.name}")
    private void setPublicBucketName(String publicBucketName) {
        this.PUBLIC_BUCKET_NAME = publicBucketName;
    }

    @Value("${sfood.it.oci.storage.private.bucket.name}")
    private void setPrivateBucketName(String privateBucketName) {
        this.PRIVATE_BUCKET_NAME = privateBucketName;
    }

    @Value("${spring.config.activate.on-profile}")
    private void setActiveProfile(String activeProfile) {
        this.ACTIVE_PROFILE = activeProfile;
        isLocal = (AppConfig.ACTIVE_PROFILE == null || AppConfig.ACTIVE_PROFILE.equals("local"));
    }

    @Value("${spring.mail.username}")
    private void setSendEmail(String sendEmail) {
        this.SEND_EMAIL = sendEmail;
    }

    @Value("${sfood.it.dev.server.eai.host}")
    private void setEaiHost(String eaiHost) {
        this.EAI_HOST = eaiHost;
    }

    @Value("${sfood.it.dev.server.eai.port}")
    private void setEaiPort(String eaiPort) {
        this.EAI_PORT = eaiPort;
    }

    @Value("${sfood.it.dev.server.eai.api-key}")
    private void setEaiApiKey(String eaiApiKey) {
        this.EAI_API_KEY = eaiApiKey;
    }

    @Value("${sfood.it.dev.server.eai.user-key}")
    private void setEaiApiUserKey(String eaiUserKey) {
        this.EAI_USER_KEY = eaiUserKey;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","PATCH")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
