package com.example.onlineshop.service.mapper;

import com.example.onlineshop.domain.Customer;
import com.example.onlineshop.domain.Product;
import com.example.onlineshop.domain.WishList;
import com.example.onlineshop.service.dto.CustomerDTO;
import com.example.onlineshop.service.dto.ProductDTO;
import com.example.onlineshop.service.dto.WishListDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WishList} and its DTO {@link WishListDTO}.
 */
@Mapper(componentModel = "spring")
public interface WishListMapper extends EntityMapper<WishListDTO, WishList> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productTitleSet")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    WishListDTO toDto(WishList s);

    @Mapping(target = "removeProducts", ignore = true)
    WishList toEntity(WishListDTO wishListDTO);

    @Named("productTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ProductDTO toDtoProductTitle(Product product);

    @Named("productTitleSet")
    default Set<ProductDTO> toDtoProductTitleSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductTitle).collect(Collectors.toSet());
    }

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}
