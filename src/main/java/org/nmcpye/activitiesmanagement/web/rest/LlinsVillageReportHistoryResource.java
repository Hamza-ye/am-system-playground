package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageReportHistoryService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory}.
 */
@RestController
@RequestMapping("/api")
public class LlinsVillageReportHistoryResource {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageReportHistoryResource.class);

    private static final String ENTITY_NAME = "llinsVillageReportHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LlinsVillageReportHistoryService llinsVillageReportHistoryService;

    private final LlinsVillageReportHistoryRepository llinsVillageReportHistoryRepository;

    public LlinsVillageReportHistoryResource(
        LlinsVillageReportHistoryService llinsVillageReportHistoryService,
        LlinsVillageReportHistoryRepository llinsVillageReportHistoryRepository
    ) {
        this.llinsVillageReportHistoryService = llinsVillageReportHistoryService;
        this.llinsVillageReportHistoryRepository = llinsVillageReportHistoryRepository;
    }

    /**
     * {@code POST  /llins-village-report-histories} : Create a new llinsVillageReportHistory.
     *
     * @param llinsVillageReportHistory the llinsVillageReportHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new llinsVillageReportHistory, or with status {@code 400 (Bad Request)} if the llinsVillageReportHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-village-report-histories")
    public ResponseEntity<LlinsVillageReportHistory> createLlinsVillageReportHistory(
        @Valid @RequestBody LlinsVillageReportHistory llinsVillageReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to save LlinsVillageReportHistory : {}", llinsVillageReportHistory);
        if (llinsVillageReportHistory.getId() != null) {
            throw new BadRequestAlertException("A new llinsVillageReportHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LlinsVillageReportHistory result = llinsVillageReportHistoryService.save(llinsVillageReportHistory);
        return ResponseEntity
            .created(new URI("/api/llins-village-report-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-village-report-histories/:id} : Updates an existing llinsVillageReportHistory.
     *
     * @param id the id of the llinsVillageReportHistory to save.
     * @param llinsVillageReportHistory the llinsVillageReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsVillageReportHistory,
     * or with status {@code 400 (Bad Request)} if the llinsVillageReportHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the llinsVillageReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-village-report-histories/{id}")
    public ResponseEntity<LlinsVillageReportHistory> updateLlinsVillageReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LlinsVillageReportHistory llinsVillageReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to update LlinsVillageReportHistory : {}, {}", id, llinsVillageReportHistory);
        if (llinsVillageReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsVillageReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsVillageReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LlinsVillageReportHistory result = llinsVillageReportHistoryService.save(llinsVillageReportHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsVillageReportHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-village-report-histories/:id} : Partial updates given fields of an existing llinsVillageReportHistory, field will ignore if it is null
     *
     * @param id the id of the llinsVillageReportHistory to save.
     * @param llinsVillageReportHistory the llinsVillageReportHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsVillageReportHistory,
     * or with status {@code 400 (Bad Request)} if the llinsVillageReportHistory is not valid,
     * or with status {@code 404 (Not Found)} if the llinsVillageReportHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the llinsVillageReportHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-village-report-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LlinsVillageReportHistory> partialUpdateLlinsVillageReportHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LlinsVillageReportHistory llinsVillageReportHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update LlinsVillageReportHistory partially : {}, {}", id, llinsVillageReportHistory);
        if (llinsVillageReportHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsVillageReportHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsVillageReportHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LlinsVillageReportHistory> result = llinsVillageReportHistoryService.partialUpdate(llinsVillageReportHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsVillageReportHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-village-report-histories} : get all the llinsVillageReportHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of llinsVillageReportHistories in body.
     */
    @GetMapping("/llins-village-report-histories")
    public List<LlinsVillageReportHistory> getAllLlinsVillageReportHistories() {
        log.debug("REST request to get all LlinsVillageReportHistories");
        return llinsVillageReportHistoryService.findAll();
    }

    /**
     * {@code GET  /llins-village-report-histories/:id} : get the "id" llinsVillageReportHistory.
     *
     * @param id the id of the llinsVillageReportHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the llinsVillageReportHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-village-report-histories/{id}")
    public ResponseEntity<LlinsVillageReportHistory> getLlinsVillageReportHistory(@PathVariable Long id) {
        log.debug("REST request to get LlinsVillageReportHistory : {}", id);
        Optional<LlinsVillageReportHistory> llinsVillageReportHistory = llinsVillageReportHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(llinsVillageReportHistory);
    }

    /**
     * {@code DELETE  /llins-village-report-histories/:id} : delete the "id" llinsVillageReportHistory.
     *
     * @param id the id of the llinsVillageReportHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-village-report-histories/{id}")
    public ResponseEntity<Void> deleteLlinsVillageReportHistory(@PathVariable Long id) {
        log.debug("REST request to delete LlinsVillageReportHistory : {}", id);
        llinsVillageReportHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
