package com.dev.server.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidator {
    void validate(MultipartFile file);
}
