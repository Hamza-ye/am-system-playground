package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.repository.WorkingDayRepository;
import org.nmcpye.activitiesmanagement.service.WorkingDayService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.WorkingDay}.
 */
@RestController
@RequestMapping("/api")
public class WorkingDayResource {

    private final Logger log = LoggerFactory.getLogger(WorkingDayResource.class);

    private static final String ENTITY_NAME = "workingDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkingDayService workingDayService;

    private final WorkingDayRepository workingDayRepository;

    public WorkingDayResource(WorkingDayService workingDayService, WorkingDayRepository workingDayRepository) {
        this.workingDayService = workingDayService;
        this.workingDayRepository = workingDayRepository;
    }

    /**
     * {@code POST  /working-days} : Create a new workingDay.
     *
     * @param workingDay the workingDay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workingDay, or with status {@code 400 (Bad Request)} if the workingDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/working-days")
    public ResponseEntity<WorkingDay> createWorkingDay(@Valid @RequestBody WorkingDay workingDay) throws URISyntaxException {
        log.debug("REST request to save WorkingDay : {}", workingDay);
        if (workingDay.getId() != null) {
            throw new BadRequestAlertException("A new workingDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkingDay result = workingDayService.save(workingDay);
        return ResponseEntity
            .created(new URI("/api/working-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /working-days/:id} : Updates an existing workingDay.
     *
     * @param id the id of the workingDay to save.
     * @param workingDay the workingDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workingDay,
     * or with status {@code 400 (Bad Request)} if the workingDay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workingDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/working-days/{id}")
    public ResponseEntity<WorkingDay> updateWorkingDay(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkingDay workingDay
    ) throws URISyntaxException {
        log.debug("REST request to update WorkingDay : {}, {}", id, workingDay);
        if (workingDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workingDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workingDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkingDay result = workingDayService.save(workingDay);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workingDay.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /working-days/:id} : Partial updates given fields of an existing workingDay, field will ignore if it is null
     *
     * @param id the id of the workingDay to save.
     * @param workingDay the workingDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workingDay,
     * or with status {@code 400 (Bad Request)} if the workingDay is not valid,
     * or with status {@code 404 (Not Found)} if the workingDay is not found,
     * or with status {@code 500 (Internal Server Error)} if the workingDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/working-days/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkingDay> partialUpdateWorkingDay(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkingDay workingDay
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkingDay partially : {}, {}", id, workingDay);
        if (workingDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workingDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workingDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkingDay> result = workingDayService.partialUpdate(workingDay);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workingDay.getId().toString())
        );
    }

    /**
     * {@code GET  /working-days} : get all the workingDays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workingDays in body.
     */
    @GetMapping("/working-days")
    public List<WorkingDay> getAllWorkingDays() {
        log.debug("REST request to get all WorkingDays");
        return workingDayService.findAll();
    }

    /**
     * {@code GET  /working-days/:id} : get the "id" workingDay.
     *
     * @param id the id of the workingDay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workingDay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/working-days/{id}")
    public ResponseEntity<WorkingDay> getWorkingDay(@PathVariable Long id) {
        log.debug("REST request to get WorkingDay : {}", id);
        Optional<WorkingDay> workingDay = workingDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workingDay);
    }

    /**
     * {@code DELETE  /working-days/:id} : delete the "id" workingDay.
     *
     * @param id the id of the workingDay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/working-days/{id}")
    public ResponseEntity<Void> deleteWorkingDay(@PathVariable Long id) {
        log.debug("REST request to delete WorkingDay : {}", id);
        workingDayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
