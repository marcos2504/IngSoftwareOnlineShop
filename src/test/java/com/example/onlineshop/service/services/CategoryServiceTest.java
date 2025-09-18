package com.example.onlineshop.service.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.example.onlineshop.domain.Category;
import com.example.onlineshop.repository.CategoryRepository;
import com.example.onlineshop.service.CategoryService;
import com.example.onlineshop.service.dto.CategoryDTO;
import com.example.onlineshop.service.mapper.CategoryMapper;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveCategory() {
        // given
        CategoryDTO dto = new CategoryDTO();
        dto.setDescription("Electrónica");
        dto.setSortOrder(1);
        dto.setDateAdded(LocalDate.now());

        Category category = new Category();
        category.setDescription("Electrónica");
        category.setSortOrder(1);
        category.setDateAdded(dto.getDateAdded());

        when(categoryMapper.toEntity(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(dto);

        // when
        CategoryDTO result = categoryService.save(dto);

        // then
        assertThat(result.getDescription()).isEqualTo("Electrónica");
        assertThat(result.getSortOrder()).isEqualTo(1);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void shouldFindCategoryById() {
        // given
        Category category = new Category();
        category.setId(1L);
        category.setDescription("Hogar");
        category.setSortOrder(2);

        CategoryDTO dto = new CategoryDTO();
        dto.setId(1L);
        dto.setDescription("Hogar");
        dto.setSortOrder(2);

        when(categoryRepository.findOneWithEagerRelationships(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);

        // when
        Optional<CategoryDTO> result = categoryService.findOne(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getDescription()).isEqualTo("Hogar");
        assertThat(result.get().getSortOrder()).isEqualTo(2);
    }
}
