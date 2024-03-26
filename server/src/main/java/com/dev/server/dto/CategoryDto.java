package com.dev.server.dto;

import com.dev.server.model.Category;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryDto {
    private Long id;

    @NotBlank
    private String name;

    public static CategoryDto toDto(Category category) {
        CategoryDto categoryDTO = new CategoryDto();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    public static List<CategoryDto> toListDto(List<Category> list) {
        return list.stream().map(category -> toDto(category))
                .collect(Collectors.toList());
    }
}


