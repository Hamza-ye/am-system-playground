package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageTargetRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageTargetService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget}.
 */
@RestController
@RequestMapping("/api")
public class LlinsVillageTargetResource {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageTargetResource.class);

    private static final String ENTITY_NAME = "llinsVillageTarget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LlinsVillageTargetService llinsVillageTargetService;

    private final LlinsVillageTargetRepository llinsVillageTargetRepository;

    public LlinsVillageTargetResource(
        LlinsVillageTargetService llinsVillageTargetService,
        LlinsVillageTargetRepository llinsVillageTargetRepository
    ) {
        this.llinsVillageTargetService = llinsVillageTargetService;
        this.llinsVillageTargetRepository = llinsVillageTargetRepository;
    }

    /**
     * {@code POST  /llins-village-targets} : Create a new llinsVillageTarget.
     *
     * @param llinsVillageTarget the llinsVillageTarget to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new llinsVillageTarget, or with status {@code 400 (Bad Request)} if the llinsVillageTarget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/llins-village-targets")
    public ResponseEntity<LlinsVillageTarget> createLlinsVillageTarget(@Valid @RequestBody LlinsVillageTarget llinsVillageTarget)
        throws URISyntaxException {
        log.debug("REST request to save LlinsVillageTarget : {}", llinsVillageTarget);
        if (llinsVillageTarget.getId() != null) {
            throw new BadRequestAlertException("A new llinsVillageTarget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LlinsVillageTarget result = llinsVillageTargetService.save(llinsVillageTarget);
        return ResponseEntity
            .created(new URI("/api/llins-village-targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /llins-village-targets/:id} : Updates an existing llinsVillageTarget.
     *
     * @param id the id of the llinsVillageTarget to save.
     * @param llinsVillageTarget the llinsVillageTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsVillageTarget,
     * or with status {@code 400 (Bad Request)} if the llinsVillageTarget is not valid,
     * or with status {@code 500 (Internal Server Error)} if the llinsVillageTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/llins-village-targets/{id}")
    public ResponseEntity<LlinsVillageTarget> updateLlinsVillageTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LlinsVillageTarget llinsVillageTarget
    ) throws URISyntaxException {
        log.debug("REST request to update LlinsVillageTarget : {}, {}", id, llinsVillageTarget);
        if (llinsVillageTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsVillageTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsVillageTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LlinsVillageTarget result = llinsVillageTargetService.save(llinsVillageTarget);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsVillageTarget.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /llins-village-targets/:id} : Partial updates given fields of an existing llinsVillageTarget, field will ignore if it is null
     *
     * @param id the id of the llinsVillageTarget to save.
     * @param llinsVillageTarget the llinsVillageTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated llinsVillageTarget,
     * or with status {@code 400 (Bad Request)} if the llinsVillageTarget is not valid,
     * or with status {@code 404 (Not Found)} if the llinsVillageTarget is not found,
     * or with status {@code 500 (Internal Server Error)} if the llinsVillageTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/llins-village-targets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LlinsVillageTarget> partialUpdateLlinsVillageTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LlinsVillageTarget llinsVillageTarget
    ) throws URISyntaxException {
        log.debug("REST request to partial update LlinsVillageTarget partially : {}, {}", id, llinsVillageTarget);
        if (llinsVillageTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, llinsVillageTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!llinsVillageTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LlinsVillageTarget> result = llinsVillageTargetService.partialUpdate(llinsVillageTarget);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, llinsVillageTarget.getId().toString())
        );
    }

    /**
     * {@code GET  /llins-village-targets} : get all the llinsVillageTargets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of llinsVillageTargets in body.
     */
    @GetMapping("/llins-village-targets")
    public ResponseEntity<List<LlinsVillageTarget>> getAllLlinsVillageTargets(Pageable pageable) {
        log.debug("REST request to get a page of LlinsVillageTargets");
        Page<LlinsVillageTarget> page = llinsVillageTargetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /llins-village-targets/:id} : get the "id" llinsVillageTarget.
     *
     * @param id the id of the llinsVillageTarget to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the llinsVillageTarget, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/llins-village-targets/{id}")
    public ResponseEntity<LlinsVillageTarget> getLlinsVillageTarget(@PathVariable Long id) {
        log.debug("REST request to get LlinsVillageTarget : {}", id);
        Optional<LlinsVillageTarget> llinsVillageTarget = llinsVillageTargetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(llinsVillageTarget);
    }

    /**
     * {@code DELETE  /llins-village-targets/:id} : delete the "id" llinsVillageTarget.
     *
     * @param id the id of the llinsVillageTarget to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/llins-village-targets/{id}")
    public ResponseEntity<Void> deleteLlinsVillageTarget(@PathVariable Long id) {
        log.debug("REST request to delete LlinsVillageTarget : {}", id);
        llinsVillageTargetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
