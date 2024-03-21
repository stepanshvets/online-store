package com.dev.server.contoller;

import com.dev.server.dto.CategoryDTO;
import com.dev.server.dto.CategoryToSaveDTO;
import com.dev.server.model.Category;
import com.dev.server.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return convertToListCategoryDTO(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public CategoryDTO getById(@PathVariable Long id) {
        return convertToCategoryDTO(categoryService.getById(id));
    }

    @PostMapping
    public CategoryDTO save(@RequestBody CategoryDTO categoryDTO) {
        return convertToCategoryDTO(categoryService.save(categoryDTO));
    }

    @PatchMapping("/{id}")
    public CategoryDTO patch(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return convertToCategoryDTO(categoryService.patch(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    protected CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }

    protected List<CategoryDTO> convertToListCategoryDTO(List<Category> list) {
        return list.stream().map(category -> convertToCategoryDTO(category))
                .collect(Collectors.toList());
    }
}
