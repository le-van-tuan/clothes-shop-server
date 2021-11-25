package vn.triumphstudio.clothesshop.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.triumphstudio.clothesshop.domain.response.FileUploadResponse;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.exception.ResourceNotFoundException;
import vn.triumphstudio.clothesshop.global.Const;
import vn.triumphstudio.clothesshop.service.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation = Paths.get(Const.FILE_UPLOAD_DIR);

    @Override
    public FileUploadResponse uploadFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new BusinessLogicException("Failed to upload empty file.");
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileId = UUID.randomUUID().toString();
            String uploadedFileName = fileId + "." + extension;

            Path destinationFile = rootLocation.resolve(Paths.get(uploadedFileName))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return new FileUploadResponse(fileId, uploadedFileName);
            }
        } catch (IOException e) {
            throw new BusinessLogicException("Failed to upload file.");
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Could not read file: " + fileName);
        }
    }
}
