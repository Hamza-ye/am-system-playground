package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.MalariaUnitStaffMember;
import org.nmcpye.activitiesmanagement.repository.MalariaUnitStaffMemberRepository;
import org.nmcpye.activitiesmanagement.service.MalariaUnitStaffMemberService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.MalariaUnitStaffMember}.
 */
@RestController
@RequestMapping("/api")
public class MalariaUnitStaffMemberResource {

    private final Logger log = LoggerFactory.getLogger(MalariaUnitStaffMemberResource.class);

    private static final String ENTITY_NAME = "malariaUnitStaffMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MalariaUnitStaffMemberService malariaUnitStaffMemberService;

    private final MalariaUnitStaffMemberRepository malariaUnitStaffMemberRepository;

    public MalariaUnitStaffMemberResource(
        MalariaUnitStaffMemberService malariaUnitStaffMemberService,
        MalariaUnitStaffMemberRepository malariaUnitStaffMemberRepository
    ) {
        this.malariaUnitStaffMemberService = malariaUnitStaffMemberService;
        this.malariaUnitStaffMemberRepository = malariaUnitStaffMemberRepository;
    }

    /**
     * {@code POST  /malaria-unit-staff-members} : Create a new malariaUnitStaffMember.
     *
     * @param malariaUnitStaffMember the malariaUnitStaffMember to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new malariaUnitStaffMember, or with status {@code 400 (Bad Request)} if the malariaUnitStaffMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/malaria-unit-staff-members")
    public ResponseEntity<MalariaUnitStaffMember> createMalariaUnitStaffMember(
        @Valid @RequestBody MalariaUnitStaffMember malariaUnitStaffMember
    ) throws URISyntaxException {
        log.debug("REST request to save MalariaUnitStaffMember : {}", malariaUnitStaffMember);
        if (malariaUnitStaffMember.getId() != null) {
            throw new BadRequestAlertException("A new malariaUnitStaffMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MalariaUnitStaffMember result = malariaUnitStaffMemberService.save(malariaUnitStaffMember);
        return ResponseEntity
            .created(new URI("/api/malaria-unit-staff-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /malaria-unit-staff-members/:id} : Updates an existing malariaUnitStaffMember.
     *
     * @param id the id of the malariaUnitStaffMember to save.
     * @param malariaUnitStaffMember the malariaUnitStaffMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated malariaUnitStaffMember,
     * or with status {@code 400 (Bad Request)} if the malariaUnitStaffMember is not valid,
     * or with status {@code 500 (Internal Server Error)} if the malariaUnitStaffMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/malaria-unit-staff-members/{id}")
    public ResponseEntity<MalariaUnitStaffMember> updateMalariaUnitStaffMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MalariaUnitStaffMember malariaUnitStaffMember
    ) throws URISyntaxException {
        log.debug("REST request to update MalariaUnitStaffMember : {}, {}", id, malariaUnitStaffMember);
        if (malariaUnitStaffMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, malariaUnitStaffMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!malariaUnitStaffMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MalariaUnitStaffMember result = malariaUnitStaffMemberService.save(malariaUnitStaffMember);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, malariaUnitStaffMember.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /malaria-unit-staff-members/:id} : Partial updates given fields of an existing malariaUnitStaffMember, field will ignore if it is null
     *
     * @param id the id of the malariaUnitStaffMember to save.
     * @param malariaUnitStaffMember the malariaUnitStaffMember to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated malariaUnitStaffMember,
     * or with status {@code 400 (Bad Request)} if the malariaUnitStaffMember is not valid,
     * or with status {@code 404 (Not Found)} if the malariaUnitStaffMember is not found,
     * or with status {@code 500 (Internal Server Error)} if the malariaUnitStaffMember couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/malaria-unit-staff-members/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MalariaUnitStaffMember> partialUpdateMalariaUnitStaffMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MalariaUnitStaffMember malariaUnitStaffMember
    ) throws URISyntaxException {
        log.debug("REST request to partial update MalariaUnitStaffMember partially : {}, {}", id, malariaUnitStaffMember);
        if (malariaUnitStaffMember.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, malariaUnitStaffMember.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!malariaUnitStaffMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MalariaUnitStaffMember> result = malariaUnitStaffMemberService.partialUpdate(malariaUnitStaffMember);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, malariaUnitStaffMember.getId().toString())
        );
    }

    /**
     * {@code GET  /malaria-unit-staff-members} : get all the malariaUnitStaffMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of malariaUnitStaffMembers in body.
     */
    @GetMapping("/malaria-unit-staff-members")
    public List<MalariaUnitStaffMember> getAllMalariaUnitStaffMembers() {
        log.debug("REST request to get all MalariaUnitStaffMembers");
        return malariaUnitStaffMemberService.findAll();
    }

    /**
     * {@code GET  /malaria-unit-staff-members/:id} : get the "id" malariaUnitStaffMember.
     *
     * @param id the id of the malariaUnitStaffMember to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the malariaUnitStaffMember, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/malaria-unit-staff-members/{id}")
    public ResponseEntity<MalariaUnitStaffMember> getMalariaUnitStaffMember(@PathVariable Long id) {
        log.debug("REST request to get MalariaUnitStaffMember : {}", id);
        Optional<MalariaUnitStaffMember> malariaUnitStaffMember = malariaUnitStaffMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(malariaUnitStaffMember);
    }

    /**
     * {@code DELETE  /malaria-unit-staff-members/:id} : delete the "id" malariaUnitStaffMember.
     *
     * @param id the id of the malariaUnitStaffMember to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/malaria-unit-staff-members/{id}")
    public ResponseEntity<Void> deleteMalariaUnitStaffMember(@PathVariable Long id) {
        log.debug("REST request to delete MalariaUnitStaffMember : {}", id);
        malariaUnitStaffMemberService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
