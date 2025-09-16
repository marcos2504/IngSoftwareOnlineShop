package com.example.onlineshop.service.mapper;

import com.example.onlineshop.domain.Category;
import com.example.onlineshop.domain.Product;
import com.example.onlineshop.domain.WishList;
import com.example.onlineshop.service.dto.CategoryDTO;
import com.example.onlineshop.service.dto.ProductDTO;
import com.example.onlineshop.service.dto.WishListDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoryIdSet")
    @Mapping(target = "wishLists", source = "wishLists", qualifiedByName = "wishListIdSet")
    ProductDTO toDto(Product s);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "removeCategories", ignore = true)
    @Mapping(target = "wishLists", ignore = true)
    @Mapping(target = "removeWishLists", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("categoryIdSet")
    default Set<CategoryDTO> toDtoCategoryIdSet(Set<Category> category) {
        return category.stream().map(this::toDtoCategoryId).collect(Collectors.toSet());
    }

    @Named("wishListId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WishListDTO toDtoWishListId(WishList wishList);

    @Named("wishListIdSet")
    default Set<WishListDTO> toDtoWishListIdSet(Set<WishList> wishList) {
        return wishList.stream().map(this::toDtoWishListId).collect(Collectors.toSet());
    }
}
