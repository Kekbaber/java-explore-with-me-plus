package ru.practicum.main.service.category;

import jakarta.persistence.criteria.CriteriaBuilder;
import ru.practicum.main.dto.request.NewCategoryDto;
import ru.practicum.main.dto.response.CategoryDto;

import java.util.List;

public interface CategoryService {
    public CategoryDto addCategory(NewCategoryDto newCategory);

    public CategoryDto updateCategory(Long catId, NewCategoryDto updateCategory);

    public void deleteCategory(Long catId);

    public List<CategoryDto> getCategories(Integer from, Integer size);

    public CategoryDto getCategory(Long catId);
}
