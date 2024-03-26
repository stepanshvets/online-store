package com.dev.server.contoller;

import com.dev.server.dto.ProductDto;
import com.dev.server.model.Product;
import com.dev.server.service.ProductImageService;
import com.dev.server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductImageService productImageService;

    @GetMapping
    public List<ProductDto> get(@RequestParam(required = false) String query,
                                @RequestParam(required = false) List<Long> categoryIds,
                                @RequestParam(required = false) Double priceMin,
                                @RequestParam(required = false) Double priceMax,
                                @RequestParam(required = false) String order,
                                @RequestParam(required = false) Integer page,
                                @RequestParam(required = false) Integer size) {
        return ProductDto.toListDto(productService.get(query, categoryIds, priceMin, priceMax, order, page, size));
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return ProductDto.toDto(productService.getById(id));
    }

    @PostMapping
    public ProductDto save(@RequestBody ProductDto productDTO) {
        return ProductDto.toDto(productService.save(productDTO));
    }

    @PutMapping("/{id}")
    public ProductDto put(@PathVariable Long id, @RequestBody ProductDto productDTO) {
        return ProductDto.toDto(productService.put(id, productDTO));
    }

    @PatchMapping("/{id}")
    public ProductDto patch(@PathVariable Long id, @RequestBody ProductDto productDTO) {
        return ProductDto.toDto(productService.patch(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getMainImage(@PathVariable Long id) throws IOException {
        Resource resource = productImageService.loadMainImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(resource.getFile().toPath())))
                .body(resource);
    }

    @PostMapping("/{id}/image")
    public void updateMainImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        productImageService.updateMainImage(id, file);
    }
}
