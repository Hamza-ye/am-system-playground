package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyReportRepository;
import org.nmcpye.activitiesmanagement.service.LLINSFamilyReportService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport}.
 */
@RestController
@RequestMapping("/api")
public class LLINSFamilyReportResource {

    private final Logger log = LoggerFactory.getLogger(LLINSFamilyReportResource.class);

    private static final String ENTITY_NAME = "lLINSFamilyReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LLINSFamilyReportService lLINSFamilyReportService;

    private final LLINSFamilyReportRepository lLINSFamilyReportRepository;

    public LLINSFamilyReportResource(
        LLINSFamilyReportService lLINSFamilyReportService,
        LLINSFamilyReportRepository lLINSFamilyReportRepository
    ) {
        this.lLINSFamilyReportService = lLINSFamilyReportService;
        this.lLINSFamilyReportRepository = lLINSFamilyReportRepository;
    }

    /**
     * {@code POST  /llins-family-reports} : Create a new lLINSFamilyReport.
     *
     * @param lLINSFamilyReport the lLINSFamilyReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lLINSFamilyReport, or with status {@code 400 (Bad Request)} if the lLINSFamilyReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-family-reports")
    public ResponseEntity<LLINSFamilyReport> createLLINSFamilyReport(@Valid @RequestBody LLINSFamilyReport lLINSFamilyReport)
        throws URISyntaxException {
        log.debug("REST request to save LLINSFamilyReport : {}", lLINSFamilyReport);
        if (lLINSFamilyReport.getId() != null) {
            throw new BadRequestAlertException("A new lLINSFamilyReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LLINSFamilyReport result = lLINSFamilyReportService.save(lLINSFamilyReport);
        return ResponseEntity
            .created(new URI("/api/llins-family-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-family-reports/:id} : Updates an existing lLINSFamilyReport.
     *
     * @param id the id of the lLINSFamilyReport to save.
     * @param lLINSFamilyReport the lLINSFamilyReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSFamilyReport,
     * or with status {@code 400 (Bad Request)} if the lLINSFamilyReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lLINSFamilyReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-family-reports/{id}")
    public ResponseEntity<LLINSFamilyReport> updateLLINSFamilyReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LLINSFamilyReport lLINSFamilyReport
    ) throws URISyntaxException {
        log.debug("REST request to update LLINSFamilyReport : {}, {}", id, lLINSFamilyReport);
        if (lLINSFamilyReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSFamilyReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSFamilyReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LLINSFamilyReport result = lLINSFamilyReportService.save(lLINSFamilyReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSFamilyReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-family-reports/:id} : Partial updates given fields of an existing lLINSFamilyReport, field will ignore if it is null
     *
     * @param id the id of the lLINSFamilyReport to save.
     * @param lLINSFamilyReport the lLINSFamilyReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSFamilyReport,
     * or with status {@code 400 (Bad Request)} if the lLINSFamilyReport is not valid,
     * or with status {@code 404 (Not Found)} if the lLINSFamilyReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the lLINSFamilyReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-family-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LLINSFamilyReport> partialUpdateLLINSFamilyReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LLINSFamilyReport lLINSFamilyReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update LLINSFamilyReport partially : {}, {}", id, lLINSFamilyReport);
        if (lLINSFamilyReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSFamilyReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSFamilyReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LLINSFamilyReport> result = lLINSFamilyReportService.partialUpdate(lLINSFamilyReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSFamilyReport.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-family-reports} : get all the lLINSFamilyReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lLINSFamilyReports in body.
     */
    @GetMapping("/llins-family-reports")
    public ResponseEntity<List<LLINSFamilyReport>> getAllLLINSFamilyReports(Pageable pageable) {
        log.debug("REST request to get a page of LLINSFamilyReports");
        Page<LLINSFamilyReport> page = lLINSFamilyReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /llins-family-reports/:id} : get the "id" lLINSFamilyReport.
     *
     * @param id the id of the lLINSFamilyReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lLINSFamilyReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-family-reports/{id}")
    public ResponseEntity<LLINSFamilyReport> getLLINSFamilyReport(@PathVariable Long id) {
        log.debug("REST request to get LLINSFamilyReport : {}", id);
        Optional<LLINSFamilyReport> lLINSFamilyReport = lLINSFamilyReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lLINSFamilyReport);
    }

    /**
     * {@code DELETE  /llins-family-reports/:id} : delete the "id" lLINSFamilyReport.
     *
     * @param id the id of the lLINSFamilyReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-family-reports/{id}")
    public ResponseEntity<Void> deleteLLINSFamilyReport(@PathVariable Long id) {
        log.debug("REST request to delete LLINSFamilyReport : {}", id);
        lLINSFamilyReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
