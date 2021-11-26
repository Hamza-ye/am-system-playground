package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaReportVersion1;
import org.nmcpye.activitiesmanagement.repository.ChvMalariaReportVersion1Repository;
import org.nmcpye.activitiesmanagement.service.ChvMalariaReportVersion1Service;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.ChvMalariaReportVersion1}.
 */
@RestController
@RequestMapping("/api")
public class ChvMalariaReportVersion1Resource {

    private final Logger log = LoggerFactory.getLogger(ChvMalariaReportVersion1Resource.class);

    private static final String ENTITY_NAME = "chvMalariaReportVersion1";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChvMalariaReportVersion1Service chvMalariaReportVersion1Service;

    private final ChvMalariaReportVersion1Repository chvMalariaReportVersion1Repository;

    public ChvMalariaReportVersion1Resource(
        ChvMalariaReportVersion1Service chvMalariaReportVersion1Service,
        ChvMalariaReportVersion1Repository chvMalariaReportVersion1Repository
    ) {
        this.chvMalariaReportVersion1Service = chvMalariaReportVersion1Service;
        this.chvMalariaReportVersion1Repository = chvMalariaReportVersion1Repository;
    }

    /**
     * {@code POST  /chv-malaria-report-version-1-s} : Create a new chvMalariaReportVersion1.
     *
     * @param chvMalariaReportVersion1 the chvMalariaReportVersion1 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chvMalariaReportVersion1, or with status {@code 400 (Bad Request)} if the chvMalariaReportVersion1 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chv-malaria-report-version-1-s")
    public ResponseEntity<ChvMalariaReportVersion1> createChvMalariaReportVersion1(
        @Valid @RequestBody ChvMalariaReportVersion1 chvMalariaReportVersion1
    ) throws URISyntaxException {
        log.debug("REST request to save ChvMalariaReportVersion1 : {}", chvMalariaReportVersion1);
        if (chvMalariaReportVersion1.getId() != null) {
            throw new BadRequestAlertException("A new chvMalariaReportVersion1 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChvMalariaReportVersion1 result = chvMalariaReportVersion1Service.save(chvMalariaReportVersion1);
        return ResponseEntity
            .created(new URI("/api/chv-malaria-report-version-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chv-malaria-report-version-1-s/:id} : Updates an existing chvMalariaReportVersion1.
     *
     * @param id the id of the chvMalariaReportVersion1 to save.
     * @param chvMalariaReportVersion1 the chvMalariaReportVersion1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chvMalariaReportVersion1,
     * or with status {@code 400 (Bad Request)} if the chvMalariaReportVersion1 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chvMalariaReportVersion1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chv-malaria-report-version-1-s/{id}")
    public ResponseEntity<ChvMalariaReportVersion1> updateChvMalariaReportVersion1(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChvMalariaReportVersion1 chvMalariaReportVersion1
    ) throws URISyntaxException {
        log.debug("REST request to update ChvMalariaReportVersion1 : {}, {}", id, chvMalariaReportVersion1);
        if (chvMalariaReportVersion1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chvMalariaReportVersion1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvMalariaReportVersion1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChvMalariaReportVersion1 result = chvMalariaReportVersion1Service.save(chvMalariaReportVersion1);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chvMalariaReportVersion1.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chv-malaria-report-version-1-s/:id} : Partial updates given fields of an existing chvMalariaReportVersion1, field will ignore if it is null
     *
     * @param id the id of the chvMalariaReportVersion1 to save.
     * @param chvMalariaReportVersion1 the chvMalariaReportVersion1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chvMalariaReportVersion1,
     * or with status {@code 400 (Bad Request)} if the chvMalariaReportVersion1 is not valid,
     * or with status {@code 404 (Not Found)} if the chvMalariaReportVersion1 is not found,
     * or with status {@code 500 (Internal Server Error)} if the chvMalariaReportVersion1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chv-malaria-report-version-1-s/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ChvMalariaReportVersion1> partialUpdateChvMalariaReportVersion1(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChvMalariaReportVersion1 chvMalariaReportVersion1
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChvMalariaReportVersion1 partially : {}, {}", id, chvMalariaReportVersion1);
        if (chvMalariaReportVersion1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chvMalariaReportVersion1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvMalariaReportVersion1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChvMalariaReportVersion1> result = chvMalariaReportVersion1Service.partialUpdate(chvMalariaReportVersion1);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chvMalariaReportVersion1.getId().toString())
        );
    }

    /**
     * {@code GET  /chv-malaria-report-version-1-s} : get all the chvMalariaReportVersion1s.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chvMalariaReportVersion1s in body.
     */
    @GetMapping("/chv-malaria-report-version-1-s")
    public List<ChvMalariaReportVersion1> getAllChvMalariaReportVersion1s() {
        log.debug("REST request to get all ChvMalariaReportVersion1s");
        return chvMalariaReportVersion1Service.findAll();
    }

    /**
     * {@code GET  /chv-malaria-report-version-1-s/:id} : get the "id" chvMalariaReportVersion1.
     *
     * @param id the id of the chvMalariaReportVersion1 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chvMalariaReportVersion1, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chv-malaria-report-version-1-s/{id}")
    public ResponseEntity<ChvMalariaReportVersion1> getChvMalariaReportVersion1(@PathVariable Long id) {
        log.debug("REST request to get ChvMalariaReportVersion1 : {}", id);
        Optional<ChvMalariaReportVersion1> chvMalariaReportVersion1 = chvMalariaReportVersion1Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(chvMalariaReportVersion1);
    }

    /**
     * {@code DELETE  /chv-malaria-report-version-1-s/:id} : delete the "id" chvMalariaReportVersion1.
     *
     * @param id the id of the chvMalariaReportVersion1 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chv-malaria-report-version-1-s/{id}")
    public ResponseEntity<Void> deleteChvMalariaReportVersion1(@PathVariable Long id) {
        log.debug("REST request to delete ChvMalariaReportVersion1 : {}", id);
        chvMalariaReportVersion1Service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
