package com.example.onlineshop.web.rest;

import com.example.onlineshop.repository.WishListRepository;
import com.example.onlineshop.service.WishListService;
import com.example.onlineshop.service.dto.WishListDTO;
import com.example.onlineshop.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.example.onlineshop.domain.WishList}.
 */
@RestController
@RequestMapping("/api/wish-lists")
public class WishListResource {

    private static final Logger LOG = LoggerFactory.getLogger(WishListResource.class);

    private static final String ENTITY_NAME = "wishList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WishListService wishListService;

    private final WishListRepository wishListRepository;

    public WishListResource(WishListService wishListService, WishListRepository wishListRepository) {
        this.wishListService = wishListService;
        this.wishListRepository = wishListRepository;
    }

    /**
     * {@code POST  /wish-lists} : Create a new wishList.
     *
     * @param wishListDTO the wishListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wishListDTO, or with status {@code 400 (Bad Request)} if the wishList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WishListDTO> createWishList(@Valid @RequestBody WishListDTO wishListDTO) throws URISyntaxException {
        LOG.debug("REST request to save WishList : {}", wishListDTO);
        if (wishListDTO.getId() != null) {
            throw new BadRequestAlertException("A new wishList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        wishListDTO = wishListService.save(wishListDTO);
        return ResponseEntity.created(new URI("/api/wish-lists/" + wishListDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, wishListDTO.getId().toString()))
            .body(wishListDTO);
    }

    /**
     * {@code PUT  /wish-lists/:id} : Updates an existing wishList.
     *
     * @param id the id of the wishListDTO to save.
     * @param wishListDTO the wishListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishListDTO,
     * or with status {@code 400 (Bad Request)} if the wishListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wishListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WishListDTO> updateWishList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WishListDTO wishListDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WishList : {}, {}", id, wishListDTO);
        if (wishListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        wishListDTO = wishListService.update(wishListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wishListDTO.getId().toString()))
            .body(wishListDTO);
    }

    /**
     * {@code PATCH  /wish-lists/:id} : Partial updates given fields of an existing wishList, field will ignore if it is null
     *
     * @param id the id of the wishListDTO to save.
     * @param wishListDTO the wishListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wishListDTO,
     * or with status {@code 400 (Bad Request)} if the wishListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the wishListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the wishListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WishListDTO> partialUpdateWishList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WishListDTO wishListDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WishList partially : {}, {}", id, wishListDTO);
        if (wishListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wishListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WishListDTO> result = wishListService.partialUpdate(wishListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wishListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wish-lists} : get all the wishLists.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wishLists in body.
     */
    @GetMapping("")
    public List<WishListDTO> getAllWishLists(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all WishLists");
        return wishListService.findAll();
    }

    /**
     * {@code GET  /wish-lists/:id} : get the "id" wishList.
     *
     * @param id the id of the wishListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wishListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WishListDTO> getWishList(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WishList : {}", id);
        Optional<WishListDTO> wishListDTO = wishListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wishListDTO);
    }

    /**
     * {@code DELETE  /wish-lists/:id} : delete the "id" wishList.
     *
     * @param id the id of the wishListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishList(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WishList : {}", id);
        wishListService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
