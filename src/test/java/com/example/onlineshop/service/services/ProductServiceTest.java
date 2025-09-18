package com.example.onlineshop.service.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.example.onlineshop.domain.Product;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.service.ProductService;
import com.example.onlineshop.service.dto.ProductDTO;
import com.example.onlineshop.service.mapper.ProductMapper;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveProduct() {
        // given
        ProductDTO dto = new ProductDTO();
        dto.setTitle("Notebook");
        dto.setPrice(BigDecimal.valueOf(1500));

        Product product = new Product();
        product.setTitle("Notebook");
        product.setPrice(BigDecimal.valueOf(1500));

        when(productMapper.toEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(dto);

        // when
        ProductDTO result = productService.save(dto);

        // then
        assertThat(result.getTitle()).isEqualTo("Notebook");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(1500));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void shouldFindProductById() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Celular");
        product.setPrice(BigDecimal.valueOf(800));

        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setTitle("Celular");
        dto.setPrice(BigDecimal.valueOf(800));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(dto);

        // when
        Optional<ProductDTO> result = productService.findOne(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Celular");
        assertThat(result.get().getPrice()).isEqualTo(BigDecimal.valueOf(800));
    }
}
