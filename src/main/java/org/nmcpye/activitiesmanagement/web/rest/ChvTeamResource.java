package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.nmcpye.activitiesmanagement.repository.ChvTeamRepository;
import org.nmcpye.activitiesmanagement.service.ChvTeamService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.ChvTeam}.
 */
@RestController
@RequestMapping("/api")
public class ChvTeamResource {

    private final Logger log = LoggerFactory.getLogger(ChvTeamResource.class);

    private static final String ENTITY_NAME = "chvTeam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChvTeamService chvTeamService;

    private final ChvTeamRepository chvTeamRepository;

    public ChvTeamResource(ChvTeamService chvTeamService, ChvTeamRepository chvTeamRepository) {
        this.chvTeamService = chvTeamService;
        this.chvTeamRepository = chvTeamRepository;
    }

    /**
     * {@code POST  /chv-teams} : Create a new chvTeam.
     *
     * @param chvTeam the chvTeam to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chvTeam, or with status {@code 400 (Bad Request)} if the chvTeam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chv-teams")
    public ResponseEntity<ChvTeam> createChvTeam(@Valid @RequestBody ChvTeam chvTeam) throws URISyntaxException {
        log.debug("REST request to save ChvTeam : {}", chvTeam);
        if (chvTeam.getId() != null) {
            throw new BadRequestAlertException("A new chvTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChvTeam result = chvTeamService.save(chvTeam);
        return ResponseEntity
            .created(new URI("/api/chv-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chv-teams/:id} : Updates an existing chvTeam.
     *
     * @param id the id of the chvTeam to save.
     * @param chvTeam the chvTeam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chvTeam,
     * or with status {@code 400 (Bad Request)} if the chvTeam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chvTeam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chv-teams/{id}")
    public ResponseEntity<ChvTeam> updateChvTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChvTeam chvTeam
    ) throws URISyntaxException {
        log.debug("REST request to update ChvTeam : {}, {}", id, chvTeam);
        if (chvTeam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chvTeam.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChvTeam result = chvTeamService.save(chvTeam);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chvTeam.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chv-teams/:id} : Partial updates given fields of an existing chvTeam, field will ignore if it is null
     *
     * @param id the id of the chvTeam to save.
     * @param chvTeam the chvTeam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chvTeam,
     * or with status {@code 400 (Bad Request)} if the chvTeam is not valid,
     * or with status {@code 404 (Not Found)} if the chvTeam is not found,
     * or with status {@code 500 (Internal Server Error)} if the chvTeam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chv-teams/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ChvTeam> partialUpdateChvTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChvTeam chvTeam
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChvTeam partially : {}, {}", id, chvTeam);
        if (chvTeam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chvTeam.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChvTeam> result = chvTeamService.partialUpdate(chvTeam);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chvTeam.getId().toString())
        );
    }

    /**
     * {@code GET  /chv-teams} : get all the chvTeams.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chvTeams in body.
     */
    @GetMapping("/chv-teams")
    public ResponseEntity<List<ChvTeam>> getAllChvTeams(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of ChvTeams");
        Page<ChvTeam> page;
        if (eagerload) {
            page = chvTeamService.findAllWithEagerRelationships(pageable);
        } else {
            page = chvTeamService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chv-teams/:id} : get the "id" chvTeam.
     *
     * @param id the id of the chvTeam to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chvTeam, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chv-teams/{id}")
    public ResponseEntity<ChvTeam> getChvTeam(@PathVariable Long id) {
        log.debug("REST request to get ChvTeam : {}", id);
        Optional<ChvTeam> chvTeam = chvTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chvTeam);
    }

    /**
     * {@code DELETE  /chv-teams/:id} : delete the "id" chvTeam.
     *
     * @param id the id of the chvTeam to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chv-teams/{id}")
    public ResponseEntity<Void> deleteChvTeam(@PathVariable Long id) {
        log.debug("REST request to delete ChvTeam : {}", id);
        chvTeamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
