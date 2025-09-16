package com.example.onlineshop.domain;

import static com.example.onlineshop.domain.CategoryTestSamples.*;
import static com.example.onlineshop.domain.ProductTestSamples.*;
import static com.example.onlineshop.domain.WishListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.onlineshop.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoriesTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.addCategories(categoryBack);
        assertThat(product.getCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getProducts()).containsOnly(product);

        product.removeCategories(categoryBack);
        assertThat(product.getCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getProducts()).doesNotContain(product);

        product.categories(new HashSet<>(Set.of(categoryBack)));
        assertThat(product.getCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getProducts()).containsOnly(product);

        product.setCategories(new HashSet<>());
        assertThat(product.getCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getProducts()).doesNotContain(product);
    }

    @Test
    void wishListsTest() {
        Product product = getProductRandomSampleGenerator();
        WishList wishListBack = getWishListRandomSampleGenerator();

        product.addWishLists(wishListBack);
        assertThat(product.getWishLists()).containsOnly(wishListBack);
        assertThat(wishListBack.getProducts()).containsOnly(product);

        product.removeWishLists(wishListBack);
        assertThat(product.getWishLists()).doesNotContain(wishListBack);
        assertThat(wishListBack.getProducts()).doesNotContain(product);

        product.wishLists(new HashSet<>(Set.of(wishListBack)));
        assertThat(product.getWishLists()).containsOnly(wishListBack);
        assertThat(wishListBack.getProducts()).containsOnly(product);

        product.setWishLists(new HashSet<>());
        assertThat(product.getWishLists()).doesNotContain(wishListBack);
        assertThat(wishListBack.getProducts()).doesNotContain(product);
    }
}
