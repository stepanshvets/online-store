package com.dev.server.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileSystemStorageService {
    private final Path rootDirectoryPath;

    private final FileValidator defaultValidator;

    public FileSystemStorageService(@Value("${app.config.upload.path}") String path, FileValidator defaultValidator) {
        this.rootDirectoryPath = Paths.get(path).normalize();
        this.defaultValidator = defaultValidator;
    }

    @PostConstruct
    public void initializeDirectory() throws IOException {
        try {
            Files.createDirectory(rootDirectoryPath);
        } catch (FileAlreadyExistsException ignored) {
        } catch (Exception e) {
            throw e;
        }
    }

    public Path getDirectoryPath(String directoryName) {
        return rootDirectoryPath.resolve(directoryName);
    }

    public void initDirectory(Path path) throws IOException {
        try {
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException ignored) {
        } catch (Exception e) {
            throw e;
        }
    }

    public void save(MultipartFile file, Path directoryPath, String fileName) {
        save(file, directoryPath, fileName, defaultValidator);
    }

    public void save(MultipartFile file, Path directoryPath, String fileName, FileValidator fileValidator) {
        try {
            fileValidator.validate(file);

            Path destinationFile = directoryPath.resolve(fileName + "." +
                    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        } catch (Exception e) {
            throw e;
        }
    }

    public Resource load(Path path) {
        Resource resource = new FileSystemResource(path);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }
        throw new RuntimeException("Failed to load file");
    }

    public void delete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store delete", e);
        } catch (Exception e) {
            throw e;
        }
    }
}
