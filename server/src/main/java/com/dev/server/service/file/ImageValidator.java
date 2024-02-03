package com.dev.server.service.file;

import com.dev.server.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Component("FileValidator")
public class ImageValidator implements FileValidator {
    @Value("${app.config.upload.image.max-size-mb}")
    private Integer maxSizeMB;

    @Override
    public void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("File is empty");
        }

        if (file.getSize() > maxSizeMB * 1024 * 1024) {
            throw new StorageException("File is more than " + maxSizeMB + " MB");
        }


        if (!List.of(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE).contains(file.getContentType())) {
            throw new StorageException("File has not valid extension");
        }

    }
}
