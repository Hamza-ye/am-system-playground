package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.repository.CasesReportClassRepository;
import org.nmcpye.activitiesmanagement.service.CasesReportClassService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link CasesReportClass}.
 */
@RestController
@RequestMapping("/api")
public class CasesReportClassResource {

    private final Logger log = LoggerFactory.getLogger(CasesReportClassResource.class);

    private static final String ENTITY_NAME = "casesReportClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CasesReportClassService casesReportClassService;

    private final CasesReportClassRepository casesReportClassRepository;

    public CasesReportClassResource(
        CasesReportClassService casesReportClassService,
        CasesReportClassRepository casesReportClassRepository
    ) {
        this.casesReportClassService = casesReportClassService;
        this.casesReportClassRepository = casesReportClassRepository;
    }

    /**
     * {@code POST  /cases-report-classes} : Create a new casesReportClass.
     *
     * @param casesReportClass the casesReportClass to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new casesReportClass, or with status {@code 400 (Bad Request)} if the casesReportClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cases-report-classes")
    public ResponseEntity<CasesReportClass> createCasesReportClass(@Valid @RequestBody CasesReportClass casesReportClass)
        throws URISyntaxException {
        log.debug("REST request to save CasesReportClass : {}", casesReportClass);
        if (casesReportClass.getId() != null) {
            throw new BadRequestAlertException("A new casesReportClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CasesReportClass result = casesReportClassService.save(casesReportClass);
        return ResponseEntity
            .created(new URI("/api/cases-report-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cases-report-classes/:id} : Updates an existing casesReportClass.
     *
     * @param id the id of the casesReportClass to save.
     * @param casesReportClass the casesReportClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casesReportClass,
     * or with status {@code 400 (Bad Request)} if the casesReportClass is not valid,
     * or with status {@code 500 (Internal Server Error)} if the casesReportClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cases-report-classes/{id}")
    public ResponseEntity<CasesReportClass> updateCasesReportClass(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CasesReportClass casesReportClass
    ) throws URISyntaxException {
        log.debug("REST request to update CasesReportClass : {}, {}", id, casesReportClass);
        if (casesReportClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casesReportClass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casesReportClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CasesReportClass result = casesReportClassService.save(casesReportClass);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, casesReportClass.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cases-report-classes/:id} : Partial updates given fields of an existing casesReportClass, field will ignore if it is null
     *
     * @param id the id of the casesReportClass to save.
     * @param casesReportClass the casesReportClass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casesReportClass,
     * or with status {@code 400 (Bad Request)} if the casesReportClass is not valid,
     * or with status {@code 404 (Not Found)} if the casesReportClass is not found,
     * or with status {@code 500 (Internal Server Error)} if the casesReportClass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cases-report-classes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CasesReportClass> partialUpdateCasesReportClass(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CasesReportClass casesReportClass
    ) throws URISyntaxException {
        log.debug("REST request to partial update CasesReportClass partially : {}, {}", id, casesReportClass);
        if (casesReportClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casesReportClass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casesReportClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CasesReportClass> result = casesReportClassService.partialUpdate(casesReportClass);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, casesReportClass.getId().toString())
        );
    }

    /**
     * {@code GET  /cases-report-classes} : get all the casesReportClasses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casesReportClasses in body.
     */
    @GetMapping("/cases-report-classes")
    public List<CasesReportClass> getAllCasesReportClasses() {
        log.debug("REST request to get all CasesReportClasses");
        return casesReportClassService.findAll();
    }

    /**
     * {@code GET  /cases-report-classes/:id} : get the "id" casesReportClass.
     *
     * @param id the id of the casesReportClass to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casesReportClass, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cases-report-classes/{id}")
    public ResponseEntity<CasesReportClass> getCasesReportClass(@PathVariable Long id) {
        log.debug("REST request to get CasesReportClass : {}", id);
        Optional<CasesReportClass> casesReportClass = casesReportClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(casesReportClass);
    }

    /**
     * {@code DELETE  /cases-report-classes/:id} : delete the "id" casesReportClass.
     *
     * @param id the id of the casesReportClass to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cases-report-classes/{id}")
    public ResponseEntity<Void> deleteCasesReportClass(@PathVariable Long id) {
        log.debug("REST request to delete CasesReportClass : {}", id);
        casesReportClassService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
