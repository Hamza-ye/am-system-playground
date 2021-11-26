package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LlinsFamilyReportHistoryService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory}.
 */
@RestController
@RequestMapping("/api")
public class LlinsFamilyReportHistoryResource {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyReportHistoryResource.class);

    private static final String ENTITY_NAME = "llinsFamilyReportHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LlinsFamilyReportHistoryService llinsFamilyReportHistoryService;

    private final LlinsFamilyReportHistoryRepository llinsFamilyReportHistoryRepository;

    public LlinsFamilyReportHistoryResource(
        LlinsFamilyReportHistoryService llinsFamilyReportHistoryService,
        LlinsFamilyReportHistoryRepository llinsFamilyReportHistoryRepository
    ) {
        this.llinsFamilyReportHistoryService = llinsFamilyReportHistoryService;
        this.llinsFamilyReportHistoryRepository = llinsFamilyReportHistoryRepository;
    }

    /**
     * {@code POST  /llins-family-report-histories} : Create a new llinsFamilyReportHistory.
     *
     * @param llinsFamilyReportHistory the llinsFamilyReportHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new llinsFamilyReportHistory, or with status {@code 400 (Bad Request)} if the llinsFamilyReportHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-family-report-histories")
    public ResponseEntity<LlinsFamilyReportHistory> createLlinsFamilyReportHistory(
        @Valid @RequestBody LlinsFamilyReportHistory llinsFamilyReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to save LlinsFamilyReportHistory : {}", llinsFamilyReportHistory);
        if (llinsFamilyReportHistory.getId() != null) {
            throw new BadRequestAlertException("A new llinsFamilyReportHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LlinsFamilyReportHistory result = llinsFamilyReportHistoryService.save(llinsFamilyReportHistory);
        return ResponseEntity
            .created(new URI("/api/llins-family-report-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-family-report-histories/:id} : Updates an existing llinsFamilyReportHistory.
     *
     * @param id the id of the llinsFamilyReportHistory to save.
     * @param llinsFamilyReportHistory the llinsFamilyReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsFamilyReportHistory,
     * or with status {@code 400 (Bad Request)} if the llinsFamilyReportHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the llinsFamilyReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-family-report-histories/{id}")
    public ResponseEntity<LlinsFamilyReportHistory> updateLlinsFamilyReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LlinsFamilyReportHistory llinsFamilyReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to update LlinsFamilyReportHistory : {}, {}", id, llinsFamilyReportHistory);
        if (llinsFamilyReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsFamilyReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsFamilyReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LlinsFamilyReportHistory result = llinsFamilyReportHistoryService.save(llinsFamilyReportHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsFamilyReportHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-family-report-histories/:id} : Partial updates given fields of an existing llinsFamilyReportHistory, field will ignore if it is null
     *
     * @param id the id of the llinsFamilyReportHistory to save.
     * @param llinsFamilyReportHistory the llinsFamilyReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsFamilyReportHistory,
     * or with status {@code 400 (Bad Request)} if the llinsFamilyReportHistory is not valid,
     * or with status {@code 404 (Not Found)} if the llinsFamilyReportHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the llinsFamilyReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-family-report-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LlinsFamilyReportHistory> partialUpdateLlinsFamilyReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LlinsFamilyReportHistory llinsFamilyReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update LlinsFamilyReportHistory partially : {}, {}", id, llinsFamilyReportHistory);
        if (llinsFamilyReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsFamilyReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsFamilyReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LlinsFamilyReportHistory> result = llinsFamilyReportHistoryService.partialUpdate(llinsFamilyReportHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsFamilyReportHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-family-report-histories} : get all the llinsFamilyReportHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of llinsFamilyReportHistories in body.
     */
    @GetMapping("/llins-family-report-histories")
    public List<LlinsFamilyReportHistory> getAllLlinsFamilyReportHistories() {
        log.debug("REST request to get all LlinsFamilyReportHistories");
        return llinsFamilyReportHistoryService.findAll();
    }

    /**
     * {@code GET  /llins-family-report-histories/:id} : get the "id" llinsFamilyReportHistory.
     *
     * @param id the id of the llinsFamilyReportHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the llinsFamilyReportHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-family-report-histories/{id}")
    public ResponseEntity<LlinsFamilyReportHistory> getLlinsFamilyReportHistory(@PathVariable Long id) {
        log.debug("REST request to get LlinsFamilyReportHistory : {}", id);
        Optional<LlinsFamilyReportHistory> llinsFamilyReportHistory = llinsFamilyReportHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(llinsFamilyReportHistory);
    }

    /**
     * {@code DELETE  /llins-family-report-histories/:id} : delete the "id" llinsFamilyReportHistory.
     *
     * @param id the id of the llinsFamilyReportHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-family-report-histories/{id}")
    public ResponseEntity<Void> deleteLlinsFamilyReportHistory(@PathVariable Long id) {
        log.debug("REST request to delete LlinsFamilyReportHistory : {}", id);
        llinsFamilyReportHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
