package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReportHistory;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LLINSFamilyReportHistoryService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LLINSFamilyReportHistory}.
 */
@RestController
@RequestMapping("/api")
public class LLINSFamilyReportHistoryResource {

    private final Logger log = LoggerFactory.getLogger(LLINSFamilyReportHistoryResource.class);

    private static final String ENTITY_NAME = "lLINSFamilyReportHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LLINSFamilyReportHistoryService lLINSFamilyReportHistoryService;

    private final LLINSFamilyReportHistoryRepository lLINSFamilyReportHistoryRepository;

    public LLINSFamilyReportHistoryResource(
        LLINSFamilyReportHistoryService lLINSFamilyReportHistoryService,
        LLINSFamilyReportHistoryRepository lLINSFamilyReportHistoryRepository
    ) {
        this.lLINSFamilyReportHistoryService = lLINSFamilyReportHistoryService;
        this.lLINSFamilyReportHistoryRepository = lLINSFamilyReportHistoryRepository;
    }

    /**
     * {@code POST  /llins-family-report-histories} : Create a new lLINSFamilyReportHistory.
     *
     * @param lLINSFamilyReportHistory the lLINSFamilyReportHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lLINSFamilyReportHistory, or with status {@code 400 (Bad Request)} if the lLINSFamilyReportHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-family-report-histories")
    public ResponseEntity<LLINSFamilyReportHistory> createLLINSFamilyReportHistory(
        @Valid @RequestBody LLINSFamilyReportHistory lLINSFamilyReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to save LLINSFamilyReportHistory : {}", lLINSFamilyReportHistory);
        if (lLINSFamilyReportHistory.getId() != null) {
            throw new BadRequestAlertException("A new lLINSFamilyReportHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LLINSFamilyReportHistory result = lLINSFamilyReportHistoryService.save(lLINSFamilyReportHistory);
        return ResponseEntity
            .created(new URI("/api/llins-family-report-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-family-report-histories/:id} : Updates an existing lLINSFamilyReportHistory.
     *
     * @param id the id of the lLINSFamilyReportHistory to save.
     * @param lLINSFamilyReportHistory the lLINSFamilyReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSFamilyReportHistory,
     * or with status {@code 400 (Bad Request)} if the lLINSFamilyReportHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lLINSFamilyReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-family-report-histories/{id}")
    public ResponseEntity<LLINSFamilyReportHistory> updateLLINSFamilyReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LLINSFamilyReportHistory lLINSFamilyReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to update LLINSFamilyReportHistory : {}, {}", id, lLINSFamilyReportHistory);
        if (lLINSFamilyReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSFamilyReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSFamilyReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LLINSFamilyReportHistory result = lLINSFamilyReportHistoryService.save(lLINSFamilyReportHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSFamilyReportHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-family-report-histories/:id} : Partial updates given fields of an existing lLINSFamilyReportHistory, field will ignore if it is null
     *
     * @param id the id of the lLINSFamilyReportHistory to save.
     * @param lLINSFamilyReportHistory the lLINSFamilyReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSFamilyReportHistory,
     * or with status {@code 400 (Bad Request)} if the lLINSFamilyReportHistory is not valid,
     * or with status {@code 404 (Not Found)} if the lLINSFamilyReportHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the lLINSFamilyReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-family-report-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LLINSFamilyReportHistory> partialUpdateLLINSFamilyReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LLINSFamilyReportHistory lLINSFamilyReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update LLINSFamilyReportHistory partially : {}, {}", id, lLINSFamilyReportHistory);
        if (lLINSFamilyReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSFamilyReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSFamilyReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LLINSFamilyReportHistory> result = lLINSFamilyReportHistoryService.partialUpdate(lLINSFamilyReportHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSFamilyReportHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-family-report-histories} : get all the lLINSFamilyReportHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lLINSFamilyReportHistories in body.
     */
    @GetMapping("/llins-family-report-histories")
    public List<LLINSFamilyReportHistory> getAllLLINSFamilyReportHistories() {
        log.debug("REST request to get all LLINSFamilyReportHistories");
        return lLINSFamilyReportHistoryService.findAll();
    }

    /**
     * {@code GET  /llins-family-report-histories/:id} : get the "id" lLINSFamilyReportHistory.
     *
     * @param id the id of the lLINSFamilyReportHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lLINSFamilyReportHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-family-report-histories/{id}")
    public ResponseEntity<LLINSFamilyReportHistory> getLLINSFamilyReportHistory(@PathVariable Long id) {
        log.debug("REST request to get LLINSFamilyReportHistory : {}", id);
        Optional<LLINSFamilyReportHistory> lLINSFamilyReportHistory = lLINSFamilyReportHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lLINSFamilyReportHistory);
    }

    /**
     * {@code DELETE  /llins-family-report-histories/:id} : delete the "id" lLINSFamilyReportHistory.
     *
     * @param id the id of the lLINSFamilyReportHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-family-report-histories/{id}")
    public ResponseEntity<Void> deleteLLINSFamilyReportHistory(@PathVariable Long id) {
        log.debug("REST request to delete LLINSFamilyReportHistory : {}", id);
        lLINSFamilyReportHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
