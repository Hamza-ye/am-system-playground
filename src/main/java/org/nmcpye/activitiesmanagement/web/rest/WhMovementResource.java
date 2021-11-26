package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.WhMovement;
import org.nmcpye.activitiesmanagement.repository.WhMovementRepository;
import org.nmcpye.activitiesmanagement.service.WhMovementService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.WhMovement}.
 */
@RestController
@RequestMapping("/api")
public class WhMovementResource {

    private final Logger log = LoggerFactory.getLogger(WhMovementResource.class);

    private static final String ENTITY_NAME = "whMovement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WhMovementService whMovementService;

    private final WhMovementRepository whMovementRepository;

    public WhMovementResource(WhMovementService whMovementService, WhMovementRepository whMovementRepository) {
        this.whMovementService = whMovementService;
        this.whMovementRepository = whMovementRepository;
    }

    /**
     * {@code POST  /wh-movements} : Create a new whMovement.
     *
     * @param whMovement the whMovement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new whMovement, or with status {@code 400 (Bad Request)} if the whMovement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wh-movements")
    public ResponseEntity<WhMovement> createWhMovement(@Valid @RequestBody WhMovement whMovement) throws URISyntaxException {
        log.debug("REST request to save WhMovement : {}", whMovement);
        if (whMovement.getId() != null) {
            throw new BadRequestAlertException("A new whMovement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WhMovement result = whMovementService.save(whMovement);
        return ResponseEntity
            .created(new URI("/api/wh-movements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wh-movements/:id} : Updates an existing whMovement.
     *
     * @param id the id of the whMovement to save.
     * @param whMovement the whMovement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated whMovement,
     * or with status {@code 400 (Bad Request)} if the whMovement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the whMovement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wh-movements/{id}")
    public ResponseEntity<WhMovement> updateWhMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WhMovement whMovement
    ) throws URISyntaxException {
        log.debug("REST request to update WhMovement : {}, {}", id, whMovement);
        if (whMovement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, whMovement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!whMovementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WhMovement result = whMovementService.save(whMovement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, whMovement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wh-movements/:id} : Partial updates given fields of an existing whMovement, field will ignore if it is null
     *
     * @param id the id of the whMovement to save.
     * @param whMovement the whMovement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated whMovement,
     * or with status {@code 400 (Bad Request)} if the whMovement is not valid,
     * or with status {@code 404 (Not Found)} if the whMovement is not found,
     * or with status {@code 500 (Internal Server Error)} if the whMovement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wh-movements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WhMovement> partialUpdateWhMovement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WhMovement whMovement
    ) throws URISyntaxException {
        log.debug("REST request to partial update WhMovement partially : {}, {}", id, whMovement);
        if (whMovement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, whMovement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!whMovementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WhMovement> result = whMovementService.partialUpdate(whMovement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, whMovement.getId().toString())
        );
    }

    /**
     * {@code GET  /wh-movements} : get all the whMovements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of whMovements in body.
     */
    @GetMapping("/wh-movements")
    public ResponseEntity<List<WhMovement>> getAllWhMovements(Pageable pageable) {
        log.debug("REST request to get a page of WhMovements");
        Page<WhMovement> page = whMovementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wh-movements/:id} : get the "id" whMovement.
     *
     * @param id the id of the whMovement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the whMovement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wh-movements/{id}")
    public ResponseEntity<WhMovement> getWhMovement(@PathVariable Long id) {
        log.debug("REST request to get WhMovement : {}", id);
        Optional<WhMovement> whMovement = whMovementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(whMovement);
    }

    /**
     * {@code DELETE  /wh-movements/:id} : delete the "id" whMovement.
     *
     * @param id the id of the whMovement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wh-movements/{id}")
    public ResponseEntity<Void> deleteWhMovement(@PathVariable Long id) {
        log.debug("REST request to delete WhMovement : {}", id);
        whMovementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
