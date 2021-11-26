package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyReportRepository;
import org.nmcpye.activitiesmanagement.service.LlinsFamilyReportService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport}.
 */
@RestController
@RequestMapping("/api")
public class LlinsFamilyReportResource {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyReportResource.class);

    private static final String ENTITY_NAME = "llinsFamilyReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LlinsFamilyReportService llinsFamilyReportService;

    private final LlinsFamilyReportRepository llinsFamilyReportRepository;

    public LlinsFamilyReportResource(
        LlinsFamilyReportService llinsFamilyReportService,
        LlinsFamilyReportRepository llinsFamilyReportRepository
    ) {
        this.llinsFamilyReportService = llinsFamilyReportService;
        this.llinsFamilyReportRepository = llinsFamilyReportRepository;
    }

    /**
     * {@code POST  /llins-family-reports} : Create a new llinsFamilyReport.
     *
     * @param llinsFamilyReport the llinsFamilyReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new llinsFamilyReport, or with status {@code 400 (Bad Request)} if the llinsFamilyReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-family-reports")
    public ResponseEntity<LlinsFamilyReport> createLlinsFamilyReport(@Valid @RequestBody LlinsFamilyReport llinsFamilyReport)
        throws URISyntaxException {
        log.debug("REST request to save LlinsFamilyReport : {}", llinsFamilyReport);
        if (llinsFamilyReport.getId() != null) {
            throw new BadRequestAlertException("A new llinsFamilyReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LlinsFamilyReport result = llinsFamilyReportService.save(llinsFamilyReport);
        return ResponseEntity
            .created(new URI("/api/llins-family-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-family-reports/:id} : Updates an existing llinsFamilyReport.
     *
     * @param id the id of the llinsFamilyReport to save.
     * @param llinsFamilyReport the llinsFamilyReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsFamilyReport,
     * or with status {@code 400 (Bad Request)} if the llinsFamilyReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the llinsFamilyReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-family-reports/{id}")
    public ResponseEntity<LlinsFamilyReport> updateLlinsFamilyReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LlinsFamilyReport llinsFamilyReport
    ) throws URISyntaxException {
        log.debug("REST request to update LlinsFamilyReport : {}, {}", id, llinsFamilyReport);
        if (llinsFamilyReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsFamilyReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsFamilyReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LlinsFamilyReport result = llinsFamilyReportService.save(llinsFamilyReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsFamilyReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-family-reports/:id} : Partial updates given fields of an existing llinsFamilyReport, field will ignore if it is null
     *
     * @param id the id of the llinsFamilyReport to save.
     * @param llinsFamilyReport the llinsFamilyReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsFamilyReport,
     * or with status {@code 400 (Bad Request)} if the llinsFamilyReport is not valid,
     * or with status {@code 404 (Not Found)} if the llinsFamilyReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the llinsFamilyReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-family-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LlinsFamilyReport> partialUpdateLlinsFamilyReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LlinsFamilyReport llinsFamilyReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update LlinsFamilyReport partially : {}, {}", id, llinsFamilyReport);
        if (llinsFamilyReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsFamilyReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsFamilyReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LlinsFamilyReport> result = llinsFamilyReportService.partialUpdate(llinsFamilyReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsFamilyReport.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-family-reports} : get all the llinsFamilyReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of llinsFamilyReports in body.
     */
    @GetMapping("/llins-family-reports")
    public ResponseEntity<List<LlinsFamilyReport>> getAllLlinsFamilyReports(Pageable pageable) {
        log.debug("REST request to get a page of LlinsFamilyReports");
        Page<LlinsFamilyReport> page = llinsFamilyReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /llins-family-reports/:id} : get the "id" llinsFamilyReport.
     *
     * @param id the id of the llinsFamilyReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the llinsFamilyReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-family-reports/{id}")
    public ResponseEntity<LlinsFamilyReport> getLlinsFamilyReport(@PathVariable Long id) {
        log.debug("REST request to get LlinsFamilyReport : {}", id);
        Optional<LlinsFamilyReport> llinsFamilyReport = llinsFamilyReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(llinsFamilyReport);
    }

    /**
     * {@code DELETE  /llins-family-reports/:id} : delete the "id" llinsFamilyReport.
     *
     * @param id the id of the llinsFamilyReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-family-reports/{id}")
    public ResponseEntity<Void> deleteLlinsFamilyReport(@PathVariable Long id) {
        log.debug("REST request to delete LlinsFamilyReport : {}", id);
        llinsFamilyReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
