package com.sfood.eai.external.component.oci;

import com.sfood.eai.external.component.document.XmlDocument;
import com.sfood.eai.external.config.AppConfig;
import com.sfood.eai.external.type.OciBucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class StorageManager {

    private final StorageClient storageClient;

    public StorageManager(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public String getFilePublicUrl(String storageFileName) {
        return AppConfig.STORAGE_PUBLIC_URL + storageFileName.replace("/", AppConfig.STORAGE_PATH_SEPARATOR);
    }

    public void uploadXmlFile(final XmlDocument document, final String storagePath, final String fileName) throws Exception {

        boolean result = document.saveXml(fileName);

        if (result) {
            storageClient.upload(fileName, getFullFileName(storagePath, fileName), OciBucket.PUBLIC);
        }
    }

    public void deleteXmlFile(final String storagePath, final String fileName) throws Exception {
        storageClient.upload(fileName, storagePath + fileName, OciBucket.PRIVATE);
        storageClient.delete(getFullFileName(storagePath, fileName), OciBucket.PUBLIC);
    }

    private String getFullFileName(String storagePath, String fileName) {
        return storagePath + fileName;
    }
}
