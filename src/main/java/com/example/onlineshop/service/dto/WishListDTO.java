package com.example.onlineshop.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.example.onlineshop.domain.WishList} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WishListDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private Boolean restricted;

    private Set<ProductDTO> products = new HashSet<>();

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WishListDTO)) {
            return false;
        }

        WishListDTO wishListDTO = (WishListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wishListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WishListDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", restricted='" + getRestricted() + "'" +
            ", products=" + getProducts() +
            ", customer=" + getCustomer() +
            "}";
    }
}
