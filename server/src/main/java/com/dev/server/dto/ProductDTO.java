package com.dev.server.dto;

import com.dev.server.model.Category;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank
    private String name;

    private Double price;

    private Long count;

    private String description;

    private String avatarPath;

    private CategoryDTO categoryDTO;

    @NotNull
    private Long categoryId;
}
