package com.dev.server.service;

import com.dev.server.service.file.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class TestFileService {
    private final FileSystemStorageService fileSystemStorageService;

    @Value("${app.config.upload.relative-path.test}")
    private String directoryName;

    public Path directoryPath;

    @PostConstruct
    public void init() throws IOException {
        this.directoryPath = fileSystemStorageService.getDirectoryPath(directoryName);
        fileSystemStorageService.initDirectory(this.directoryPath);
    }

    public void save(MultipartFile file) {
        fileSystemStorageService.save(file, directoryPath , "test");
    }

    public Resource get() {
        return fileSystemStorageService.load(directoryPath.resolve(Paths.get("test.jpg")));
    }

    public void delete() {
        fileSystemStorageService.delete(directoryPath.resolve(Paths.get("test.jpg")));
    }
}
