package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.MalariaUnit;
import org.nmcpye.activitiesmanagement.repository.MalariaUnitRepository;
import org.nmcpye.activitiesmanagement.service.MalariaUnitService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.MalariaUnit}.
 */
@RestController
@RequestMapping("/api")
public class MalariaUnitResource {

    private final Logger log = LoggerFactory.getLogger(MalariaUnitResource.class);

    private static final String ENTITY_NAME = "malariaUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MalariaUnitService malariaUnitService;

    private final MalariaUnitRepository malariaUnitRepository;

    public MalariaUnitResource(MalariaUnitService malariaUnitService, MalariaUnitRepository malariaUnitRepository) {
        this.malariaUnitService = malariaUnitService;
        this.malariaUnitRepository = malariaUnitRepository;
    }

    /**
     * {@code POST  /malaria-units} : Create a new malariaUnit.
     *
     * @param malariaUnit the malariaUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new malariaUnit, or with status {@code 400 (Bad Request)} if the malariaUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/malaria-units")
    public ResponseEntity<MalariaUnit> createMalariaUnit(@Valid @RequestBody MalariaUnit malariaUnit) throws URISyntaxException {
        log.debug("REST request to save MalariaUnit : {}", malariaUnit);
        if (malariaUnit.getId() != null) {
            throw new BadRequestAlertException("A new malariaUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MalariaUnit result = malariaUnitService.save(malariaUnit);
        return ResponseEntity
            .created(new URI("/api/malaria-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /malaria-units/:id} : Updates an existing malariaUnit.
     *
     * @param id the id of the malariaUnit to save.
     * @param malariaUnit the malariaUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated malariaUnit,
     * or with status {@code 400 (Bad Request)} if the malariaUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the malariaUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/malaria-units/{id}")
    public ResponseEntity<MalariaUnit> updateMalariaUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MalariaUnit malariaUnit
    ) throws URISyntaxException {
        log.debug("REST request to update MalariaUnit : {}, {}", id, malariaUnit);
        if (malariaUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, malariaUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!malariaUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MalariaUnit result = malariaUnitService.save(malariaUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, malariaUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /malaria-units/:id} : Partial updates given fields of an existing malariaUnit, field will ignore if it is null
     *
     * @param id the id of the malariaUnit to save.
     * @param malariaUnit the malariaUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated malariaUnit,
     * or with status {@code 400 (Bad Request)} if the malariaUnit is not valid,
     * or with status {@code 404 (Not Found)} if the malariaUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the malariaUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/malaria-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MalariaUnit> partialUpdateMalariaUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MalariaUnit malariaUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update MalariaUnit partially : {}, {}", id, malariaUnit);
        if (malariaUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, malariaUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!malariaUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MalariaUnit> result = malariaUnitService.partialUpdate(malariaUnit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, malariaUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /malaria-units} : get all the malariaUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of malariaUnits in body.
     */
    @GetMapping("/malaria-units")
    public List<MalariaUnit> getAllMalariaUnits() {
        log.debug("REST request to get all MalariaUnits");
        return malariaUnitService.findAll();
    }

    /**
     * {@code GET  /malaria-units/:id} : get the "id" malariaUnit.
     *
     * @param id the id of the malariaUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the malariaUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/malaria-units/{id}")
    public ResponseEntity<MalariaUnit> getMalariaUnit(@PathVariable Long id) {
        log.debug("REST request to get MalariaUnit : {}", id);
        Optional<MalariaUnit> malariaUnit = malariaUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(malariaUnit);
    }

    /**
     * {@code DELETE  /malaria-units/:id} : delete the "id" malariaUnit.
     *
     * @param id the id of the malariaUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/malaria-units/{id}")
    public ResponseEntity<Void> deleteMalariaUnit(@PathVariable Long id) {
        log.debug("REST request to delete MalariaUnit : {}", id);
        malariaUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
