package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.repository.ContentPageRepository;
import org.nmcpye.activitiesmanagement.service.ContentPageService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.ContentPage}.
 */
@RestController
@RequestMapping("/api")
public class ContentPageResource {

    private final Logger log = LoggerFactory.getLogger(ContentPageResource.class);

    private static final String ENTITY_NAME = "contentPage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentPageService contentPageService;

    private final ContentPageRepository contentPageRepository;

    public ContentPageResource(ContentPageService contentPageService, ContentPageRepository contentPageRepository) {
        this.contentPageService = contentPageService;
        this.contentPageRepository = contentPageRepository;
    }

    /**
     * {@code POST  /content-pages} : Create a new contentPage.
     *
     * @param contentPage the contentPage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentPage, or with status {@code 400 (Bad Request)} if the contentPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-pages")
    public ResponseEntity<ContentPage> createContentPage(@Valid @RequestBody ContentPage contentPage) throws URISyntaxException {
        log.debug("REST request to save ContentPage : {}", contentPage);
        if (contentPage.getId() != null) {
            throw new BadRequestAlertException("A new contentPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContentPage result = contentPageService.save(contentPage);
        return ResponseEntity
            .created(new URI("/api/content-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /content-pages/:id} : Updates an existing contentPage.
     *
     * @param id the id of the contentPage to save.
     * @param contentPage the contentPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentPage,
     * or with status {@code 400 (Bad Request)} if the contentPage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-pages/{id}")
    public ResponseEntity<ContentPage> updateContentPage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentPage contentPage
    ) throws URISyntaxException {
        log.debug("REST request to update ContentPage : {}, {}", id, contentPage);
        if (contentPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContentPage result = contentPageService.save(contentPage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentPage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /content-pages/:id} : Partial updates given fields of an existing contentPage, field will ignore if it is null
     *
     * @param id the id of the contentPage to save.
     * @param contentPage the contentPage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentPage,
     * or with status {@code 400 (Bad Request)} if the contentPage is not valid,
     * or with status {@code 404 (Not Found)} if the contentPage is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentPage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/content-pages/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ContentPage> partialUpdateContentPage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContentPage contentPage
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContentPage partially : {}, {}", id, contentPage);
        if (contentPage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentPage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentPage> result = contentPageService.partialUpdate(contentPage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentPage.getId().toString())
        );
    }

    /**
     * {@code GET  /content-pages} : get all the contentPages.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentPages in body.
     */
    @GetMapping("/content-pages")
    public List<ContentPage> getAllContentPages(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ContentPages");
        return contentPageService.findAll();
    }

    /**
     * {@code GET  /content-pages/:id} : get the "id" contentPage.
     *
     * @param id the id of the contentPage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentPage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-pages/{id}")
    public ResponseEntity<ContentPage> getContentPage(@PathVariable Long id) {
        log.debug("REST request to get ContentPage : {}", id);
        Optional<ContentPage> contentPage = contentPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentPage);
    }

    /**
     * {@code DELETE  /content-pages/:id} : delete the "id" contentPage.
     *
     * @param id the id of the contentPage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-pages/{id}")
    public ResponseEntity<Void> deleteContentPage(@PathVariable Long id) {
        log.debug("REST request to delete ContentPage : {}", id);
        contentPageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
