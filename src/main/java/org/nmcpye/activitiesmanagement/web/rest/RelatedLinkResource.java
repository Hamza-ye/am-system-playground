package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;
import org.nmcpye.activitiesmanagement.repository.RelatedLinkRepository;
import org.nmcpye.activitiesmanagement.service.RelatedLinkService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.RelatedLink}.
 */
@RestController
@RequestMapping("/api")
public class RelatedLinkResource {

    private final Logger log = LoggerFactory.getLogger(RelatedLinkResource.class);

    private static final String ENTITY_NAME = "relatedLink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelatedLinkService relatedLinkService;

    private final RelatedLinkRepository relatedLinkRepository;

    public RelatedLinkResource(RelatedLinkService relatedLinkService, RelatedLinkRepository relatedLinkRepository) {
        this.relatedLinkService = relatedLinkService;
        this.relatedLinkRepository = relatedLinkRepository;
    }

    /**
     * {@code POST  /related-links} : Create a new relatedLink.
     *
     * @param relatedLink the relatedLink to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relatedLink, or with status {@code 400 (Bad Request)} if the relatedLink has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/related-links")
    public ResponseEntity<RelatedLink> createRelatedLink(@Valid @RequestBody RelatedLink relatedLink) throws URISyntaxException {
        log.debug("REST request to save RelatedLink : {}", relatedLink);
        if (relatedLink.getId() != null) {
            throw new BadRequestAlertException("A new relatedLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RelatedLink result = relatedLinkService.save(relatedLink);
        return ResponseEntity
            .created(new URI("/api/related-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /related-links/:id} : Updates an existing relatedLink.
     *
     * @param id the id of the relatedLink to save.
     * @param relatedLink the relatedLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedLink,
     * or with status {@code 400 (Bad Request)} if the relatedLink is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relatedLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/related-links/{id}")
    public ResponseEntity<RelatedLink> updateRelatedLink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RelatedLink relatedLink
    ) throws URISyntaxException {
        log.debug("REST request to update RelatedLink : {}, {}", id, relatedLink);
        if (relatedLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatedLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RelatedLink result = relatedLinkService.save(relatedLink);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatedLink.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /related-links/:id} : Partial updates given fields of an existing relatedLink, field will ignore if it is null
     *
     * @param id the id of the relatedLink to save.
     * @param relatedLink the relatedLink to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedLink,
     * or with status {@code 400 (Bad Request)} if the relatedLink is not valid,
     * or with status {@code 404 (Not Found)} if the relatedLink is not found,
     * or with status {@code 500 (Internal Server Error)} if the relatedLink couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/related-links/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RelatedLink> partialUpdateRelatedLink(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RelatedLink relatedLink
    ) throws URISyntaxException {
        log.debug("REST request to partial update RelatedLink partially : {}, {}", id, relatedLink);
        if (relatedLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedLink.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatedLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RelatedLink> result = relatedLinkService.partialUpdate(relatedLink);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatedLink.getId().toString())
        );
    }

    /**
     * {@code GET  /related-links} : get all the relatedLinks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relatedLinks in body.
     */
    @GetMapping("/related-links")
    public List<RelatedLink> getAllRelatedLinks() {
        log.debug("REST request to get all RelatedLinks");
        return relatedLinkService.findAll();
    }

    /**
     * {@code GET  /related-links/:id} : get the "id" relatedLink.
     *
     * @param id the id of the relatedLink to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relatedLink, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/related-links/{id}")
    public ResponseEntity<RelatedLink> getRelatedLink(@PathVariable Long id) {
        log.debug("REST request to get RelatedLink : {}", id);
        Optional<RelatedLink> relatedLink = relatedLinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relatedLink);
    }

    /**
     * {@code DELETE  /related-links/:id} : delete the "id" relatedLink.
     *
     * @param id the id of the relatedLink to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/related-links/{id}")
    public ResponseEntity<Void> deleteRelatedLink(@PathVariable Long id) {
        log.debug("REST request to delete RelatedLink : {}", id);
        relatedLinkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
