package org.nmcpye.activitiesmanagement.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.IntegrationTest;
import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.repository.ContentPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContentPageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContentPageResourceIT {

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

    private static final String DEFAULT_SUBTITLE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Integer DEFAULT_VISITED_COUNT = 1;
    private static final Integer UPDATED_VISITED_COUNT = 2;

    private static final String ENTITY_API_URL = "/api/content-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContentPageRepository contentPageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentPageMockMvc;

    private ContentPage contentPage;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentPage createEntity(EntityManager em) {
        ContentPage contentPage = new ContentPage()
            .uid(DEFAULT_UID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .title(DEFAULT_TITLE)
            .subtitle(DEFAULT_SUBTITLE)
            .content(DEFAULT_CONTENT)
            .active(DEFAULT_ACTIVE)
            .visitedCount(DEFAULT_VISITED_COUNT);
        return contentPage;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentPage createUpdatedEntity(EntityManager em) {
        ContentPage contentPage = new ContentPage()
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .content(UPDATED_CONTENT)
            .active(UPDATED_ACTIVE)
            .visitedCount(UPDATED_VISITED_COUNT);
        return contentPage;
    }

    @BeforeEach
    public void initTest() {
        contentPage = createEntity(em);
    }

    @Test
    @Transactional
    void createContentPage() throws Exception {
        int databaseSizeBeforeCreate = contentPageRepository.findAll().size();
        // Create the ContentPage
        restContentPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPage)))
            .andExpect(status().isCreated());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeCreate + 1);
        ContentPage testContentPage = contentPageList.get(contentPageList.size() - 1);
        assertThat(testContentPage.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testContentPage.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testContentPage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContentPage.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testContentPage.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testContentPage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContentPage.getSubtitle()).isEqualTo(DEFAULT_SUBTITLE);
        assertThat(testContentPage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testContentPage.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testContentPage.getVisitedCount()).isEqualTo(DEFAULT_VISITED_COUNT);
    }

    @Test
    @Transactional
    void createContentPageWithExistingId() throws Exception {
        // Create the ContentPage with an existing ID
        contentPage.setId(1L);

        int databaseSizeBeforeCreate = contentPageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPage)))
            .andExpect(status().isBadRequest());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentPageRepository.findAll().size();
        // set the field null
        contentPage.setUid(null);

        // Create the ContentPage, which fails.

        restContentPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPage)))
            .andExpect(status().isBadRequest());

        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContentPages() throws Exception {
        // Initialize the database
        contentPageRepository.saveAndFlush(contentPage);

        // Get all the contentPageList
        restContentPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].visitedCount").value(hasItem(DEFAULT_VISITED_COUNT)));
    }

    @Test
    @Transactional
    void getContentPage() throws Exception {
        // Initialize the database
        contentPageRepository.saveAndFlush(contentPage);

        // Get the contentPage
        restContentPageMockMvc
            .perform(get(ENTITY_API_URL_ID, contentPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentPage.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subtitle").value(DEFAULT_SUBTITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.visitedCount").value(DEFAULT_VISITED_COUNT));
    }

    @Test
    @Transactional
    void getNonExistingContentPage() throws Exception {
        // Get the contentPage
        restContentPageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContentPage() throws Exception {
        // Initialize the database
        contentPageRepository.saveAndFlush(contentPage);

        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();

        // Update the contentPage
        ContentPage updatedContentPage = contentPageRepository.findById(contentPage.getId()).get();
        // Disconnect from session so that the updates on updatedContentPage are not directly saved in db
        em.detach(updatedContentPage);
        updatedContentPage
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .content(UPDATED_CONTENT)
            .active(UPDATED_ACTIVE)
            .visitedCount(UPDATED_VISITED_COUNT);

        restContentPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContentPage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContentPage))
            )
            .andExpect(status().isOk());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
        ContentPage testContentPage = contentPageList.get(contentPageList.size() - 1);
        assertThat(testContentPage.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testContentPage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testContentPage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContentPage.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContentPage.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testContentPage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContentPage.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testContentPage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testContentPage.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testContentPage.getVisitedCount()).isEqualTo(UPDATED_VISITED_COUNT);
    }

    @Test
    @Transactional
    void putNonExistingContentPage() throws Exception {
        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();
        contentPage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contentPage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContentPage() throws Exception {
        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();
        contentPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contentPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContentPage() throws Exception {
        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();
        contentPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contentPage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContentPageWithPatch() throws Exception {
        // Initialize the database
        contentPageRepository.saveAndFlush(contentPage);

        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();

        // Update the contentPage using partial update
        ContentPage partialUpdatedContentPage = new ContentPage();
        partialUpdatedContentPage.setId(contentPage.getId());

        partialUpdatedContentPage
            .uid(UPDATED_UID)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .subtitle(UPDATED_SUBTITLE)
            .active(UPDATED_ACTIVE);

        restContentPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentPage))
            )
            .andExpect(status().isOk());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
        ContentPage testContentPage = contentPageList.get(contentPageList.size() - 1);
        assertThat(testContentPage.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testContentPage.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testContentPage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContentPage.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContentPage.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testContentPage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContentPage.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testContentPage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testContentPage.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testContentPage.getVisitedCount()).isEqualTo(DEFAULT_VISITED_COUNT);
    }

    @Test
    @Transactional
    void fullUpdateContentPageWithPatch() throws Exception {
        // Initialize the database
        contentPageRepository.saveAndFlush(contentPage);

        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();

        // Update the contentPage using partial update
        ContentPage partialUpdatedContentPage = new ContentPage();
        partialUpdatedContentPage.setId(contentPage.getId());

        partialUpdatedContentPage
            .uid(UPDATED_UID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .title(UPDATED_TITLE)
            .subtitle(UPDATED_SUBTITLE)
            .content(UPDATED_CONTENT)
            .active(UPDATED_ACTIVE)
            .visitedCount(UPDATED_VISITED_COUNT);

        restContentPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContentPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContentPage))
            )
            .andExpect(status().isOk());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
        ContentPage testContentPage = contentPageList.get(contentPageList.size() - 1);
        assertThat(testContentPage.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testContentPage.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testContentPage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContentPage.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testContentPage.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testContentPage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContentPage.getSubtitle()).isEqualTo(UPDATED_SUBTITLE);
        assertThat(testContentPage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testContentPage.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testContentPage.getVisitedCount()).isEqualTo(UPDATED_VISITED_COUNT);
    }

    @Test
    @Transactional
    void patchNonExistingContentPage() throws Exception {
        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();
        contentPage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contentPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContentPage() throws Exception {
        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();
        contentPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contentPage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContentPage() throws Exception {
        int databaseSizeBeforeUpdate = contentPageRepository.findAll().size();
        contentPage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContentPageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contentPage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContentPage in the database
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContentPage() throws Exception {
        // Initialize the database
        contentPageRepository.saveAndFlush(contentPage);

        int databaseSizeBeforeDelete = contentPageRepository.findAll().size();

        // Delete the contentPage
        restContentPageMockMvc
            .perform(delete(ENTITY_API_URL_ID, contentPage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContentPage> contentPageList = contentPageRepository.findAll();
        assertThat(contentPageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
