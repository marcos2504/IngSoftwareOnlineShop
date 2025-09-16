package com.example.onlineshop.domain;

import static com.example.onlineshop.domain.CustomerTestSamples.*;
import static com.example.onlineshop.domain.ProductTestSamples.*;
import static com.example.onlineshop.domain.WishListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.onlineshop.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WishListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WishList.class);
        WishList wishList1 = getWishListSample1();
        WishList wishList2 = new WishList();
        assertThat(wishList1).isNotEqualTo(wishList2);

        wishList2.setId(wishList1.getId());
        assertThat(wishList1).isEqualTo(wishList2);

        wishList2 = getWishListSample2();
        assertThat(wishList1).isNotEqualTo(wishList2);
    }

    @Test
    void productsTest() {
        WishList wishList = getWishListRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        wishList.addProducts(productBack);
        assertThat(wishList.getProducts()).containsOnly(productBack);

        wishList.removeProducts(productBack);
        assertThat(wishList.getProducts()).doesNotContain(productBack);

        wishList.products(new HashSet<>(Set.of(productBack)));
        assertThat(wishList.getProducts()).containsOnly(productBack);

        wishList.setProducts(new HashSet<>());
        assertThat(wishList.getProducts()).doesNotContain(productBack);
    }

    @Test
    void customerTest() {
        WishList wishList = getWishListRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        wishList.setCustomer(customerBack);
        assertThat(wishList.getCustomer()).isEqualTo(customerBack);

        wishList.customer(null);
        assertThat(wishList.getCustomer()).isNull();
    }
}
