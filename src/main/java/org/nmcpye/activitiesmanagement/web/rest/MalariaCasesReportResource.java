package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.repository.MalariaCasesReportRepository;
import org.nmcpye.activitiesmanagement.service.MalariaCasesReportService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport}.
 */
@RestController
@RequestMapping("/api")
public class MalariaCasesReportResource {

    private final Logger log = LoggerFactory.getLogger(MalariaCasesReportResource.class);

    private static final String ENTITY_NAME = "malariaCasesReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MalariaCasesReportService malariaCasesReportService;

    private final MalariaCasesReportRepository malariaCasesReportRepository;

    public MalariaCasesReportResource(
        MalariaCasesReportService malariaCasesReportService,
        MalariaCasesReportRepository malariaCasesReportRepository
    ) {
        this.malariaCasesReportService = malariaCasesReportService;
        this.malariaCasesReportRepository = malariaCasesReportRepository;
    }

    /**
     * {@code POST  /malaria-cases-reports} : Create a new malariaCasesReport.
     *
     * @param malariaCasesReport the malariaCasesReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new malariaCasesReport, or with status {@code 400 (Bad Request)} if the malariaCasesReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/malaria-cases-reports")
    public ResponseEntity<MalariaCasesReport> createMalariaCasesReport(@Valid @RequestBody MalariaCasesReport malariaCasesReport)
        throws URISyntaxException {
        log.debug("REST request to save MalariaCasesReport : {}", malariaCasesReport);
        if (malariaCasesReport.getId() != null) {
            throw new BadRequestAlertException("A new malariaCasesReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MalariaCasesReport result = malariaCasesReportService.save(malariaCasesReport);
        return ResponseEntity
            .created(new URI("/api/malaria-cases-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /malaria-cases-reports/:id} : Updates an existing malariaCasesReport.
     *
     * @param id the id of the malariaCasesReport to save.
     * @param malariaCasesReport the malariaCasesReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated malariaCasesReport,
     * or with status {@code 400 (Bad Request)} if the malariaCasesReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the malariaCasesReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/malaria-cases-reports/{id}")
    public ResponseEntity<MalariaCasesReport> updateMalariaCasesReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MalariaCasesReport malariaCasesReport
    ) throws URISyntaxException {
        log.debug("REST request to update MalariaCasesReport : {}, {}", id, malariaCasesReport);
        if (malariaCasesReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, malariaCasesReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!malariaCasesReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MalariaCasesReport result = malariaCasesReportService.save(malariaCasesReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, malariaCasesReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /malaria-cases-reports/:id} : Partial updates given fields of an existing malariaCasesReport, field will ignore if it is null
     *
     * @param id the id of the malariaCasesReport to save.
     * @param malariaCasesReport the malariaCasesReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated malariaCasesReport,
     * or with status {@code 400 (Bad Request)} if the malariaCasesReport is not valid,
     * or with status {@code 404 (Not Found)} if the malariaCasesReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the malariaCasesReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/malaria-cases-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MalariaCasesReport> partialUpdateMalariaCasesReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MalariaCasesReport malariaCasesReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update MalariaCasesReport partially : {}, {}", id, malariaCasesReport);
        if (malariaCasesReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, malariaCasesReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!malariaCasesReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MalariaCasesReport> result = malariaCasesReportService.partialUpdate(malariaCasesReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, malariaCasesReport.getId().toString())
        );
    }

    /**
     * {@code GET  /malaria-cases-reports} : get all the malariaCasesReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of malariaCasesReports in body.
     */
    @GetMapping("/malaria-cases-reports")
    public ResponseEntity<List<MalariaCasesReport>> getAllMalariaCasesReports(Pageable pageable) {
        log.debug("REST request to get a page of MalariaCasesReports");
        Page<MalariaCasesReport> page = malariaCasesReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /malaria-cases-reports/:id} : get the "id" malariaCasesReport.
     *
     * @param id the id of the malariaCasesReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the malariaCasesReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/malaria-cases-reports/{id}")
    public ResponseEntity<MalariaCasesReport> getMalariaCasesReport(@PathVariable Long id) {
        log.debug("REST request to get MalariaCasesReport : {}", id);
        Optional<MalariaCasesReport> malariaCasesReport = malariaCasesReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(malariaCasesReport);
    }

    /**
     * {@code DELETE  /malaria-cases-reports/:id} : delete the "id" malariaCasesReport.
     *
     * @param id the id of the malariaCasesReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/malaria-cases-reports/{id}")
    public ResponseEntity<Void> deleteMalariaCasesReport(@PathVariable Long id) {
        log.debug("REST request to delete MalariaCasesReport : {}", id);
        malariaCasesReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
