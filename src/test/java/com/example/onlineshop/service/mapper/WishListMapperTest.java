package com.example.onlineshop.service.mapper;

import static com.example.onlineshop.domain.WishListAsserts.*;
import static com.example.onlineshop.domain.WishListTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WishListMapperTest {

    private WishListMapper wishListMapper;

    @BeforeEach
    void setUp() {
        wishListMapper = new WishListMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWishListSample1();
        var actual = wishListMapper.toEntity(wishListMapper.toDto(expected));
        assertWishListAllPropertiesEquals(expected, actual);
    }
}
