package com.dev.server.service;

import com.dev.server.exception.GeneralException;
import com.dev.server.model.Product;
import com.dev.server.repository.ProductRepository;
import com.dev.server.service.file.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductRepository productRepository;
    private final FileSystemStorageService fileSystemStorageService;

    @Value("${app.config.upload.relative-path.products}")
    private String directoryName;

    public Path directoryPath;

    @PostConstruct
    public void init() throws IOException {
        this.directoryPath = fileSystemStorageService.getDirectoryPath(directoryName);
        fileSystemStorageService.initDirectory(this.directoryPath);
    }

    @Transactional(readOnly = true)
    public Resource loadMainImage(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Product not found", HttpStatus.BAD_REQUEST));

        return fileSystemStorageService.load(Path.of(product.getAvatarPath()));
    }

    @Transactional
    public void updateMainImage(Long id, MultipartFile file) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Product not found", HttpStatus.BAD_REQUEST));

        Path path = fileSystemStorageService.save(file, directoryPath , product.getName() + product.getId());

        product.setAvatarPath(path.toString());
        productRepository.save(product);
    }
}
