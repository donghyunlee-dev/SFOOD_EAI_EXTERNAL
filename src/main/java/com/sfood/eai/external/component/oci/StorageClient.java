package com.sfood.eai.external.component.oci;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader.ConfigFile;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.type.OciBucket;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@DependsOn("AppConfig")
@Component
public class StorageClient {

    private ObjectStorageClient objectStorageClient;

    @PostConstruct
    private void init() {
        try {
            ConfigFile config = ConfigFileReader.parse(AppConfig.CONFIGURATION_FILE_PATH, AppConfig.CONFIGURATION_PROFILE);
            AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(config);

            objectStorageClient = ObjectStorageClient.builder()
                    .region(Region.AP_SEOUL_1)
                    .build(provider);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void upload(String fileName, String storageFilePath, OciBucket bucket) {

        try {
            File uploadFile = new File(AppConfig.LOCAL_FILE_PATH + fileName);
            UploadConfiguration configuration = UploadConfiguration.builder()
                    .allowMultipartUploads(true)
                    .allowParallelUploads(true)
                    .build();

            UploadManager uploadManager = new UploadManager(objectStorageClient, configuration);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucketName(getBucketName(bucket))
                    .namespaceName(AppConfig.STORAGE_BUCKET_NAMESPACE)
                    .objectName(storageFilePath)
                    .build();

            UploadRequest uploadDetails = UploadRequest.builder(uploadFile)
                    .allowOverwrite(true)
                    .build(request);

            uploadManager.upload(uploadDetails);
            log.info("Upload File : (SUCCESS) {}", AppConfig.LOCAL_FILE_PATH + fileName);
        } catch (Exception e) {
            objectStorageClient.close();
            init();
            log.error("=========> Client instance has been Closed => upload failed", e);
            throw e;
        }
    }

    public void delete(String apiFilePath, OciBucket bucket) {

        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucketName(getBucketName(bucket))
                    .namespaceName(AppConfig.STORAGE_BUCKET_NAMESPACE)
                    .objectName(apiFilePath)
                    .build();

            objectStorageClient.deleteObject(request);
        } catch (Exception e) {
            objectStorageClient.close();
            init();
            log.error("=========> Client instance has been Closed => delete failed", e);
            throw e;
        }
    }

    private String getBucketName(OciBucket bucket) {

        return switch (bucket) {
            case PUBLIC -> AppConfig.PUBLIC_BUCKET_NAME;
            case PRIVATE -> AppConfig.PRIVATE_BUCKET_NAME;
        };
    }
}
