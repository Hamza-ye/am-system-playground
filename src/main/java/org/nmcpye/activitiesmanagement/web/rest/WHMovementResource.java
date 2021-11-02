package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.WHMovement;
import org.nmcpye.activitiesmanagement.repository.WHMovementRepository;
import org.nmcpye.activitiesmanagement.service.WHMovementService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.WHMovement}.
 */
@RestController
@RequestMapping("/api")
public class WHMovementResource {

    private final Logger log = LoggerFactory.getLogger(WHMovementResource.class);

    private static final String ENTITY_NAME = "wHMovement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WHMovementService wHMovementService;

    private final WHMovementRepository wHMovementRepository;

    public WHMovementResource(WHMovementService wHMovementService, WHMovementRepository wHMovementRepository) {
        this.wHMovementService = wHMovementService;
        this.wHMovementRepository = wHMovementRepository;
    }

    /**
     * {@code POST  /wh-movements} : Create a new wHMovement.
     *
     * @param wHMovement the wHMovement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wHMovement, or with status {@code 400 (Bad Request)} if the wHMovement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wh-movements")
    public ResponseEntity<WHMovement> createWHMovement(@Valid @RequestBody WHMovement wHMovement) throws URISyntaxException {
        log.debug("REST request to save WHMovement : {}", wHMovement);
        if (wHMovement.getId() != null) {
            throw new BadRequestAlertException("A new wHMovement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WHMovement result = wHMovementService.save(wHMovement);
        return ResponseEntity
            .created(new URI("/api/wh-movements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wh-movements/:id} : Updates an existing wHMovement.
     *
     * @param id the id of the wHMovement to save.
     * @param wHMovement the wHMovement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wHMovement,
     * or with status {@code 400 (Bad Request)} if the wHMovement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wHMovement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wh-movements/{id}")
    public ResponseEntity<WHMovement> updateWHMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WHMovement wHMovement
    ) throws URISyntaxException {
        log.debug("REST request to update WHMovement : {}, {}", id, wHMovement);
        if (wHMovement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wHMovement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wHMovementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WHMovement result = wHMovementService.save(wHMovement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wHMovement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wh-movements/:id} : Partial updates given fields of an existing wHMovement, field will ignore if it is null
     *
     * @param id the id of the wHMovement to save.
     * @param wHMovement the wHMovement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wHMovement,
     * or with status {@code 400 (Bad Request)} if the wHMovement is not valid,
     * or with status {@code 404 (Not Found)} if the wHMovement is not found,
     * or with status {@code 500 (Internal Server Error)} if the wHMovement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wh-movements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WHMovement> partialUpdateWHMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WHMovement wHMovement
    ) throws URISyntaxException {
        log.debug("REST request to partial update WHMovement partially : {}, {}", id, wHMovement);
        if (wHMovement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wHMovement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wHMovementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WHMovement> result = wHMovementService.partialUpdate(wHMovement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wHMovement.getId().toString())
        );
    }

    /**
     * {@code GET  /wh-movements} : get all the wHMovements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wHMovements in body.
     */
    @GetMapping("/wh-movements")
    public ResponseEntity<List<WHMovement>> getAllWHMovements(Pageable pageable) {
        log.debug("REST request to get a page of WHMovements");
        Page<WHMovement> page = wHMovementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wh-movements/:id} : get the "id" wHMovement.
     *
     * @param id the id of the wHMovement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wHMovement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wh-movements/{id}")
    public ResponseEntity<WHMovement> getWHMovement(@PathVariable Long id) {
        log.debug("REST request to get WHMovement : {}", id);
        Optional<WHMovement> wHMovement = wHMovementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wHMovement);
    }

    /**
     * {@code DELETE  /wh-movements/:id} : delete the "id" wHMovement.
     *
     * @param id the id of the wHMovement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wh-movements/{id}")
    public ResponseEntity<Void> deleteWHMovement(@PathVariable Long id) {
        log.debug("REST request to delete WHMovement : {}", id);
        wHMovementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
