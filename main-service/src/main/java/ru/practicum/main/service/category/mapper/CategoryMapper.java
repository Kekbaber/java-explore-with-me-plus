package ru.practicum.main.service.category.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.dto.request.NewCategoryDto;
import ru.practicum.main.dto.response.CategoryDto;
import ru.practicum.main.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
    public static Category mapToCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static Category mapToCategory(CategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        if (category != null && category.getName() != null && category.getId() != null) {
            return CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        } else {
            return null;
        }
    }
}
