package vn.triumphstudio.clothesshop.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.response.FileUploadResponse;

public interface FileStorageService {
    FileUploadResponse uploadFile(MultipartFile file);

    Resource loadFile(String fileName);

    void deleteFile(String fileName);
}
