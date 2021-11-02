package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReport;
import org.nmcpye.activitiesmanagement.repository.LLINSVillageReportRepository;
import org.nmcpye.activitiesmanagement.service.LLINSVillageReportService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LLINSVillageReport}.
 */
@RestController
@RequestMapping("/api")
public class LLINSVillageReportResource {

    private final Logger log = LoggerFactory.getLogger(LLINSVillageReportResource.class);

    private static final String ENTITY_NAME = "lLINSVillageReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LLINSVillageReportService lLINSVillageReportService;

    private final LLINSVillageReportRepository lLINSVillageReportRepository;

    public LLINSVillageReportResource(
        LLINSVillageReportService lLINSVillageReportService,
        LLINSVillageReportRepository lLINSVillageReportRepository
    ) {
        this.lLINSVillageReportService = lLINSVillageReportService;
        this.lLINSVillageReportRepository = lLINSVillageReportRepository;
    }

    /**
     * {@code POST  /llins-village-reports} : Create a new lLINSVillageReport.
     *
     * @param lLINSVillageReport the lLINSVillageReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lLINSVillageReport, or with status {@code 400 (Bad Request)} if the lLINSVillageReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-village-reports")
    public ResponseEntity<LLINSVillageReport> createLLINSVillageReport(@Valid @RequestBody LLINSVillageReport lLINSVillageReport)
        throws URISyntaxException {
        log.debug("REST request to save LLINSVillageReport : {}", lLINSVillageReport);
        if (lLINSVillageReport.getId() != null) {
            throw new BadRequestAlertException("A new lLINSVillageReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LLINSVillageReport result = lLINSVillageReportService.save(lLINSVillageReport);
        return ResponseEntity
            .created(new URI("/api/llins-village-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-village-reports/:id} : Updates an existing lLINSVillageReport.
     *
     * @param id the id of the lLINSVillageReport to save.
     * @param lLINSVillageReport the lLINSVillageReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSVillageReport,
     * or with status {@code 400 (Bad Request)} if the lLINSVillageReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lLINSVillageReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-village-reports/{id}")
    public ResponseEntity<LLINSVillageReport> updateLLINSVillageReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LLINSVillageReport lLINSVillageReport
    ) throws URISyntaxException {
        log.debug("REST request to update LLINSVillageReport : {}, {}", id, lLINSVillageReport);
        if (lLINSVillageReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSVillageReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSVillageReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LLINSVillageReport result = lLINSVillageReportService.save(lLINSVillageReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSVillageReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-village-reports/:id} : Partial updates given fields of an existing lLINSVillageReport, field will ignore if it is null
     *
     * @param id the id of the lLINSVillageReport to save.
     * @param lLINSVillageReport the lLINSVillageReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSVillageReport,
     * or with status {@code 400 (Bad Request)} if the lLINSVillageReport is not valid,
     * or with status {@code 404 (Not Found)} if the lLINSVillageReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the lLINSVillageReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-village-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LLINSVillageReport> partialUpdateLLINSVillageReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LLINSVillageReport lLINSVillageReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update LLINSVillageReport partially : {}, {}", id, lLINSVillageReport);
        if (lLINSVillageReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSVillageReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSVillageReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LLINSVillageReport> result = lLINSVillageReportService.partialUpdate(lLINSVillageReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSVillageReport.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-village-reports} : get all the lLINSVillageReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lLINSVillageReports in body.
     */
    @GetMapping("/llins-village-reports")
    public List<LLINSVillageReport> getAllLLINSVillageReports() {
        log.debug("REST request to get all LLINSVillageReports");
        return lLINSVillageReportService.findAll();
    }

    /**
     * {@code GET  /llins-village-reports/:id} : get the "id" lLINSVillageReport.
     *
     * @param id the id of the lLINSVillageReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lLINSVillageReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-village-reports/{id}")
    public ResponseEntity<LLINSVillageReport> getLLINSVillageReport(@PathVariable Long id) {
        log.debug("REST request to get LLINSVillageReport : {}", id);
        Optional<LLINSVillageReport> lLINSVillageReport = lLINSVillageReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lLINSVillageReport);
    }

    /**
     * {@code DELETE  /llins-village-reports/:id} : delete the "id" lLINSVillageReport.
     *
     * @param id the id of the lLINSVillageReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-village-reports/{id}")
    public ResponseEntity<Void> deleteLLINSVillageReport(@PathVariable Long id) {
        log.debug("REST request to delete LLINSVillageReport : {}", id);
        lLINSVillageReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
