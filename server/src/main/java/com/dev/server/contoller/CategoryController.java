package com.dev.server.contoller;

import com.dev.server.dto.CategoryDto;
import com.dev.server.model.Category;
import com.dev.server.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll() {
        return CategoryDto.toListDto(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        return CategoryDto.toDto(categoryService.getById(id));
    }

    @PostMapping
    public CategoryDto save(@RequestBody CategoryDto categoryDTO) {
        return CategoryDto.toDto(categoryService.save(categoryDTO));
    }

    @PatchMapping("/{id}")
    public CategoryDto patch(@PathVariable Long id, @RequestBody CategoryDto categoryDTO) {
        return CategoryDto.toDto(categoryService.patch(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
