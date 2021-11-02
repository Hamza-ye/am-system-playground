package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.CHVTeam;
import org.nmcpye.activitiesmanagement.repository.CHVTeamRepository;
import org.nmcpye.activitiesmanagement.service.CHVTeamService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.CHVTeam}.
 */
@RestController
@RequestMapping("/api")
public class CHVTeamResource {

    private final Logger log = LoggerFactory.getLogger(CHVTeamResource.class);

    private static final String ENTITY_NAME = "cHVTeam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CHVTeamService cHVTeamService;

    private final CHVTeamRepository cHVTeamRepository;

    public CHVTeamResource(CHVTeamService cHVTeamService, CHVTeamRepository cHVTeamRepository) {
        this.cHVTeamService = cHVTeamService;
        this.cHVTeamRepository = cHVTeamRepository;
    }

    /**
     * {@code POST  /chv-teams} : Create a new cHVTeam.
     *
     * @param cHVTeam the cHVTeam to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cHVTeam, or with status {@code 400 (Bad Request)} if the cHVTeam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chv-teams")
    public ResponseEntity<CHVTeam> createCHVTeam(@Valid @RequestBody CHVTeam cHVTeam) throws URISyntaxException {
        log.debug("REST request to save CHVTeam : {}", cHVTeam);
        if (cHVTeam.getId() != null) {
            throw new BadRequestAlertException("A new cHVTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CHVTeam result = cHVTeamService.save(cHVTeam);
        return ResponseEntity
            .created(new URI("/api/chv-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chv-teams/:id} : Updates an existing cHVTeam.
     *
     * @param id the id of the cHVTeam to save.
     * @param cHVTeam the cHVTeam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHVTeam,
     * or with status {@code 400 (Bad Request)} if the cHVTeam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cHVTeam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chv-teams/{id}")
    public ResponseEntity<CHVTeam> updateCHVTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CHVTeam cHVTeam
    ) throws URISyntaxException {
        log.debug("REST request to update CHVTeam : {}, {}", id, cHVTeam);
        if (cHVTeam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHVTeam.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CHVTeam result = cHVTeamService.save(cHVTeam);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHVTeam.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chv-teams/:id} : Partial updates given fields of an existing cHVTeam, field will ignore if it is null
     *
     * @param id the id of the cHVTeam to save.
     * @param cHVTeam the cHVTeam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHVTeam,
     * or with status {@code 400 (Bad Request)} if the cHVTeam is not valid,
     * or with status {@code 404 (Not Found)} if the cHVTeam is not found,
     * or with status {@code 500 (Internal Server Error)} if the cHVTeam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chv-teams/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CHVTeam> partialUpdateCHVTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CHVTeam cHVTeam
    ) throws URISyntaxException {
        log.debug("REST request to partial update CHVTeam partially : {}, {}", id, cHVTeam);
        if (cHVTeam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHVTeam.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CHVTeam> result = cHVTeamService.partialUpdate(cHVTeam);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHVTeam.getId().toString())
        );
    }

    /**
     * {@code GET  /chv-teams} : get all the cHVTeams.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cHVTeams in body.
     */
    @GetMapping("/chv-teams")
    public List<CHVTeam> getAllCHVTeams(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CHVTeams");
        return cHVTeamService.findAll();
    }

    /**
     * {@code GET  /chv-teams/:id} : get the "id" cHVTeam.
     *
     * @param id the id of the cHVTeam to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cHVTeam, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chv-teams/{id}")
    public ResponseEntity<CHVTeam> getCHVTeam(@PathVariable Long id) {
        log.debug("REST request to get CHVTeam : {}", id);
        Optional<CHVTeam> cHVTeam = cHVTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cHVTeam);
    }

    /**
     * {@code DELETE  /chv-teams/:id} : delete the "id" cHVTeam.
     *
     * @param id the id of the cHVTeam to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chv-teams/{id}")
    public ResponseEntity<Void> deleteCHVTeam(@PathVariable Long id) {
        log.debug("REST request to delete CHVTeam : {}", id);
        cHVTeamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
