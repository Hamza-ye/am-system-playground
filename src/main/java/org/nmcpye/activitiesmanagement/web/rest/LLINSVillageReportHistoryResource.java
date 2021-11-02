package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReportHistory;
import org.nmcpye.activitiesmanagement.repository.LLINSVillageReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LLINSVillageReportHistoryService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LLINSVillageReportHistory}.
 */
@RestController
@RequestMapping("/api")
public class LLINSVillageReportHistoryResource {

    private final Logger log = LoggerFactory.getLogger(LLINSVillageReportHistoryResource.class);

    private static final String ENTITY_NAME = "lLINSVillageReportHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LLINSVillageReportHistoryService lLINSVillageReportHistoryService;

    private final LLINSVillageReportHistoryRepository lLINSVillageReportHistoryRepository;

    public LLINSVillageReportHistoryResource(
        LLINSVillageReportHistoryService lLINSVillageReportHistoryService,
        LLINSVillageReportHistoryRepository lLINSVillageReportHistoryRepository
    ) {
        this.lLINSVillageReportHistoryService = lLINSVillageReportHistoryService;
        this.lLINSVillageReportHistoryRepository = lLINSVillageReportHistoryRepository;
    }

    /**
     * {@code POST  /llins-village-report-histories} : Create a new lLINSVillageReportHistory.
     *
     * @param lLINSVillageReportHistory the lLINSVillageReportHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lLINSVillageReportHistory, or with status {@code 400 (Bad Request)} if the lLINSVillageReportHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-village-report-histories")
    public ResponseEntity<LLINSVillageReportHistory> createLLINSVillageReportHistory(
        @Valid @RequestBody LLINSVillageReportHistory lLINSVillageReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to save LLINSVillageReportHistory : {}", lLINSVillageReportHistory);
        if (lLINSVillageReportHistory.getId() != null) {
            throw new BadRequestAlertException("A new lLINSVillageReportHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LLINSVillageReportHistory result = lLINSVillageReportHistoryService.save(lLINSVillageReportHistory);
        return ResponseEntity
            .created(new URI("/api/llins-village-report-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-village-report-histories/:id} : Updates an existing lLINSVillageReportHistory.
     *
     * @param id the id of the lLINSVillageReportHistory to save.
     * @param lLINSVillageReportHistory the lLINSVillageReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSVillageReportHistory,
     * or with status {@code 400 (Bad Request)} if the lLINSVillageReportHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lLINSVillageReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-village-report-histories/{id}")
    public ResponseEntity<LLINSVillageReportHistory> updateLLINSVillageReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LLINSVillageReportHistory lLINSVillageReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to update LLINSVillageReportHistory : {}, {}", id, lLINSVillageReportHistory);
        if (lLINSVillageReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSVillageReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSVillageReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LLINSVillageReportHistory result = lLINSVillageReportHistoryService.save(lLINSVillageReportHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSVillageReportHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-village-report-histories/:id} : Partial updates given fields of an existing lLINSVillageReportHistory, field will ignore if it is null
     *
     * @param id the id of the lLINSVillageReportHistory to save.
     * @param lLINSVillageReportHistory the lLINSVillageReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSVillageReportHistory,
     * or with status {@code 400 (Bad Request)} if the lLINSVillageReportHistory is not valid,
     * or with status {@code 404 (Not Found)} if the lLINSVillageReportHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the lLINSVillageReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-village-report-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LLINSVillageReportHistory> partialUpdateLLINSVillageReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LLINSVillageReportHistory lLINSVillageReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update LLINSVillageReportHistory partially : {}, {}", id, lLINSVillageReportHistory);
        if (lLINSVillageReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSVillageReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSVillageReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LLINSVillageReportHistory> result = lLINSVillageReportHistoryService.partialUpdate(lLINSVillageReportHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSVillageReportHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-village-report-histories} : get all the lLINSVillageReportHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lLINSVillageReportHistories in body.
     */
    @GetMapping("/llins-village-report-histories")
    public List<LLINSVillageReportHistory> getAllLLINSVillageReportHistories() {
        log.debug("REST request to get all LLINSVillageReportHistories");
        return lLINSVillageReportHistoryService.findAll();
    }

    /**
     * {@code GET  /llins-village-report-histories/:id} : get the "id" lLINSVillageReportHistory.
     *
     * @param id the id of the lLINSVillageReportHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lLINSVillageReportHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-village-report-histories/{id}")
    public ResponseEntity<LLINSVillageReportHistory> getLLINSVillageReportHistory(@PathVariable Long id) {
        log.debug("REST request to get LLINSVillageReportHistory : {}", id);
        Optional<LLINSVillageReportHistory> lLINSVillageReportHistory = lLINSVillageReportHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lLINSVillageReportHistory);
    }

    /**
     * {@code DELETE  /llins-village-report-histories/:id} : delete the "id" lLINSVillageReportHistory.
     *
     * @param id the id of the lLINSVillageReportHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-village-report-histories/{id}")
    public ResponseEntity<Void> deleteLLINSVillageReportHistory(@PathVariable Long id) {
        log.debug("REST request to delete LLINSVillageReportHistory : {}", id);
        lLINSVillageReportHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
