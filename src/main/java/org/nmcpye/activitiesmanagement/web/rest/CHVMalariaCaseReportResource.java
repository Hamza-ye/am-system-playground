package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaCaseReport;
import org.nmcpye.activitiesmanagement.repository.CHVMalariaCaseReportRepository;
import org.nmcpye.activitiesmanagement.service.CHVMalariaCaseReportService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.CHVMalariaCaseReport}.
 */
@RestController
@RequestMapping("/api")
public class CHVMalariaCaseReportResource {

    private final Logger log = LoggerFactory.getLogger(CHVMalariaCaseReportResource.class);

    private static final String ENTITY_NAME = "cHVMalariaCaseReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CHVMalariaCaseReportService cHVMalariaCaseReportService;

    private final CHVMalariaCaseReportRepository cHVMalariaCaseReportRepository;

    public CHVMalariaCaseReportResource(
        CHVMalariaCaseReportService cHVMalariaCaseReportService,
        CHVMalariaCaseReportRepository cHVMalariaCaseReportRepository
    ) {
        this.cHVMalariaCaseReportService = cHVMalariaCaseReportService;
        this.cHVMalariaCaseReportRepository = cHVMalariaCaseReportRepository;
    }

    /**
     * {@code POST  /chv-malaria-case-reports} : Create a new cHVMalariaCaseReport.
     *
     * @param cHVMalariaCaseReport the cHVMalariaCaseReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cHVMalariaCaseReport, or with status {@code 400 (Bad Request)} if the cHVMalariaCaseReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chv-malaria-case-reports")
    public ResponseEntity<CHVMalariaCaseReport> createCHVMalariaCaseReport(@Valid @RequestBody CHVMalariaCaseReport cHVMalariaCaseReport)
        throws URISyntaxException {
        log.debug("REST request to save CHVMalariaCaseReport : {}", cHVMalariaCaseReport);
        if (cHVMalariaCaseReport.getId() != null) {
            throw new BadRequestAlertException("A new cHVMalariaCaseReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CHVMalariaCaseReport result = cHVMalariaCaseReportService.save(cHVMalariaCaseReport);
        return ResponseEntity
            .created(new URI("/api/chv-malaria-case-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chv-malaria-case-reports/:id} : Updates an existing cHVMalariaCaseReport.
     *
     * @param id the id of the cHVMalariaCaseReport to save.
     * @param cHVMalariaCaseReport the cHVMalariaCaseReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHVMalariaCaseReport,
     * or with status {@code 400 (Bad Request)} if the cHVMalariaCaseReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cHVMalariaCaseReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chv-malaria-case-reports/{id}")
    public ResponseEntity<CHVMalariaCaseReport> updateCHVMalariaCaseReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CHVMalariaCaseReport cHVMalariaCaseReport
    ) throws URISyntaxException {
        log.debug("REST request to update CHVMalariaCaseReport : {}, {}", id, cHVMalariaCaseReport);
        if (cHVMalariaCaseReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHVMalariaCaseReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVMalariaCaseReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CHVMalariaCaseReport result = cHVMalariaCaseReportService.save(cHVMalariaCaseReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHVMalariaCaseReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chv-malaria-case-reports/:id} : Partial updates given fields of an existing cHVMalariaCaseReport, field will ignore if it is null
     *
     * @param id the id of the cHVMalariaCaseReport to save.
     * @param cHVMalariaCaseReport the cHVMalariaCaseReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHVMalariaCaseReport,
     * or with status {@code 400 (Bad Request)} if the cHVMalariaCaseReport is not valid,
     * or with status {@code 404 (Not Found)} if the cHVMalariaCaseReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the cHVMalariaCaseReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chv-malaria-case-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CHVMalariaCaseReport> partialUpdateCHVMalariaCaseReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CHVMalariaCaseReport cHVMalariaCaseReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update CHVMalariaCaseReport partially : {}, {}", id, cHVMalariaCaseReport);
        if (cHVMalariaCaseReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHVMalariaCaseReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVMalariaCaseReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CHVMalariaCaseReport> result = cHVMalariaCaseReportService.partialUpdate(cHVMalariaCaseReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHVMalariaCaseReport.getId().toString())
        );
    }

    /**
     * {@code GET  /chv-malaria-case-reports} : get all the cHVMalariaCaseReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cHVMalariaCaseReports in body.
     */
    @GetMapping("/chv-malaria-case-reports")
    public List<CHVMalariaCaseReport> getAllCHVMalariaCaseReports() {
        log.debug("REST request to get all CHVMalariaCaseReports");
        return cHVMalariaCaseReportService.findAll();
    }

    /**
     * {@code GET  /chv-malaria-case-reports/:id} : get the "id" cHVMalariaCaseReport.
     *
     * @param id the id of the cHVMalariaCaseReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cHVMalariaCaseReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chv-malaria-case-reports/{id}")
    public ResponseEntity<CHVMalariaCaseReport> getCHVMalariaCaseReport(@PathVariable Long id) {
        log.debug("REST request to get CHVMalariaCaseReport : {}", id);
        Optional<CHVMalariaCaseReport> cHVMalariaCaseReport = cHVMalariaCaseReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cHVMalariaCaseReport);
    }

    /**
     * {@code DELETE  /chv-malaria-case-reports/:id} : delete the "id" cHVMalariaCaseReport.
     *
     * @param id the id of the cHVMalariaCaseReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chv-malaria-case-reports/{id}")
    public ResponseEntity<Void> deleteCHVMalariaCaseReport(@PathVariable Long id) {
        log.debug("REST request to delete CHVMalariaCaseReport : {}", id);
        cHVMalariaCaseReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
