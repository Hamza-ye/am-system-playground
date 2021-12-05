package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.repository.ChvRepository;
import org.nmcpye.activitiesmanagement.service.ChvService;
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
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.Chv}.
 */
@RestController
@RequestMapping("/api")
public class ChvResource {

    private final Logger log = LoggerFactory.getLogger(ChvResource.class);

    private static final String ENTITY_NAME = "chv";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChvService chvService;

    private final ChvRepository chvRepository;

    public ChvResource(ChvService chvService, ChvRepository chvRepository) {
        this.chvService = chvService;
        this.chvRepository = chvRepository;
    }

    /**
     * {@code POST  /chvs} : Create a new chv.
     *
     * @param chv the chv to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chv, or with status {@code 400 (Bad Request)} if the chv has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chvs")
    public ResponseEntity<Chv> createChv(@Valid @RequestBody Chv chv) throws URISyntaxException {
        log.debug("REST request to save Chv : {}", chv);
        if (chv.getId() != null) {
            throw new BadRequestAlertException("A new chv cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Chv result = chvService.save(chv);
        return ResponseEntity
            .created(new URI("/api/chvs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chvs/:id} : Updates an existing chv.
     *
     * @param id the id of the chv to save.
     * @param chv the chv to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chv,
     * or with status {@code 400 (Bad Request)} if the chv is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chv couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chvs/{id}")
    public ResponseEntity<Chv> updateChv(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Chv chv)
        throws URISyntaxException {
        log.debug("REST request to update Chv : {}, {}", id, chv);
        if (chv.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chv.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Chv result = chvService.save(chv);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chv.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chvs/:id} : Partial updates given fields of an existing chv, field will ignore if it is null
     *
     * @param id the id of the chv to save.
     * @param chv the chv to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chv,
     * or with status {@code 400 (Bad Request)} if the chv is not valid,
     * or with status {@code 404 (Not Found)} if the chv is not found,
     * or with status {@code 500 (Internal Server Error)} if the chv couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chvs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Chv> partialUpdateChv(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Chv chv)
        throws URISyntaxException {
        log.debug("REST request to partial update Chv partially : {}, {}", id, chv);
        if (chv.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chv.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chvRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Chv> result = chvService.partialUpdate(chv);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chv.getId().toString())
        );
    }

    /**
     * {@code GET  /chvs} : get all the chvs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chvs in body.
     */
    @GetMapping("/chvs")
    public ResponseEntity<List<Chv>> getAllChvs(Pageable pageable) {
        log.debug("REST request to get a page of Chvs");
        Page<Chv> page = chvService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chvs/:id} : get the "id" chv.
     *
     * @param id the id of the chv to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chv, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chvs/{id}")
    public ResponseEntity<Chv> getChv(@PathVariable Long id) {
        log.debug("REST request to get Chv : {}", id);
        Optional<Chv> chv = chvService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chv);
    }

    /**
     * {@code DELETE  /chvs/:id} : delete the "id" chv.
     *
     * @param id the id of the chv to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chvs/{id}")
    public ResponseEntity<Void> deleteChv(@PathVariable Long id) {
        log.debug("REST request to delete Chv : {}", id);
        chvService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
