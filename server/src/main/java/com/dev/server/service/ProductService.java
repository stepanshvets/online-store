package com.dev.server.service;

import com.dev.server.dto.ProductDto;
import com.dev.server.exception.GeneralException;
import com.dev.server.model.Product;
import com.dev.server.repository.CategoryRepository;
import com.dev.server.repository.ProductRepository;
import com.dev.server.util.validation.GeneralValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final GeneralValidator validator;

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Product not found", HttpStatus.BAD_REQUEST));
    }

    @Transactional(readOnly = true)
    public List<Product> get(String query, List<Long> categoryIds, Double priceMin, Double priceMax,
                       String order, Integer page, Integer size) {
        priceMin = (priceMin == null) ? 0 : priceMin;
        priceMax = (priceMax == null) ? Double.MAX_VALUE : priceMax;

        Sort sort;
        if (order != null && order.equals("price.desc")) {
            sort = Sort.by(Sort.Direction.DESC, "price");
        }
        else {
            sort = Sort.by(Sort.Direction.ASC, "price");
        }

        page = (page == null) ? 0 : page;
        size = (size == null || size == 0) ? 5 : size;
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.find(priceMin, priceMax, query, categoryIds, pageable);
    }

    @Transactional
    public Product save(@Valid ProductDto productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCount(productDTO.getCount());
        product.setDescription(productDTO.getDescription());

        product.setCategory(categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new GeneralException("Category not found", HttpStatus.BAD_REQUEST)));

        return productRepository.save(product);
    }

    @Transactional
    public Product put(Long id, @Valid ProductDto productDTO) {
        Product updatedProduct = productRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Product not found", HttpStatus.BAD_REQUEST));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCount(productDTO.getCount());
        product.setDescription(productDTO.getDescription());

        product.setCategory(categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new GeneralException("Category not found", HttpStatus.BAD_REQUEST)));

        product.setId(updatedProduct.getId());
        product.setAvatarPath(updatedProduct.getAvatarPath());

        return productRepository.save(product);

    }

    @Transactional
    public Product patch(Long id, ProductDto productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Product not found", HttpStatus.BAD_REQUEST));

        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getCount() != null) {
            product.setCount(productDTO.getCount());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getCategoryId() != null) {
            product.setCategory(categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new GeneralException("Category not found", HttpStatus.BAD_REQUEST)));
        }

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(product);
        if (constraintViolations.size() > 0) {
            throw new ConstraintViolationException(constraintViolations);
        }

        return product;
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
