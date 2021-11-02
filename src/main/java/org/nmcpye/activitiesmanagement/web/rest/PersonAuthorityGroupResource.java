package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.repository.PersonAuthorityGroupRepository;
import org.nmcpye.activitiesmanagement.service.PersonAuthorityGroupService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.PersonAuthorityGroup}.
 */
@RestController
@RequestMapping("/api")
public class PersonAuthorityGroupResource {

    private final Logger log = LoggerFactory.getLogger(PersonAuthorityGroupResource.class);

    private static final String ENTITY_NAME = "personAuthorityGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonAuthorityGroupService personAuthorityGroupService;

    private final PersonAuthorityGroupRepository personAuthorityGroupRepository;

    public PersonAuthorityGroupResource(
        PersonAuthorityGroupService personAuthorityGroupService,
        PersonAuthorityGroupRepository personAuthorityGroupRepository
    ) {
        this.personAuthorityGroupService = personAuthorityGroupService;
        this.personAuthorityGroupRepository = personAuthorityGroupRepository;
    }

    /**
     * {@code POST  /person-authority-groups} : Create a new personAuthorityGroup.
     *
     * @param personAuthorityGroup the personAuthorityGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personAuthorityGroup, or with status {@code 400 (Bad Request)} if the personAuthorityGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-authority-groups")
    public ResponseEntity<PersonAuthorityGroup> createPersonAuthorityGroup(@Valid @RequestBody PersonAuthorityGroup personAuthorityGroup)
        throws URISyntaxException {
        log.debug("REST request to save PersonAuthorityGroup : {}", personAuthorityGroup);
        if (personAuthorityGroup.getId() != null) {
            throw new BadRequestAlertException("A new personAuthorityGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonAuthorityGroup result = personAuthorityGroupService.save(personAuthorityGroup);
        return ResponseEntity
            .created(new URI("/api/person-authority-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-authority-groups/:id} : Updates an existing personAuthorityGroup.
     *
     * @param id the id of the personAuthorityGroup to save.
     * @param personAuthorityGroup the personAuthorityGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personAuthorityGroup,
     * or with status {@code 400 (Bad Request)} if the personAuthorityGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personAuthorityGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-authority-groups/{id}")
    public ResponseEntity<PersonAuthorityGroup> updatePersonAuthorityGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonAuthorityGroup personAuthorityGroup
    ) throws URISyntaxException {
        log.debug("REST request to update PersonAuthorityGroup : {}, {}", id, personAuthorityGroup);
        if (personAuthorityGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personAuthorityGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personAuthorityGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonAuthorityGroup result = personAuthorityGroupService.save(personAuthorityGroup);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personAuthorityGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-authority-groups/:id} : Partial updates given fields of an existing personAuthorityGroup, field will ignore if it is null
     *
     * @param id the id of the personAuthorityGroup to save.
     * @param personAuthorityGroup the personAuthorityGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personAuthorityGroup,
     * or with status {@code 400 (Bad Request)} if the personAuthorityGroup is not valid,
     * or with status {@code 404 (Not Found)} if the personAuthorityGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the personAuthorityGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-authority-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonAuthorityGroup> partialUpdatePersonAuthorityGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonAuthorityGroup personAuthorityGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonAuthorityGroup partially : {}, {}", id, personAuthorityGroup);
        if (personAuthorityGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personAuthorityGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personAuthorityGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonAuthorityGroup> result = personAuthorityGroupService.partialUpdate(personAuthorityGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personAuthorityGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /person-authority-groups} : get all the personAuthorityGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personAuthorityGroups in body.
     */
    @GetMapping("/person-authority-groups")
    public List<PersonAuthorityGroup> getAllPersonAuthorityGroups() {
        log.debug("REST request to get all PersonAuthorityGroups");
        return personAuthorityGroupService.findAll();
    }

    /**
     * {@code GET  /person-authority-groups/:id} : get the "id" personAuthorityGroup.
     *
     * @param id the id of the personAuthorityGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personAuthorityGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-authority-groups/{id}")
    public ResponseEntity<PersonAuthorityGroup> getPersonAuthorityGroup(@PathVariable Long id) {
        log.debug("REST request to get PersonAuthorityGroup : {}", id);
        Optional<PersonAuthorityGroup> personAuthorityGroup = personAuthorityGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personAuthorityGroup);
    }

    /**
     * {@code DELETE  /person-authority-groups/:id} : delete the "id" personAuthorityGroup.
     *
     * @param id the id of the personAuthorityGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-authority-groups/{id}")
    public ResponseEntity<Void> deletePersonAuthorityGroup(@PathVariable Long id) {
        log.debug("REST request to delete PersonAuthorityGroup : {}", id);
        personAuthorityGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
