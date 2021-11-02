package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.FamilyHead;
import org.nmcpye.activitiesmanagement.repository.FamilyHeadRepository;
import org.nmcpye.activitiesmanagement.service.FamilyHeadService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.FamilyHead}.
 */
@RestController
@RequestMapping("/api")
public class FamilyHeadResource {

    private final Logger log = LoggerFactory.getLogger(FamilyHeadResource.class);

    private static final String ENTITY_NAME = "familyHead";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilyHeadService familyHeadService;

    private final FamilyHeadRepository familyHeadRepository;

    public FamilyHeadResource(FamilyHeadService familyHeadService, FamilyHeadRepository familyHeadRepository) {
        this.familyHeadService = familyHeadService;
        this.familyHeadRepository = familyHeadRepository;
    }

    /**
     * {@code POST  /family-heads} : Create a new familyHead.
     *
     * @param familyHead the familyHead to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familyHead, or with status {@code 400 (Bad Request)} if the familyHead has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/family-heads")
    public ResponseEntity<FamilyHead> createFamilyHead(@Valid @RequestBody FamilyHead familyHead) throws URISyntaxException {
        log.debug("REST request to save FamilyHead : {}", familyHead);
        if (familyHead.getId() != null) {
            throw new BadRequestAlertException("A new familyHead cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilyHead result = familyHeadService.save(familyHead);
        return ResponseEntity
            .created(new URI("/api/family-heads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /family-heads/:id} : Updates an existing familyHead.
     *
     * @param id the id of the familyHead to save.
     * @param familyHead the familyHead to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyHead,
     * or with status {@code 400 (Bad Request)} if the familyHead is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familyHead couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/family-heads/{id}")
    public ResponseEntity<FamilyHead> updateFamilyHead(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FamilyHead familyHead
    ) throws URISyntaxException {
        log.debug("REST request to update FamilyHead : {}, {}", id, familyHead);
        if (familyHead.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyHead.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyHeadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FamilyHead result = familyHeadService.save(familyHead);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyHead.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /family-heads/:id} : Partial updates given fields of an existing familyHead, field will ignore if it is null
     *
     * @param id the id of the familyHead to save.
     * @param familyHead the familyHead to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyHead,
     * or with status {@code 400 (Bad Request)} if the familyHead is not valid,
     * or with status {@code 404 (Not Found)} if the familyHead is not found,
     * or with status {@code 500 (Internal Server Error)} if the familyHead couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/family-heads/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FamilyHead> partialUpdateFamilyHead(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FamilyHead familyHead
    ) throws URISyntaxException {
        log.debug("REST request to partial update FamilyHead partially : {}, {}", id, familyHead);
        if (familyHead.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyHead.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyHeadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilyHead> result = familyHeadService.partialUpdate(familyHead);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyHead.getId().toString())
        );
    }

    /**
     * {@code GET  /family-heads} : get all the familyHeads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familyHeads in body.
     */
    @GetMapping("/family-heads")
    public ResponseEntity<List<FamilyHead>> getAllFamilyHeads(Pageable pageable) {
        log.debug("REST request to get a page of FamilyHeads");
        Page<FamilyHead> page = familyHeadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /family-heads/:id} : get the "id" familyHead.
     *
     * @param id the id of the familyHead to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familyHead, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/family-heads/{id}")
    public ResponseEntity<FamilyHead> getFamilyHead(@PathVariable Long id) {
        log.debug("REST request to get FamilyHead : {}", id);
        Optional<FamilyHead> familyHead = familyHeadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyHead);
    }

    /**
     * {@code DELETE  /family-heads/:id} : delete the "id" familyHead.
     *
     * @param id the id of the familyHead to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/family-heads/{id}")
    public ResponseEntity<Void> deleteFamilyHead(@PathVariable Long id) {
        log.debug("REST request to delete FamilyHead : {}", id);
        familyHeadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
