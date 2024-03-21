package com.dev.server.service;

import com.dev.server.dto.CategoryDTO;
import com.dev.server.exception.GeneralException;
import com.dev.server.model.Category;
import com.dev.server.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElse(null); //Throw(() -> new GeneralException("Category not found", HttpStatus.BAD_REQUEST));
    }

    @Transactional
    public Category save(@Valid CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new GeneralException("Category already exist", HttpStatus.BAD_REQUEST);
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public Category patch(Long id, @Valid CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Category not found", HttpStatus.BAD_REQUEST));

        if (categoryDTO.getName() != null) {
            if (!category.getName().equals(categoryDTO.getName()) && categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
                throw new GeneralException("Category already exist", HttpStatus.BAD_REQUEST);
            }

            category.setName(categoryDTO.getName());
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
