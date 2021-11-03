package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.repository.PeopleGroupRepository;
import org.nmcpye.activitiesmanagement.service.PeopleGroupService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link PeopleGroup}.
 */
@RestController
@RequestMapping("/api")
public class PeopleGroupResource {

    private final Logger log = LoggerFactory.getLogger(PeopleGroupResource.class);

    private static final String ENTITY_NAME = "peopleGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeopleGroupService peopleGroupService;

    private final PeopleGroupRepository peopleGroupRepository;

    public PeopleGroupResource(PeopleGroupService peopleGroupService, PeopleGroupRepository peopleGroupRepository) {
        this.peopleGroupService = peopleGroupService;
        this.peopleGroupRepository = peopleGroupRepository;
    }

    /**
     * {@code POST  /people-groups} : Create a new peopleGroup.
     *
     * @param peopleGroup the peopleGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peopleGroup, or with status {@code 400 (Bad Request)} if the peopleGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people-groups")
    public ResponseEntity<PeopleGroup> createPeopleGroup(@Valid @RequestBody PeopleGroup peopleGroup) throws URISyntaxException {
        log.debug("REST request to save PeopleGroup : {}", peopleGroup);
        if (peopleGroup.getId() != null) {
            throw new BadRequestAlertException("A new peopleGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeopleGroup result = peopleGroupService.save(peopleGroup);
        return ResponseEntity
            .created(new URI("/api/people-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people-groups/:id} : Updates an existing peopleGroup.
     *
     * @param id the id of the peopleGroup to save.
     * @param peopleGroup the peopleGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peopleGroup,
     * or with status {@code 400 (Bad Request)} if the peopleGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peopleGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people-groups/{id}")
    public ResponseEntity<PeopleGroup> updatePeopleGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeopleGroup peopleGroup
    ) throws URISyntaxException {
        log.debug("REST request to update PeopleGroup : {}, {}", id, peopleGroup);
        if (peopleGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peopleGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peopleGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PeopleGroup result = peopleGroupService.save(peopleGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peopleGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /people-groups/:id} : Partial updates given fields of an existing peopleGroup, field will ignore if it is null
     *
     * @param id the id of the peopleGroup to save.
     * @param peopleGroup the peopleGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peopleGroup,
     * or with status {@code 400 (Bad Request)} if the peopleGroup is not valid,
     * or with status {@code 404 (Not Found)} if the peopleGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the peopleGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/people-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PeopleGroup> partialUpdatePeopleGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeopleGroup peopleGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update PeopleGroup partially : {}, {}", id, peopleGroup);
        if (peopleGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peopleGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peopleGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeopleGroup> result = peopleGroupService.partialUpdate(peopleGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peopleGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /people-groups} : get all the peopleGroups.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peopleGroups in body.
     */
    @GetMapping("/people-groups")
    public List<PeopleGroup> getAllPeopleGroups(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all PeopleGroups");
        return peopleGroupService.findAll();
    }

    /**
     * {@code GET  /people-groups/:id} : get the "id" peopleGroup.
     *
     * @param id the id of the peopleGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peopleGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people-groups/{id}")
    public ResponseEntity<PeopleGroup> getPeopleGroup(@PathVariable Long id) {
        log.debug("REST request to get PeopleGroup : {}", id);
        Optional<PeopleGroup> peopleGroup = peopleGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peopleGroup);
    }

    /**
     * {@code DELETE  /people-groups/:id} : delete the "id" peopleGroup.
     *
     * @param id the id of the peopleGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people-groups/{id}")
    public ResponseEntity<Void> deletePeopleGroup(@PathVariable Long id) {
        log.debug("REST request to delete PeopleGroup : {}", id);
        peopleGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
