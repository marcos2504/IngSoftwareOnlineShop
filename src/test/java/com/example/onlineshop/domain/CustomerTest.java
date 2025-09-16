package com.example.onlineshop.domain;

import static com.example.onlineshop.domain.AddressTestSamples.*;
import static com.example.onlineshop.domain.CustomerTestSamples.*;
import static com.example.onlineshop.domain.WishListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.onlineshop.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void addressesTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        customer.addAddresses(addressBack);
        assertThat(customer.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCustomer()).isEqualTo(customer);

        customer.removeAddresses(addressBack);
        assertThat(customer.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCustomer()).isNull();

        customer.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(customer.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCustomer()).isEqualTo(customer);

        customer.setAddresses(new HashSet<>());
        assertThat(customer.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCustomer()).isNull();
    }

    @Test
    void wishListsTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        WishList wishListBack = getWishListRandomSampleGenerator();

        customer.addWishLists(wishListBack);
        assertThat(customer.getWishLists()).containsOnly(wishListBack);
        assertThat(wishListBack.getCustomer()).isEqualTo(customer);

        customer.removeWishLists(wishListBack);
        assertThat(customer.getWishLists()).doesNotContain(wishListBack);
        assertThat(wishListBack.getCustomer()).isNull();

        customer.wishLists(new HashSet<>(Set.of(wishListBack)));
        assertThat(customer.getWishLists()).containsOnly(wishListBack);
        assertThat(wishListBack.getCustomer()).isEqualTo(customer);

        customer.setWishLists(new HashSet<>());
        assertThat(customer.getWishLists()).doesNotContain(wishListBack);
        assertThat(wishListBack.getCustomer()).isNull();
    }
}
