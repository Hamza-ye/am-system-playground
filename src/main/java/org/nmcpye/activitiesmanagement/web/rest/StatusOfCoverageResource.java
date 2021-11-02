package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.StatusOfCoverage;
import org.nmcpye.activitiesmanagement.repository.StatusOfCoverageRepository;
import org.nmcpye.activitiesmanagement.service.StatusOfCoverageService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.StatusOfCoverage}.
 */
@RestController
@RequestMapping("/api")
public class StatusOfCoverageResource {

    private final Logger log = LoggerFactory.getLogger(StatusOfCoverageResource.class);

    private static final String ENTITY_NAME = "statusOfCoverage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatusOfCoverageService statusOfCoverageService;

    private final StatusOfCoverageRepository statusOfCoverageRepository;

    public StatusOfCoverageResource(
        StatusOfCoverageService statusOfCoverageService,
        StatusOfCoverageRepository statusOfCoverageRepository
    ) {
        this.statusOfCoverageService = statusOfCoverageService;
        this.statusOfCoverageRepository = statusOfCoverageRepository;
    }

    /**
     * {@code POST  /status-of-coverages} : Create a new statusOfCoverage.
     *
     * @param statusOfCoverage the statusOfCoverage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statusOfCoverage, or with status {@code 400 (Bad Request)} if the statusOfCoverage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/status-of-coverages")
    public ResponseEntity<StatusOfCoverage> createStatusOfCoverage(@Valid @RequestBody StatusOfCoverage statusOfCoverage)
        throws URISyntaxException {
        log.debug("REST request to save StatusOfCoverage : {}", statusOfCoverage);
        if (statusOfCoverage.getId() != null) {
            throw new BadRequestAlertException("A new statusOfCoverage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StatusOfCoverage result = statusOfCoverageService.save(statusOfCoverage);
        return ResponseEntity
            .created(new URI("/api/status-of-coverages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /status-of-coverages/:id} : Updates an existing statusOfCoverage.
     *
     * @param id the id of the statusOfCoverage to save.
     * @param statusOfCoverage the statusOfCoverage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusOfCoverage,
     * or with status {@code 400 (Bad Request)} if the statusOfCoverage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statusOfCoverage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/status-of-coverages/{id}")
    public ResponseEntity<StatusOfCoverage> updateStatusOfCoverage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StatusOfCoverage statusOfCoverage
    ) throws URISyntaxException {
        log.debug("REST request to update StatusOfCoverage : {}, {}", id, statusOfCoverage);
        if (statusOfCoverage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusOfCoverage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusOfCoverageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StatusOfCoverage result = statusOfCoverageService.save(statusOfCoverage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusOfCoverage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /status-of-coverages/:id} : Partial updates given fields of an existing statusOfCoverage, field will ignore if it is null
     *
     * @param id the id of the statusOfCoverage to save.
     * @param statusOfCoverage the statusOfCoverage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusOfCoverage,
     * or with status {@code 400 (Bad Request)} if the statusOfCoverage is not valid,
     * or with status {@code 404 (Not Found)} if the statusOfCoverage is not found,
     * or with status {@code 500 (Internal Server Error)} if the statusOfCoverage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/status-of-coverages/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<StatusOfCoverage> partialUpdateStatusOfCoverage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StatusOfCoverage statusOfCoverage
    ) throws URISyntaxException {
        log.debug("REST request to partial update StatusOfCoverage partially : {}, {}", id, statusOfCoverage);
        if (statusOfCoverage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, statusOfCoverage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statusOfCoverageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StatusOfCoverage> result = statusOfCoverageService.partialUpdate(statusOfCoverage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusOfCoverage.getId().toString())
        );
    }

    /**
     * {@code GET  /status-of-coverages} : get all the statusOfCoverages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statusOfCoverages in body.
     */
    @GetMapping("/status-of-coverages")
    public List<StatusOfCoverage> getAllStatusOfCoverages() {
        log.debug("REST request to get all StatusOfCoverages");
        return statusOfCoverageService.findAll();
    }

    /**
     * {@code GET  /status-of-coverages/:id} : get the "id" statusOfCoverage.
     *
     * @param id the id of the statusOfCoverage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusOfCoverage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/status-of-coverages/{id}")
    public ResponseEntity<StatusOfCoverage> getStatusOfCoverage(@PathVariable Long id) {
        log.debug("REST request to get StatusOfCoverage : {}", id);
        Optional<StatusOfCoverage> statusOfCoverage = statusOfCoverageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(statusOfCoverage);
    }

    /**
     * {@code DELETE  /status-of-coverages/:id} : delete the "id" statusOfCoverage.
     *
     * @param id the id of the statusOfCoverage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/status-of-coverages/{id}")
    public ResponseEntity<Void> deleteStatusOfCoverage(@PathVariable Long id) {
        log.debug("REST request to delete StatusOfCoverage : {}", id);
        statusOfCoverageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
