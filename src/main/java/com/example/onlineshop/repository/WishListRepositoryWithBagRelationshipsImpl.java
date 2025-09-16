package com.example.onlineshop.repository;

import com.example.onlineshop.domain.WishList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class WishListRepositoryWithBagRelationshipsImpl implements WishListRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String WISHLISTS_PARAMETER = "wishLists";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<WishList> fetchBagRelationships(Optional<WishList> wishList) {
        return wishList.map(this::fetchProducts);
    }

    @Override
    public Page<WishList> fetchBagRelationships(Page<WishList> wishLists) {
        return new PageImpl<>(fetchBagRelationships(wishLists.getContent()), wishLists.getPageable(), wishLists.getTotalElements());
    }

    @Override
    public List<WishList> fetchBagRelationships(List<WishList> wishLists) {
        return Optional.of(wishLists).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    WishList fetchProducts(WishList result) {
        return entityManager
            .createQuery("select wishList from WishList wishList left join fetch wishList.products where wishList.id = :id", WishList.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<WishList> fetchProducts(List<WishList> wishLists) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, wishLists.size()).forEach(index -> order.put(wishLists.get(index).getId(), index));
        List<WishList> result = entityManager
            .createQuery(
                "select wishList from WishList wishList left join fetch wishList.products where wishList in :wishLists",
                WishList.class
            )
            .setParameter(WISHLISTS_PARAMETER, wishLists)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
