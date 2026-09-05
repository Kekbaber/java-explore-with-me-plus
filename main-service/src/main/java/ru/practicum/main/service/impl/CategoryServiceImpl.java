package ru.practicum.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.request.NewCategoryDto;
import ru.practicum.main.dto.response.CategoryDto;
import ru.practicum.main.exception.model.ConflictException;
import ru.practicum.main.exception.model.NotFoundException;
import ru.practicum.main.model.Category;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.service.CategoryService;
import ru.practicum.main.service.mapper.CategoryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private static final String CATEGORY_NOT_FOUND_EXCEPTION = "Category was not found with id=";
    private static final String CATEGORY_CONFLICT_EXCEPTION = "Category already exists with name=";

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategory) {
        if (categoryRepository.existsByName(newCategory.getName())) {
            throw new ConflictException(CATEGORY_CONFLICT_EXCEPTION + newCategory.getName());
        }

        Category category = CategoryMapper.toEntity(newCategory);

        category = categoryRepository.save(category);

        return CategoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto update) {
        Category oldCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION + catId));

        Category updateCategory = CategoryMapper.toEntity(update);

        if (categoryRepository.existsByNameAndIdNot(updateCategory.getName(), catId)) {
            throw new ConflictException(CATEGORY_CONFLICT_EXCEPTION + updateCategory.getName());
        }

        oldCategory.setName(updateCategory.getName());

        oldCategory = categoryRepository.save(oldCategory);

        return CategoryMapper.toDto(oldCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        if (categoryRepository.existsById(catId)) {
            categoryRepository.deleteById(catId);
        } else {
            throw new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION + catId);
        }
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        // Получаем страницу с категориями
        Page<Category> page = categoryRepository.findAll(pageable);

        // Возвращаем список категорий с этой страницы
        return page.getContent().stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND_EXCEPTION + catId));

        return CategoryMapper.toDto(category);
    }
}
