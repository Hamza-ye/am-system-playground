package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyTargetRepository;
import org.nmcpye.activitiesmanagement.service.LlinsFamilyTargetService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget}.
 */
@RestController
@RequestMapping("/api")
public class LlinsFamilyTargetResource {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyTargetResource.class);

    private static final String ENTITY_NAME = "llinsFamilyTarget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LlinsFamilyTargetService llinsFamilyTargetService;

    private final LlinsFamilyTargetRepository llinsFamilyTargetRepository;

    public LlinsFamilyTargetResource(
        LlinsFamilyTargetService llinsFamilyTargetService,
        LlinsFamilyTargetRepository llinsFamilyTargetRepository
    ) {
        this.llinsFamilyTargetService = llinsFamilyTargetService;
        this.llinsFamilyTargetRepository = llinsFamilyTargetRepository;
    }

    /**
     * {@code POST  /llins-family-targets} : Create a new llinsFamilyTarget.
     *
     * @param llinsFamilyTarget the llinsFamilyTarget to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new llinsFamilyTarget, or with status {@code 400 (Bad Request)} if the llinsFamilyTarget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-family-targets")
    public ResponseEntity<LlinsFamilyTarget> createLlinsFamilyTarget(@Valid @RequestBody LlinsFamilyTarget llinsFamilyTarget)
        throws URISyntaxException {
        log.debug("REST request to save LlinsFamilyTarget : {}", llinsFamilyTarget);
        if (llinsFamilyTarget.getId() != null) {
            throw new BadRequestAlertException("A new llinsFamilyTarget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LlinsFamilyTarget result = llinsFamilyTargetService.save(llinsFamilyTarget);
        return ResponseEntity
            .created(new URI("/api/llins-family-targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-family-targets/:id} : Updates an existing llinsFamilyTarget.
     *
     * @param id the id of the llinsFamilyTarget to save.
     * @param llinsFamilyTarget the llinsFamilyTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsFamilyTarget,
     * or with status {@code 400 (Bad Request)} if the llinsFamilyTarget is not valid,
     * or with status {@code 500 (Internal Server Error)} if the llinsFamilyTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-family-targets/{id}")
    public ResponseEntity<LlinsFamilyTarget> updateLlinsFamilyTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LlinsFamilyTarget llinsFamilyTarget
    ) throws URISyntaxException {
        log.debug("REST request to update LlinsFamilyTarget : {}, {}", id, llinsFamilyTarget);
        if (llinsFamilyTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsFamilyTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsFamilyTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LlinsFamilyTarget result = llinsFamilyTargetService.save(llinsFamilyTarget);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsFamilyTarget.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-family-targets/:id} : Partial updates given fields of an existing llinsFamilyTarget, field will ignore if it is null
     *
     * @param id the id of the llinsFamilyTarget to save.
     * @param llinsFamilyTarget the llinsFamilyTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsFamilyTarget,
     * or with status {@code 400 (Bad Request)} if the llinsFamilyTarget is not valid,
     * or with status {@code 404 (Not Found)} if the llinsFamilyTarget is not found,
     * or with status {@code 500 (Internal Server Error)} if the llinsFamilyTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-family-targets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LlinsFamilyTarget> partialUpdateLlinsFamilyTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LlinsFamilyTarget llinsFamilyTarget
    ) throws URISyntaxException {
        log.debug("REST request to partial update LlinsFamilyTarget partially : {}, {}", id, llinsFamilyTarget);
        if (llinsFamilyTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsFamilyTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsFamilyTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LlinsFamilyTarget> result = llinsFamilyTargetService.partialUpdate(llinsFamilyTarget);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsFamilyTarget.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-family-targets} : get all the llinsFamilyTargets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of llinsFamilyTargets in body.
     */
    @GetMapping("/llins-family-targets")
    public ResponseEntity<List<LlinsFamilyTarget>> getAllLlinsFamilyTargets(Pageable pageable) {
        log.debug("REST request to get a page of LlinsFamilyTargets");
        Page<LlinsFamilyTarget> page = llinsFamilyTargetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /llins-family-targets/:id} : get the "id" llinsFamilyTarget.
     *
     * @param id the id of the llinsFamilyTarget to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the llinsFamilyTarget, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-family-targets/{id}")
    public ResponseEntity<LlinsFamilyTarget> getLlinsFamilyTarget(@PathVariable Long id) {
        log.debug("REST request to get LlinsFamilyTarget : {}", id);
        Optional<LlinsFamilyTarget> llinsFamilyTarget = llinsFamilyTargetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(llinsFamilyTarget);
    }

    /**
     * {@code DELETE  /llins-family-targets/:id} : delete the "id" llinsFamilyTarget.
     *
     * @param id the id of the llinsFamilyTarget to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-family-targets/{id}")
    public ResponseEntity<Void> deleteLlinsFamilyTarget(@PathVariable Long id) {
        log.debug("REST request to delete LlinsFamilyTarget : {}", id);
        llinsFamilyTargetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
