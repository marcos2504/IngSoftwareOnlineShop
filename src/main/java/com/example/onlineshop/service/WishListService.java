package com.example.onlineshop.service;

import com.example.onlineshop.domain.WishList;
import com.example.onlineshop.repository.WishListRepository;
import com.example.onlineshop.service.dto.WishListDTO;
import com.example.onlineshop.service.mapper.WishListMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.onlineshop.domain.WishList}.
 */
@Service
@Transactional
public class WishListService {

    private static final Logger LOG = LoggerFactory.getLogger(WishListService.class);

    private final WishListRepository wishListRepository;

    private final WishListMapper wishListMapper;

    public WishListService(WishListRepository wishListRepository, WishListMapper wishListMapper) {
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
    }

    /**
     * Save a wishList.
     *
     * @param wishListDTO the entity to save.
     * @return the persisted entity.
     */
    public WishListDTO save(WishListDTO wishListDTO) {
        LOG.debug("Request to save WishList : {}", wishListDTO);
        WishList wishList = wishListMapper.toEntity(wishListDTO);
        wishList = wishListRepository.save(wishList);
        return wishListMapper.toDto(wishList);
    }

    /**
     * Update a wishList.
     *
     * @param wishListDTO the entity to save.
     * @return the persisted entity.
     */
    public WishListDTO update(WishListDTO wishListDTO) {
        LOG.debug("Request to update WishList : {}", wishListDTO);
        WishList wishList = wishListMapper.toEntity(wishListDTO);
        wishList = wishListRepository.save(wishList);
        return wishListMapper.toDto(wishList);
    }

    /**
     * Partially update a wishList.
     *
     * @param wishListDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WishListDTO> partialUpdate(WishListDTO wishListDTO) {
        LOG.debug("Request to partially update WishList : {}", wishListDTO);

        return wishListRepository
            .findById(wishListDTO.getId())
            .map(existingWishList -> {
                wishListMapper.partialUpdate(existingWishList, wishListDTO);

                return existingWishList;
            })
            .map(wishListRepository::save)
            .map(wishListMapper::toDto);
    }

    /**
     * Get all the wishLists.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WishListDTO> findAll() {
        LOG.debug("Request to get all WishLists");
        return wishListRepository.findAll().stream().map(wishListMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the wishLists with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WishListDTO> findAllWithEagerRelationships(Pageable pageable) {
        return wishListRepository.findAllWithEagerRelationships(pageable).map(wishListMapper::toDto);
    }

    /**
     * Get one wishList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WishListDTO> findOne(Long id) {
        LOG.debug("Request to get WishList : {}", id);
        return wishListRepository.findOneWithEagerRelationships(id).map(wishListMapper::toDto);
    }

    /**
     * Delete the wishList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WishList : {}", id);
        wishListRepository.deleteById(id);
    }
}
