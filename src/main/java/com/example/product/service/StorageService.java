package com.example.product.service;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

public interface StorageService {

    StorageResult uploadImage(MultipartFile file) throws IOException;

    boolean deleteImage(String imageId);


    Map<String, Object> getImageInfo(String imageId);


    @Getter
    class StorageResult {
        private String url;
        private String imageId;

        public StorageResult(String url, String imageId) {
            this.url = url;
            this.imageId = imageId;
        }

    }
}
