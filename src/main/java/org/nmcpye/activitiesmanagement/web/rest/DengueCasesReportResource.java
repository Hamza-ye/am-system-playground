package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.DengueCasesReport;
import org.nmcpye.activitiesmanagement.repository.DengueCasesReportRepository;
import org.nmcpye.activitiesmanagement.service.DengueCasesReportService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.DengueCasesReport}.
 */
@RestController
@RequestMapping("/api")
public class DengueCasesReportResource {

    private final Logger log = LoggerFactory.getLogger(DengueCasesReportResource.class);

    private static final String ENTITY_NAME = "dengueCasesReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DengueCasesReportService dengueCasesReportService;

    private final DengueCasesReportRepository dengueCasesReportRepository;

    public DengueCasesReportResource(
        DengueCasesReportService dengueCasesReportService,
        DengueCasesReportRepository dengueCasesReportRepository
    ) {
        this.dengueCasesReportService = dengueCasesReportService;
        this.dengueCasesReportRepository = dengueCasesReportRepository;
    }

    /**
     * {@code POST  /dengue-cases-reports} : Create a new dengueCasesReport.
     *
     * @param dengueCasesReport the dengueCasesReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dengueCasesReport, or with status {@code 400 (Bad Request)} if the dengueCasesReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dengue-cases-reports")
    public ResponseEntity<DengueCasesReport> createDengueCasesReport(@Valid @RequestBody DengueCasesReport dengueCasesReport)
        throws URISyntaxException {
        log.debug("REST request to save DengueCasesReport : {}", dengueCasesReport);
        if (dengueCasesReport.getId() != null) {
            throw new BadRequestAlertException("A new dengueCasesReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DengueCasesReport result = dengueCasesReportService.save(dengueCasesReport);
        return ResponseEntity
            .created(new URI("/api/dengue-cases-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dengue-cases-reports/:id} : Updates an existing dengueCasesReport.
     *
     * @param id the id of the dengueCasesReport to save.
     * @param dengueCasesReport the dengueCasesReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dengueCasesReport,
     * or with status {@code 400 (Bad Request)} if the dengueCasesReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dengueCasesReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dengue-cases-reports/{id}")
    public ResponseEntity<DengueCasesReport> updateDengueCasesReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DengueCasesReport dengueCasesReport
    ) throws URISyntaxException {
        log.debug("REST request to update DengueCasesReport : {}, {}", id, dengueCasesReport);
        if (dengueCasesReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dengueCasesReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dengueCasesReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DengueCasesReport result = dengueCasesReportService.save(dengueCasesReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dengueCasesReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dengue-cases-reports/:id} : Partial updates given fields of an existing dengueCasesReport, field will ignore if it is null
     *
     * @param id the id of the dengueCasesReport to save.
     * @param dengueCasesReport the dengueCasesReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dengueCasesReport,
     * or with status {@code 400 (Bad Request)} if the dengueCasesReport is not valid,
     * or with status {@code 404 (Not Found)} if the dengueCasesReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the dengueCasesReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dengue-cases-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DengueCasesReport> partialUpdateDengueCasesReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DengueCasesReport dengueCasesReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update DengueCasesReport partially : {}, {}", id, dengueCasesReport);
        if (dengueCasesReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dengueCasesReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dengueCasesReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DengueCasesReport> result = dengueCasesReportService.partialUpdate(dengueCasesReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dengueCasesReport.getId().toString())
        );
    }

    /**
     * {@code GET  /dengue-cases-reports} : get all the dengueCasesReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dengueCasesReports in body.
     */
    @GetMapping("/dengue-cases-reports")
    public List<DengueCasesReport> getAllDengueCasesReports() {
        log.debug("REST request to get all DengueCasesReports");
        return dengueCasesReportService.findAll();
    }

    /**
     * {@code GET  /dengue-cases-reports/:id} : get the "id" dengueCasesReport.
     *
     * @param id the id of the dengueCasesReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dengueCasesReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dengue-cases-reports/{id}")
    public ResponseEntity<DengueCasesReport> getDengueCasesReport(@PathVariable Long id) {
        log.debug("REST request to get DengueCasesReport : {}", id);
        Optional<DengueCasesReport> dengueCasesReport = dengueCasesReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dengueCasesReport);
    }

    /**
     * {@code DELETE  /dengue-cases-reports/:id} : delete the "id" dengueCasesReport.
     *
     * @param id the id of the dengueCasesReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dengue-cases-reports/{id}")
    public ResponseEntity<Void> deleteDengueCasesReport(@PathVariable Long id) {
        log.debug("REST request to delete DengueCasesReport : {}", id);
        dengueCasesReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
