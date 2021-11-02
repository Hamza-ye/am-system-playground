package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaReportVersion1;
import org.nmcpye.activitiesmanagement.repository.CHVMalariaReportVersion1Repository;
import org.nmcpye.activitiesmanagement.service.CHVMalariaReportVersion1Service;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.CHVMalariaReportVersion1}.
 */
@RestController
@RequestMapping("/api")
public class CHVMalariaReportVersion1Resource {

    private final Logger log = LoggerFactory.getLogger(CHVMalariaReportVersion1Resource.class);

    private static final String ENTITY_NAME = "cHVMalariaReportVersion1";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CHVMalariaReportVersion1Service cHVMalariaReportVersion1Service;

    private final CHVMalariaReportVersion1Repository cHVMalariaReportVersion1Repository;

    public CHVMalariaReportVersion1Resource(
        CHVMalariaReportVersion1Service cHVMalariaReportVersion1Service,
        CHVMalariaReportVersion1Repository cHVMalariaReportVersion1Repository
    ) {
        this.cHVMalariaReportVersion1Service = cHVMalariaReportVersion1Service;
        this.cHVMalariaReportVersion1Repository = cHVMalariaReportVersion1Repository;
    }

    /**
     * {@code POST  /chv-malaria-report-version-1-s} : Create a new cHVMalariaReportVersion1.
     *
     * @param cHVMalariaReportVersion1 the cHVMalariaReportVersion1 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cHVMalariaReportVersion1, or with status {@code 400 (Bad Request)} if the cHVMalariaReportVersion1 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chv-malaria-report-version-1-s")
    public ResponseEntity<CHVMalariaReportVersion1> createCHVMalariaReportVersion1(
        @Valid @RequestBody CHVMalariaReportVersion1 cHVMalariaReportVersion1
    ) throws URISyntaxException {
        log.debug("REST request to save CHVMalariaReportVersion1 : {}", cHVMalariaReportVersion1);
        if (cHVMalariaReportVersion1.getId() != null) {
            throw new BadRequestAlertException("A new cHVMalariaReportVersion1 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CHVMalariaReportVersion1 result = cHVMalariaReportVersion1Service.save(cHVMalariaReportVersion1);
        return ResponseEntity
            .created(new URI("/api/chv-malaria-report-version-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chv-malaria-report-version-1-s/:id} : Updates an existing cHVMalariaReportVersion1.
     *
     * @param id the id of the cHVMalariaReportVersion1 to save.
     * @param cHVMalariaReportVersion1 the cHVMalariaReportVersion1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHVMalariaReportVersion1,
     * or with status {@code 400 (Bad Request)} if the cHVMalariaReportVersion1 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cHVMalariaReportVersion1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chv-malaria-report-version-1-s/{id}")
    public ResponseEntity<CHVMalariaReportVersion1> updateCHVMalariaReportVersion1(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CHVMalariaReportVersion1 cHVMalariaReportVersion1
    ) throws URISyntaxException {
        log.debug("REST request to update CHVMalariaReportVersion1 : {}, {}", id, cHVMalariaReportVersion1);
        if (cHVMalariaReportVersion1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHVMalariaReportVersion1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVMalariaReportVersion1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CHVMalariaReportVersion1 result = cHVMalariaReportVersion1Service.save(cHVMalariaReportVersion1);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHVMalariaReportVersion1.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chv-malaria-report-version-1-s/:id} : Partial updates given fields of an existing cHVMalariaReportVersion1, field will ignore if it is null
     *
     * @param id the id of the cHVMalariaReportVersion1 to save.
     * @param cHVMalariaReportVersion1 the cHVMalariaReportVersion1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHVMalariaReportVersion1,
     * or with status {@code 400 (Bad Request)} if the cHVMalariaReportVersion1 is not valid,
     * or with status {@code 404 (Not Found)} if the cHVMalariaReportVersion1 is not found,
     * or with status {@code 500 (Internal Server Error)} if the cHVMalariaReportVersion1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chv-malaria-report-version-1-s/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CHVMalariaReportVersion1> partialUpdateCHVMalariaReportVersion1(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CHVMalariaReportVersion1 cHVMalariaReportVersion1
    ) throws URISyntaxException {
        log.debug("REST request to partial update CHVMalariaReportVersion1 partially : {}, {}", id, cHVMalariaReportVersion1);
        if (cHVMalariaReportVersion1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHVMalariaReportVersion1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVMalariaReportVersion1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CHVMalariaReportVersion1> result = cHVMalariaReportVersion1Service.partialUpdate(cHVMalariaReportVersion1);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHVMalariaReportVersion1.getId().toString())
        );
    }

    /**
     * {@code GET  /chv-malaria-report-version-1-s} : get all the cHVMalariaReportVersion1s.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cHVMalariaReportVersion1s in body.
     */
    @GetMapping("/chv-malaria-report-version-1-s")
    public List<CHVMalariaReportVersion1> getAllCHVMalariaReportVersion1s() {
        log.debug("REST request to get all CHVMalariaReportVersion1s");
        return cHVMalariaReportVersion1Service.findAll();
    }

    /**
     * {@code GET  /chv-malaria-report-version-1-s/:id} : get the "id" cHVMalariaReportVersion1.
     *
     * @param id the id of the cHVMalariaReportVersion1 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cHVMalariaReportVersion1, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chv-malaria-report-version-1-s/{id}")
    public ResponseEntity<CHVMalariaReportVersion1> getCHVMalariaReportVersion1(@PathVariable Long id) {
        log.debug("REST request to get CHVMalariaReportVersion1 : {}", id);
        Optional<CHVMalariaReportVersion1> cHVMalariaReportVersion1 = cHVMalariaReportVersion1Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(cHVMalariaReportVersion1);
    }

    /**
     * {@code DELETE  /chv-malaria-report-version-1-s/:id} : delete the "id" cHVMalariaReportVersion1.
     *
     * @param id the id of the cHVMalariaReportVersion1 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chv-malaria-report-version-1-s/{id}")
    public ResponseEntity<Void> deleteCHVMalariaReportVersion1(@PathVariable Long id) {
        log.debug("REST request to delete CHVMalariaReportVersion1 : {}", id);
        cHVMalariaReportVersion1Service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
