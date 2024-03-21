package com.dev.server.contoller;

import com.dev.server.dto.CategoryDTO;
import com.dev.server.dto.ProductDTO;
import com.dev.server.model.Category;
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
    public List<ProductDTO> get(@RequestParam(required = false) String query,
                          @RequestParam(required = false) List<Long> categoryIds,
                          @RequestParam(required = false) Double priceMin,
                          @RequestParam(required = false) Double priceMax,
                          @RequestParam(required = false) String order,
                          @RequestParam(required = false) Integer page,
                          @RequestParam(required = false) Integer size) {
        System.out.println(categoryIds);
        return convertToListProductDTO(productService.get(query, categoryIds, priceMin, priceMax, order, page, size));
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Long id) {
        return convertToProductDTO(productService.getById(id));
    }

    @PostMapping
    public ProductDTO save(@RequestBody ProductDTO productDTO) {
        return convertToProductDTO(productService.save(productDTO));
    }

    @PutMapping("/{id}")
    public ProductDTO put(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return convertToProductDTO(productService.put(id, productDTO));
    }

    @PatchMapping("/{id}")
    public ProductDTO patch(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return convertToProductDTO(productService.patch(id, productDTO));
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

    protected ProductDTO convertToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setCount(product.getCount());
        productDTO.setDescription(product.getDescription());
        productDTO.setAvatarPath(product.getAvatarPath());
//        productDTO.setCategoryDTO(categoryService.convertToCategoryDTO(product.getCategory()));
        return productDTO;
    }

    protected List<ProductDTO> convertToListProductDTO(List<Product> list) {
        return list.stream().map(product -> convertToProductDTO(product))
                .collect(Collectors.toList());
    }

    protected Product convertToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCount(productDTO.getCount());
        product.setDescription(productDTO.getDescription());
        return product;
    }
}
