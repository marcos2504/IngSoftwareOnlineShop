package com.example.onlineshop.repository;

import com.example.onlineshop.domain.WishList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface WishListRepositoryWithBagRelationships {
    Optional<WishList> fetchBagRelationships(Optional<WishList> wishList);

    List<WishList> fetchBagRelationships(List<WishList> wishLists);

    Page<WishList> fetchBagRelationships(Page<WishList> wishLists);
}
