package org.nmcpye.activitiesmanagement.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.nmcpye.activitiesmanagement.repository.ImageAlbumRepository;
import org.nmcpye.activitiesmanagement.service.ImageAlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ImageAlbumResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ImageAlbumResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Date DEFAULT_CREATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_CREATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Date DEFAULT_LAST_UPDATED = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_LAST_UPDATED = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE_UID = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE_UID = "BBBBBBBBBB";

    private static final String DEFAULT_SUBTITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/image-albums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageAlbumRepository imageAlbumRepository;

    @Mock
    private ImageAlbumRepository imageAlbumRepositoryMock;

    @Mock
    private ImageAlbumService imageAlbumServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageAlbumMockMvc;

    private ImageAlbum imageAlbum;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageAlbum createEntity(EntityManager em) {
        ImageAlbum imageAlbum = new ImageAlbum()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .title(DEFAULT_TITLE)
            .coverImageUid(DEFAULT_COVER_IMAGE_UID)
            .subtitle(DEFAULT_SUBTITLE);
        return imageAlbum;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageAlbum createUpdatedEntity(EntityManager em) {
        ImageAlbum imageAlbum = new ImageAlbum()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .title(UPDATED_TITLE)
            .coverImageUid(UPDATED_COVER_IMAGE_UID)
            .subtitle(UPDATED_SUBTITLE);
        return imageAlbum;
    }

    @BeforeEach
    public void initTest() {
        imageAlbum = createEntity(em);
    }

    @Test
    @Transactional
    void createImageAlbum() throws Exception {
        int databaseSizeBeforeCreate = imageAlbumRepository.findAll().size();
        // Create the ImageAlbum
        restImageAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageAlbum)))
            .andExpect(status().isCreated());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeCreate + 1);
        ImageAlbum testImageAlbum = imageAlbumList.get(imageAlbumList.size() - 1);
        assertThat(testImageAlbum.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testImageAlbum.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testImageAlbum.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImageAlbum.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testImageAlbum.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testImageAlbum.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImageAlbum.getCoverImageUid()).isEqualTo(DEFAULT_COVER_IMAGE_UID);
        assertThat(testImageAlbum.getSubtitle()).isEqualTo(DEFAULT_SUBTITLE);
    }

    @Test
    @Transactional
    void createImageAlbumWithExistingId() throws Exception {
        // Create the ImageAlbum with an existing ID
        imageAlbum.setId(1L);

        int databaseSizeBeforeCreate = imageAlbumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageAlbum)))
            .andExpect(status().isBadRequest());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageAlbumRepository.findAll().size();
        // set the field null
        imageAlbum.setUid(null);

        // Create the ImageAlbum, which fails.

        restImageAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageAlbum)))
            .andExpect(status().isBadRequest());

        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImageAlbums() throws Exception {
        // Initialize the database
        imageAlbumRepository.saveAndFlush(imageAlbum);

        // Get all the imageAlbumList
        restImageAlbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageAlbum.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].coverImageUid").value(hasItem(DEFAULT_COVER_IMAGE_UID)))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE)));
    }

    @SuppressWarnings({"unchecked"})
    void getAllImageAlbumsWithEagerRelationshipsIsEnabled() throws Exception {
        when(imageAlbumServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImageAlbumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(imageAlbumServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    void getAllImageAlbumsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(imageAlbumServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restImageAlbumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(imageAlbumServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getImageAlbum() throws Exception {
        // Initialize the database
        imageAlbumRepository.saveAndFlush(imageAlbum);

        // Get the imageAlbum
        restImageAlbumMockMvc
            .perform(get(ENTITY_API_URL_ID, imageAlbum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageAlbum.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.coverImageUid").value(DEFAULT_COVER_IMAGE_UID))
            .andExpect(jsonPath("$.subtitle").value(DEFAULT_SUBTITLE));
    }

    @Test
    @Transactional
    void getNonExistingImageAlbum() throws Exception {
        // Get the imageAlbum
        restImageAlbumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImageAlbum() throws Exception {
        // Initialize the database
        imageAlbumRepository.saveAndFlush(imageAlbum);

        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();

        // Update the imageAlbum
        ImageAlbum updatedImageAlbum = imageAlbumRepository.findById(imageAlbum.getId()).get();
        // Disconnect from session so that the updates on updatedImageAlbum are not directly saved in db
        em.detach(updatedImageAlbum);
        updatedImageAlbum
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .title(UPDATED_TITLE)
            .coverImageUid(UPDATED_COVER_IMAGE_UID)
            .subtitle(UPDATED_SUBTITLE);

        restImageAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImageAlbum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImageAlbum))
            )
            .andExpect(status().isOk());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
        ImageAlbum testImageAlbum = imageAlbumList.get(imageAlbumList.size() - 1);
        assertThat(testImageAlbum.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testImageAlbum.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testImageAlbum.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageAlbum.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testImageAlbum.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testImageAlbum.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImageAlbum.getCoverImageUid()).isEqualTo(UPDATED_COVER_IMAGE_UID);
        assertThat(testImageAlbum.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
    }

    @Test
    @Transactional
    void putNonExistingImageAlbum() throws Exception {
        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();
        imageAlbum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageAlbum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageAlbum))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageAlbum() throws Exception {
        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();
        imageAlbum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageAlbum))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageAlbum() throws Exception {
        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();
        imageAlbum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAlbumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageAlbum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageAlbumWithPatch() throws Exception {
        // Initialize the database
        imageAlbumRepository.saveAndFlush(imageAlbum);

        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();

        // Update the imageAlbum using partial update
        ImageAlbum partialUpdatedImageAlbum = new ImageAlbum();
        partialUpdatedImageAlbum.setId(imageAlbum.getId());

        partialUpdatedImageAlbum
            .uid(UPDATED_UID)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .subtitle(UPDATED_SUBTITLE);

        restImageAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageAlbum))
            )
            .andExpect(status().isOk());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
        ImageAlbum testImageAlbum = imageAlbumList.get(imageAlbumList.size() - 1);
        assertThat(testImageAlbum.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testImageAlbum.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testImageAlbum.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageAlbum.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testImageAlbum.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testImageAlbum.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImageAlbum.getCoverImageUid()).isEqualTo(DEFAULT_COVER_IMAGE_UID);
        assertThat(testImageAlbum.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
    }

    @Test
    @Transactional
    void fullUpdateImageAlbumWithPatch() throws Exception {
        // Initialize the database
        imageAlbumRepository.saveAndFlush(imageAlbum);

        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();

        // Update the imageAlbum using partial update
        ImageAlbum partialUpdatedImageAlbum = new ImageAlbum();
        partialUpdatedImageAlbum.setId(imageAlbum.getId());

        partialUpdatedImageAlbum
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .title(UPDATED_TITLE)
            .coverImageUid(UPDATED_COVER_IMAGE_UID)
            .subtitle(UPDATED_SUBTITLE);

        restImageAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageAlbum))
            )
            .andExpect(status().isOk());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
        ImageAlbum testImageAlbum = imageAlbumList.get(imageAlbumList.size() - 1);
        assertThat(testImageAlbum.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testImageAlbum.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testImageAlbum.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageAlbum.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testImageAlbum.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testImageAlbum.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImageAlbum.getCoverImageUid()).isEqualTo(UPDATED_COVER_IMAGE_UID);
        assertThat(testImageAlbum.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
    }

    @Test
    @Transactional
    void patchNonExistingImageAlbum() throws Exception {
        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();
        imageAlbum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageAlbum))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageAlbum() throws Exception {
        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();
        imageAlbum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageAlbum))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageAlbum() throws Exception {
        int databaseSizeBeforeUpdate = imageAlbumRepository.findAll().size();
        imageAlbum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageAlbum))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageAlbum in the database
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImageAlbum() throws Exception {
        // Initialize the database
        imageAlbumRepository.saveAndFlush(imageAlbum);

        int databaseSizeBeforeDelete = imageAlbumRepository.findAll().size();

        // Delete the imageAlbum
        restImageAlbumMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageAlbum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImageAlbum> imageAlbumList = imageAlbumRepository.findAll();
        assertThat(imageAlbumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
