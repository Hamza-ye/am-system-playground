package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;
import org.nmcpye.activitiesmanagement.repository.ChvMalariaCaseReportRepository;
import org.nmcpye.activitiesmanagement.service.ChvMalariaCaseReportService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport}.
 */
@RestController
@RequestMapping("/api")
public class ChvMalariaCaseReportResource {

    private final Logger log = LoggerFactory.getLogger(ChvMalariaCaseReportResource.class);

    private static final String ENTITY_NAME = "chvMalariaCaseReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChvMalariaCaseReportService chvMalariaCaseReportService;

    private final ChvMalariaCaseReportRepository chvMalariaCaseReportRepository;

    public ChvMalariaCaseReportResource(
        ChvMalariaCaseReportService chvMalariaCaseReportService,
        ChvMalariaCaseReportRepository chvMalariaCaseReportRepository
    ) {
        this.chvMalariaCaseReportService = chvMalariaCaseReportService;
        this.chvMalariaCaseReportRepository = chvMalariaCaseReportRepository;
    }

    /**
     * {@code POST  /chv-malaria-case-reports} : Create a new chvMalariaCaseReport.
     *
     * @param chvMalariaCaseReport the chvMalariaCaseReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chvMalariaCaseReport, or with status {@code 400 (Bad Request)} if the chvMalariaCaseReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chv-malaria-case-reports")
    public ResponseEntity<ChvMalariaCaseReport> createChvMalariaCaseReport(@Valid @RequestBody ChvMalariaCaseReport chvMalariaCaseReport)
        throws URISyntaxException {
        log.debug("REST request to save ChvMalariaCaseReport : {}", chvMalariaCaseReport);
        if (chvMalariaCaseReport.getId() != null) {
            throw new BadRequestAlertException("A new chvMalariaCaseReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChvMalariaCaseReport result = chvMalariaCaseReportService.save(chvMalariaCaseReport);
        return ResponseEntity
            .created(new URI("/api/chv-malaria-case-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chv-malaria-case-reports/:id} : Updates an existing chvMalariaCaseReport.
     *
     * @param id the id of the chvMalariaCaseReport to save.
     * @param chvMalariaCaseReport the chvMalariaCaseReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chvMalariaCaseReport,
     * or with status {@code 400 (Bad Request)} if the chvMalariaCaseReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chvMalariaCaseReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chv-malaria-case-reports/{id}")
    public ResponseEntity<ChvMalariaCaseReport> updateChvMalariaCaseReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChvMalariaCaseReport chvMalariaCaseReport
    ) throws URISyntaxException {
        log.debug("REST request to update ChvMalariaCaseReport : {}, {}", id, chvMalariaCaseReport);
        if (chvMalariaCaseReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chvMalariaCaseReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvMalariaCaseReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChvMalariaCaseReport result = chvMalariaCaseReportService.save(chvMalariaCaseReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chvMalariaCaseReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chv-malaria-case-reports/:id} : Partial updates given fields of an existing chvMalariaCaseReport, field will ignore if it is null
     *
     * @param id the id of the chvMalariaCaseReport to save.
     * @param chvMalariaCaseReport the chvMalariaCaseReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chvMalariaCaseReport,
     * or with status {@code 400 (Bad Request)} if the chvMalariaCaseReport is not valid,
     * or with status {@code 404 (Not Found)} if the chvMalariaCaseReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the chvMalariaCaseReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chv-malaria-case-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ChvMalariaCaseReport> partialUpdateChvMalariaCaseReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChvMalariaCaseReport chvMalariaCaseReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChvMalariaCaseReport partially : {}, {}", id, chvMalariaCaseReport);
        if (chvMalariaCaseReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chvMalariaCaseReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvMalariaCaseReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChvMalariaCaseReport> result = chvMalariaCaseReportService.partialUpdate(chvMalariaCaseReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chvMalariaCaseReport.getId().toString())
        );
    }

    /**
     * {@code GET  /chv-malaria-case-reports} : get all the chvMalariaCaseReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chvMalariaCaseReports in body.
     */
    @GetMapping("/chv-malaria-case-reports")
    public ResponseEntity<List<ChvMalariaCaseReport>> getAllChvMalariaCaseReports(Pageable pageable) {
        log.debug("REST request to get a page of ChvMalariaCaseReports");
        Page<ChvMalariaCaseReport> page = chvMalariaCaseReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chv-malaria-case-reports/:id} : get the "id" chvMalariaCaseReport.
     *
     * @param id the id of the chvMalariaCaseReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chvMalariaCaseReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chv-malaria-case-reports/{id}")
    public ResponseEntity<ChvMalariaCaseReport> getChvMalariaCaseReport(@PathVariable Long id) {
        log.debug("REST request to get ChvMalariaCaseReport : {}", id);
        Optional<ChvMalariaCaseReport> chvMalariaCaseReport = chvMalariaCaseReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chvMalariaCaseReport);
    }

    /**
     * {@code DELETE  /chv-malaria-case-reports/:id} : delete the "id" chvMalariaCaseReport.
     *
     * @param id the id of the chvMalariaCaseReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chv-malaria-case-reports/{id}")
    public ResponseEntity<Void> deleteChvMalariaCaseReport(@PathVariable Long id) {
        log.debug("REST request to delete ChvMalariaCaseReport : {}", id);
        chvMalariaCaseReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
