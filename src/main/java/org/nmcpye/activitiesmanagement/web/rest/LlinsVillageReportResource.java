package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageReportService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LlinsVillageReport}.
 */
@RestController
@RequestMapping("/api")
public class LlinsVillageReportResource {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageReportResource.class);

    private static final String ENTITY_NAME = "llinsVillageReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LlinsVillageReportService llinsVillageReportService;

    private final LlinsVillageReportRepository llinsVillageReportRepository;

    public LlinsVillageReportResource(
        LlinsVillageReportService llinsVillageReportService,
        LlinsVillageReportRepository llinsVillageReportRepository
    ) {
        this.llinsVillageReportService = llinsVillageReportService;
        this.llinsVillageReportRepository = llinsVillageReportRepository;
    }

    /**
     * {@code POST  /llins-village-reports} : Create a new llinsVillageReport.
     *
     * @param llinsVillageReport the llinsVillageReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new llinsVillageReport, or with status {@code 400 (Bad Request)} if the llinsVillageReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-village-reports")
    public ResponseEntity<LlinsVillageReport> createLlinsVillageReport(@Valid @RequestBody LlinsVillageReport llinsVillageReport)
        throws URISyntaxException {
        log.debug("REST request to save LlinsVillageReport : {}", llinsVillageReport);
        if (llinsVillageReport.getId() != null) {
            throw new BadRequestAlertException("A new llinsVillageReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LlinsVillageReport result = llinsVillageReportService.save(llinsVillageReport);
        return ResponseEntity
            .created(new URI("/api/llins-village-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-village-reports/:id} : Updates an existing llinsVillageReport.
     *
     * @param id the id of the llinsVillageReport to save.
     * @param llinsVillageReport the llinsVillageReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsVillageReport,
     * or with status {@code 400 (Bad Request)} if the llinsVillageReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the llinsVillageReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-village-reports/{id}")
    public ResponseEntity<LlinsVillageReport> updateLlinsVillageReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LlinsVillageReport llinsVillageReport
    ) throws URISyntaxException {
        log.debug("REST request to update LlinsVillageReport : {}, {}", id, llinsVillageReport);
        if (llinsVillageReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsVillageReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsVillageReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LlinsVillageReport result = llinsVillageReportService.save(llinsVillageReport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsVillageReport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-village-reports/:id} : Partial updates given fields of an existing llinsVillageReport, field will ignore if it is null
     *
     * @param id the id of the llinsVillageReport to save.
     * @param llinsVillageReport the llinsVillageReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsVillageReport,
     * or with status {@code 400 (Bad Request)} if the llinsVillageReport is not valid,
     * or with status {@code 404 (Not Found)} if the llinsVillageReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the llinsVillageReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-village-reports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LlinsVillageReport> partialUpdateLlinsVillageReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LlinsVillageReport llinsVillageReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update LlinsVillageReport partially : {}, {}", id, llinsVillageReport);
        if (llinsVillageReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsVillageReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsVillageReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LlinsVillageReport> result = llinsVillageReportService.partialUpdate(llinsVillageReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsVillageReport.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-village-reports} : get all the llinsVillageReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of llinsVillageReports in body.
     */
    @GetMapping("/llins-village-reports")
    public List<LlinsVillageReport> getAllLlinsVillageReports() {
        log.debug("REST request to get all LlinsVillageReports");
        return llinsVillageReportService.findAll();
    }

    /**
     * {@code GET  /llins-village-reports/:id} : get the "id" llinsVillageReport.
     *
     * @param id the id of the llinsVillageReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the llinsVillageReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-village-reports/{id}")
    public ResponseEntity<LlinsVillageReport> getLlinsVillageReport(@PathVariable Long id) {
        log.debug("REST request to get LlinsVillageReport : {}", id);
        Optional<LlinsVillageReport> llinsVillageReport = llinsVillageReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(llinsVillageReport);
    }

    /**
     * {@code DELETE  /llins-village-reports/:id} : delete the "id" llinsVillageReport.
     *
     * @param id the id of the llinsVillageReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-village-reports/{id}")
    public ResponseEntity<Void> deleteLlinsVillageReport(@PathVariable Long id) {
        log.debug("REST request to delete LlinsVillageReport : {}", id);
        llinsVillageReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
