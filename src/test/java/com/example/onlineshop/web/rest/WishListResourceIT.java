package com.example.onlineshop.web.rest;

import static com.example.onlineshop.domain.WishListAsserts.*;
import static com.example.onlineshop.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.onlineshop.IntegrationTest;
import com.example.onlineshop.domain.WishList;
import com.example.onlineshop.repository.WishListRepository;
import com.example.onlineshop.service.WishListService;
import com.example.onlineshop.service.dto.WishListDTO;
import com.example.onlineshop.service.mapper.WishListMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WishListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WishListResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RESTRICTED = false;
    private static final Boolean UPDATED_RESTRICTED = true;

    private static final String ENTITY_API_URL = "/api/wish-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WishListRepository wishListRepository;

    @Mock
    private WishListRepository wishListRepositoryMock;

    @Autowired
    private WishListMapper wishListMapper;

    @Mock
    private WishListService wishListServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWishListMockMvc;

    private WishList wishList;

    private WishList insertedWishList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishList createEntity() {
        return new WishList().title(DEFAULT_TITLE).restricted(DEFAULT_RESTRICTED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WishList createUpdatedEntity() {
        return new WishList().title(UPDATED_TITLE).restricted(UPDATED_RESTRICTED);
    }

    @BeforeEach
    void initTest() {
        wishList = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWishList != null) {
            wishListRepository.delete(insertedWishList);
            insertedWishList = null;
        }
    }

    @Test
    @Transactional
    void createWishList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);
        var returnedWishListDTO = om.readValue(
            restWishListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishListDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WishListDTO.class
        );

        // Validate the WishList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWishList = wishListMapper.toEntity(returnedWishListDTO);
        assertWishListUpdatableFieldsEquals(returnedWishList, getPersistedWishList(returnedWishList));

        insertedWishList = returnedWishList;
    }

    @Test
    @Transactional
    void createWishListWithExistingId() throws Exception {
        // Create the WishList with an existing ID
        wishList.setId(1L);
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wishList.setTitle(null);

        // Create the WishList, which fails.
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        restWishListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishListDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWishLists() throws Exception {
        // Initialize the database
        insertedWishList = wishListRepository.saveAndFlush(wishList);

        // Get all the wishListList
        restWishListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishList.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].restricted").value(hasItem(DEFAULT_RESTRICTED)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWishListsWithEagerRelationshipsIsEnabled() throws Exception {
        when(wishListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWishListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(wishListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWishListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(wishListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWishListMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(wishListRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWishList() throws Exception {
        // Initialize the database
        insertedWishList = wishListRepository.saveAndFlush(wishList);

        // Get the wishList
        restWishListMockMvc
            .perform(get(ENTITY_API_URL_ID, wishList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wishList.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.restricted").value(DEFAULT_RESTRICTED));
    }

    @Test
    @Transactional
    void getNonExistingWishList() throws Exception {
        // Get the wishList
        restWishListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWishList() throws Exception {
        // Initialize the database
        insertedWishList = wishListRepository.saveAndFlush(wishList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishList
        WishList updatedWishList = wishListRepository.findById(wishList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWishList are not directly saved in db
        em.detach(updatedWishList);
        updatedWishList.title(UPDATED_TITLE).restricted(UPDATED_RESTRICTED);
        WishListDTO wishListDTO = wishListMapper.toDto(updatedWishList);

        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishListDTO))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWishListToMatchAllProperties(updatedWishList);
    }

    @Test
    @Transactional
    void putNonExistingWishList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishList.setId(longCount.incrementAndGet());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWishList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishList.setId(longCount.incrementAndGet());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWishList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishList.setId(longCount.incrementAndGet());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWishListWithPatch() throws Exception {
        // Initialize the database
        insertedWishList = wishListRepository.saveAndFlush(wishList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishList using partial update
        WishList partialUpdatedWishList = new WishList();
        partialUpdatedWishList.setId(wishList.getId());

        partialUpdatedWishList.restricted(UPDATED_RESTRICTED);

        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWishList))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWishListUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWishList, wishList), getPersistedWishList(wishList));
    }

    @Test
    @Transactional
    void fullUpdateWishListWithPatch() throws Exception {
        // Initialize the database
        insertedWishList = wishListRepository.saveAndFlush(wishList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishList using partial update
        WishList partialUpdatedWishList = new WishList();
        partialUpdatedWishList.setId(wishList.getId());

        partialUpdatedWishList.title(UPDATED_TITLE).restricted(UPDATED_RESTRICTED);

        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWishList))
            )
            .andExpect(status().isOk());

        // Validate the WishList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWishListUpdatableFieldsEquals(partialUpdatedWishList, getPersistedWishList(partialUpdatedWishList));
    }

    @Test
    @Transactional
    void patchNonExistingWishList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishList.setId(longCount.incrementAndGet());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wishListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWishList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishList.setId(longCount.incrementAndGet());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wishListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWishList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishList.setId(longCount.incrementAndGet());

        // Create the WishList
        WishListDTO wishListDTO = wishListMapper.toDto(wishList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wishListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WishList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWishList() throws Exception {
        // Initialize the database
        insertedWishList = wishListRepository.saveAndFlush(wishList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the wishList
        restWishListMockMvc
            .perform(delete(ENTITY_API_URL_ID, wishList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return wishListRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected WishList getPersistedWishList(WishList wishList) {
        return wishListRepository.findById(wishList.getId()).orElseThrow();
    }

    protected void assertPersistedWishListToMatchAllProperties(WishList expectedWishList) {
        assertWishListAllPropertiesEquals(expectedWishList, getPersistedWishList(expectedWishList));
    }

    protected void assertPersistedWishListToMatchUpdatableProperties(WishList expectedWishList) {
        assertWishListAllUpdatablePropertiesEquals(expectedWishList, getPersistedWishList(expectedWishList));
    }
}
