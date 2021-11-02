package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget;
import org.nmcpye.activitiesmanagement.repository.LLINSVillageTargetRepository;
import org.nmcpye.activitiesmanagement.service.LLINSVillageTargetService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget}.
 */
@RestController
@RequestMapping("/api")
public class LLINSVillageTargetResource {

    private final Logger log = LoggerFactory.getLogger(LLINSVillageTargetResource.class);

    private static final String ENTITY_NAME = "lLINSVillageTarget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LLINSVillageTargetService lLINSVillageTargetService;

    private final LLINSVillageTargetRepository lLINSVillageTargetRepository;

    public LLINSVillageTargetResource(
        LLINSVillageTargetService lLINSVillageTargetService,
        LLINSVillageTargetRepository lLINSVillageTargetRepository
    ) {
        this.lLINSVillageTargetService = lLINSVillageTargetService;
        this.lLINSVillageTargetRepository = lLINSVillageTargetRepository;
    }

    /**
     * {@code POST  /llins-village-targets} : Create a new lLINSVillageTarget.
     *
     * @param lLINSVillageTarget the lLINSVillageTarget to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lLINSVillageTarget, or with status {@code 400 (Bad Request)} if the lLINSVillageTarget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-village-targets")
    public ResponseEntity<LLINSVillageTarget> createLLINSVillageTarget(@Valid @RequestBody LLINSVillageTarget lLINSVillageTarget)
        throws URISyntaxException {
        log.debug("REST request to save LLINSVillageTarget : {}", lLINSVillageTarget);
        if (lLINSVillageTarget.getId() != null) {
            throw new BadRequestAlertException("A new lLINSVillageTarget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LLINSVillageTarget result = lLINSVillageTargetService.save(lLINSVillageTarget);
        return ResponseEntity
            .created(new URI("/api/llins-village-targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-village-targets/:id} : Updates an existing lLINSVillageTarget.
     *
     * @param id the id of the lLINSVillageTarget to save.
     * @param lLINSVillageTarget the lLINSVillageTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSVillageTarget,
     * or with status {@code 400 (Bad Request)} if the lLINSVillageTarget is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lLINSVillageTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-village-targets/{id}")
    public ResponseEntity<LLINSVillageTarget> updateLLINSVillageTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LLINSVillageTarget lLINSVillageTarget
    ) throws URISyntaxException {
        log.debug("REST request to update LLINSVillageTarget : {}, {}", id, lLINSVillageTarget);
        if (lLINSVillageTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSVillageTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSVillageTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LLINSVillageTarget result = lLINSVillageTargetService.save(lLINSVillageTarget);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSVillageTarget.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-village-targets/:id} : Partial updates given fields of an existing lLINSVillageTarget, field will ignore if it is null
     *
     * @param id the id of the lLINSVillageTarget to save.
     * @param lLINSVillageTarget the lLINSVillageTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lLINSVillageTarget,
     * or with status {@code 400 (Bad Request)} if the lLINSVillageTarget is not valid,
     * or with status {@code 404 (Not Found)} if the lLINSVillageTarget is not found,
     * or with status {@code 500 (Internal Server Error)} if the lLINSVillageTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-village-targets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LLINSVillageTarget> partialUpdateLLINSVillageTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LLINSVillageTarget lLINSVillageTarget
    ) throws URISyntaxException {
        log.debug("REST request to partial update LLINSVillageTarget partially : {}, {}", id, lLINSVillageTarget);
        if (lLINSVillageTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lLINSVillageTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lLINSVillageTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LLINSVillageTarget> result = lLINSVillageTargetService.partialUpdate(lLINSVillageTarget);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lLINSVillageTarget.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-village-targets} : get all the lLINSVillageTargets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lLINSVillageTargets in body.
     */
    @GetMapping("/llins-village-targets")
    public ResponseEntity<List<LLINSVillageTarget>> getAllLLINSVillageTargets(Pageable pageable) {
        log.debug("REST request to get a page of LLINSVillageTargets");
        Page<LLINSVillageTarget> page = lLINSVillageTargetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /llins-village-targets/:id} : get the "id" lLINSVillageTarget.
     *
     * @param id the id of the lLINSVillageTarget to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lLINSVillageTarget, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-village-targets/{id}")
    public ResponseEntity<LLINSVillageTarget> getLLINSVillageTarget(@PathVariable Long id) {
        log.debug("REST request to get LLINSVillageTarget : {}", id);
        Optional<LLINSVillageTarget> lLINSVillageTarget = lLINSVillageTargetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lLINSVillageTarget);
    }

    /**
     * {@code DELETE  /llins-village-targets/:id} : delete the "id" lLINSVillageTarget.
     *
     * @param id the id of the lLINSVillageTarget to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-village-targets/{id}")
    public ResponseEntity<Void> deleteLLINSVillageTarget(@PathVariable Long id) {
        log.debug("REST request to delete LLINSVillageTarget : {}", id);
        lLINSVillageTargetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
