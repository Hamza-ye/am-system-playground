package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link LlinsFamilyTarget}.
 */
@RestController
@RequestMapping("/api")
public class LlinsFamilyTargetResource {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyTargetResource.class);

    private static final String ENTITY_NAME = "lLINSFamilyTarget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LlinsFamilyTargetService lLINSFamilyTargetService;

    private final LlinsFamilyTargetRepository lLINSFamilyTargetRepository;

    public LlinsFamilyTargetResource(
        LlinsFamilyTargetService lLINSFamilyTargetService,
        LlinsFamilyTargetRepository lLINSFamilyTargetRepository
    ) {
        this.lLINSFamilyTargetService = lLINSFamilyTargetService;
        this.lLINSFamilyTargetRepository = lLINSFamilyTargetRepository;
    }

    /**
     * {@code POST  /llins-family-targets} : Create a new lLINSFamilyTarget.
     *
     * @param lLINSFamilyTarget the lLINSFamilyTarget to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lLINSFamilyTarget, or with status {@code 400 (Bad Request)} if the lLINSFamilyTarget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-family-targets")
    public ResponseEntity<LlinsFamilyTarget> createLLINSFamilyTarget(@Valid @RequestBody LlinsFamilyTarget lLINSFamilyTarget)
        throws URISyntaxException {
        log.debug("REST request to save LlinsFamilyTarget : {}", lLINSFamilyTarget);
        if (lLINSFamilyTarget.getId() != null) {
            throw new BadRequestAlertException("A new lLINSFamilyTarget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LlinsFamilyTarget result = lLINSFamilyTargetService.save(lLINSFamilyTarget);
        return ResponseEntity
            .created(new URI("/api/llins-family-targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-family-targets/:id} : Updates an existing lLINSFamilyTarget.
     *
     * @param id the id of the lLINSFamilyTarget to save.
     * @param lLINSFamilyTarget the lLINSFamilyTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSFamilyTarget,
     * or with status {@code 400 (Bad Request)} if the lLINSFamilyTarget is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lLINSFamilyTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-family-targets/{id}")
    public ResponseEntity<LlinsFamilyTarget> updateLLINSFamilyTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LlinsFamilyTarget lLINSFamilyTarget
    ) throws URISyntaxException {
        log.debug("REST request to update LlinsFamilyTarget : {}, {}", id, lLINSFamilyTarget);
        if (lLINSFamilyTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSFamilyTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSFamilyTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LlinsFamilyTarget result = lLINSFamilyTargetService.save(lLINSFamilyTarget);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSFamilyTarget.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-family-targets/:id} : Partial updates given fields of an existing lLINSFamilyTarget, field will ignore if it is null
     *
     * @param id the id of the lLINSFamilyTarget to save.
     * @param lLINSFamilyTarget the lLINSFamilyTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSFamilyTarget,
     * or with status {@code 400 (Bad Request)} if the lLINSFamilyTarget is not valid,
     * or with status {@code 404 (Not Found)} if the lLINSFamilyTarget is not found,
     * or with status {@code 500 (Internal Server Error)} if the lLINSFamilyTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-family-targets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LlinsFamilyTarget> partialUpdateLLINSFamilyTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LlinsFamilyTarget lLINSFamilyTarget
    ) throws URISyntaxException {
        log.debug("REST request to partial update LlinsFamilyTarget partially : {}, {}", id, lLINSFamilyTarget);
        if (lLINSFamilyTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSFamilyTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSFamilyTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LlinsFamilyTarget> result = lLINSFamilyTargetService.partialUpdate(lLINSFamilyTarget);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSFamilyTarget.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-family-targets} : get all the lLINSFamilyTargets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lLINSFamilyTargets in body.
     */
    @GetMapping("/llins-family-targets")
    public ResponseEntity<List<LlinsFamilyTarget>> getAllLLINSFamilyTargets(Pageable pageable) {
        log.debug("REST request to get a page of LLINSFamilyTargets");
        Page<LlinsFamilyTarget> page = lLINSFamilyTargetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /llins-family-targets/:id} : get the "id" lLINSFamilyTarget.
     *
     * @param id the id of the lLINSFamilyTarget to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lLINSFamilyTarget, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-family-targets/{id}")
    public ResponseEntity<LlinsFamilyTarget> getLLINSFamilyTarget(@PathVariable Long id) {
        log.debug("REST request to get LlinsFamilyTarget : {}", id);
        Optional<LlinsFamilyTarget> lLINSFamilyTarget = lLINSFamilyTargetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lLINSFamilyTarget);
    }

    /**
     * {@code DELETE  /llins-family-targets/:id} : delete the "id" lLINSFamilyTarget.
     *
     * @param id the id of the lLINSFamilyTarget to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-family-targets/{id}")
    public ResponseEntity<Void> deleteLLINSFamilyTarget(@PathVariable Long id) {
        log.debug("REST request to delete LlinsFamilyTarget : {}", id);
        lLINSFamilyTargetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
