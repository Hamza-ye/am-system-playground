package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnit;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitRepository;
import org.nmcpye.activitiesmanagement.service.OrganisationUnitService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.OrganisationUnit}.
 */
@RestController
@RequestMapping("/api")
public class OrganisationUnitResource {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitResource.class);

    private static final String ENTITY_NAME = "organisationUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganisationUnitService organisationUnitService;

    private final OrganisationUnitRepository organisationUnitRepository;

    public OrganisationUnitResource(
        OrganisationUnitService organisationUnitService,
        OrganisationUnitRepository organisationUnitRepository
    ) {
        this.organisationUnitService = organisationUnitService;
        this.organisationUnitRepository = organisationUnitRepository;
    }

    /**
     * {@code POST  /organisation-units} : Create a new organisationUnit.
     *
     * @param organisationUnit the organisationUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organisationUnit, or with status {@code 400 (Bad Request)} if the organisationUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organisation-units")
    public ResponseEntity<OrganisationUnit> createOrganisationUnit(@Valid @RequestBody OrganisationUnit organisationUnit)
        throws URISyntaxException {
        log.debug("REST request to save OrganisationUnit : {}", organisationUnit);
        if (organisationUnit.getId() != null) {
            throw new BadRequestAlertException("A new organisationUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganisationUnit result = organisationUnitService.save(organisationUnit);
        return ResponseEntity
            .created(new URI("/api/organisation-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organisation-units/:id} : Updates an existing organisationUnit.
     *
     * @param id the id of the organisationUnit to save.
     * @param organisationUnit the organisationUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organisationUnit,
     * or with status {@code 400 (Bad Request)} if the organisationUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organisationUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organisation-units/{id}")
    public ResponseEntity<OrganisationUnit> updateOrganisationUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrganisationUnit organisationUnit
    ) throws URISyntaxException {
        log.debug("REST request to update OrganisationUnit : {}, {}", id, organisationUnit);
        if (organisationUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organisationUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organisationUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganisationUnit result = organisationUnitService.save(organisationUnit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organisationUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /organisation-units/:id} : Partial updates given fields of an existing organisationUnit, field will ignore if it is null
     *
     * @param id the id of the organisationUnit to save.
     * @param organisationUnit the organisationUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organisationUnit,
     * or with status {@code 400 (Bad Request)} if the organisationUnit is not valid,
     * or with status {@code 404 (Not Found)} if the organisationUnit is not found,
     * or with status {@code 500 (Internal Server Error)} if the organisationUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organisation-units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrganisationUnit> partialUpdateOrganisationUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrganisationUnit organisationUnit
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrganisationUnit partially : {}, {}", id, organisationUnit);
        if (organisationUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organisationUnit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organisationUnitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganisationUnit> result = organisationUnitService.partialUpdate(organisationUnit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organisationUnit.getId().toString())
        );
    }

    /**
     * {@code GET  /organisation-units} : get all the organisationUnits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organisationUnits in body.
     */
    @GetMapping("/organisation-units")
    public List<OrganisationUnit> getAllOrganisationUnits() {
        log.debug("REST request to get all OrganisationUnits");
        return organisationUnitService.findAll();
    }

    /**
     * {@code GET  /organisation-units/:id} : get the "id" organisationUnit.
     *
     * @param id the id of the organisationUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organisationUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organisation-units/{id}")
    public ResponseEntity<OrganisationUnit> getOrganisationUnit(@PathVariable Long id) {
        log.debug("REST request to get OrganisationUnit : {}", id);
        Optional<OrganisationUnit> organisationUnit = organisationUnitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organisationUnit);
    }

    /**
     * {@code DELETE  /organisation-units/:id} : delete the "id" organisationUnit.
     *
     * @param id the id of the organisationUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organisation-units/{id}")
    public ResponseEntity<Void> deleteOrganisationUnit(@PathVariable Long id) {
        log.debug("REST request to delete OrganisationUnit : {}", id);
        organisationUnitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
