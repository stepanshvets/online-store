package com.dev.server.dto;

import com.dev.server.model.Product;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDto {
    private Long id;

    @NotBlank
    @Size(max = 128)
    private String name;

    private Double price;

    private Long count;

    @Size(max = 2048)
    private String description;

    private String avatarPath;

    private CategoryDto categoryDTO;

    @NotNull
    private Long categoryId;

    public static ProductDto toDto(Product product) {
        ProductDto productDTO = new ProductDto();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setCount(product.getCount());
        productDTO.setDescription(product.getDescription());
        productDTO.setAvatarPath(product.getAvatarPath());
        productDTO.setCategoryId(product.getCategory().getId());
        //productDTO.setCategoryDTO(CategoryDto.toDto(product.getCategory()));
        return productDTO;
    }

    public static List<ProductDto> toListDto(List<Product> list) {
        return list.stream().map(product -> toDto(product))
                .collect(Collectors.toList());
    }
}
