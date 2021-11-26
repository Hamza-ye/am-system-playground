package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.repository.ChvRepository;
import org.nmcpye.activitiesmanagement.service.ChvService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Chv}.
 */
@RestController
@RequestMapping("/api")
public class ChvResource {

    private final Logger log = LoggerFactory.getLogger(ChvResource.class);

    private static final String ENTITY_NAME = "cHV";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChvService cHVService;

    private final ChvRepository cHVRepository;

    public ChvResource(ChvService cHVService, ChvRepository cHVRepository) {
        this.cHVService = cHVService;
        this.cHVRepository = cHVRepository;
    }

    /**
     * {@code POST  /chvs} : Create a new cHV.
     *
     * @param cHV the cHV to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cHV, or with status {@code 400 (Bad Request)} if the cHV has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chvs")
    public ResponseEntity<Chv> createCHV(@Valid @RequestBody Chv cHV) throws URISyntaxException {
        log.debug("REST request to save Chv : {}", cHV);
        if (cHV.getId() != null) {
            throw new BadRequestAlertException("A new cHV cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Chv result = cHVService.save(cHV);
        return ResponseEntity
            .created(new URI("/api/chvs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chvs/:id} : Updates an existing cHV.
     *
     * @param id the id of the cHV to save.
     * @param cHV the cHV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHV,
     * or with status {@code 400 (Bad Request)} if the cHV is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cHV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chvs/{id}")
    public ResponseEntity<Chv> updateCHV(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Chv cHV)
        throws URISyntaxException {
        log.debug("REST request to update Chv : {}, {}", id, cHV);
        if (cHV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Chv result = cHVService.save(cHV);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHV.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chvs/:id} : Partial updates given fields of an existing cHV, field will ignore if it is null
     *
     * @param id the id of the cHV to save.
     * @param cHV the cHV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cHV,
     * or with status {@code 400 (Bad Request)} if the cHV is not valid,
     * or with status {@code 404 (Not Found)} if the cHV is not found,
     * or with status {@code 500 (Internal Server Error)} if the cHV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chvs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Chv> partialUpdateCHV(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Chv cHV)
        throws URISyntaxException {
        log.debug("REST request to partial update Chv partially : {}, {}", id, cHV);
        if (cHV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cHV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cHVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Chv> result = cHVService.partialUpdate(cHV);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cHV.getId().toString())
        );
    }

    /**
     * {@code GET  /chvs} : get all the cHVS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cHVS in body.
     */
    @GetMapping("/chvs")
    public List<Chv> getAllCHVS() {
        log.debug("REST request to get all CHVS");
        return cHVService.findAll();
    }

    /**
     * {@code GET  /chvs/:id} : get the "id" cHV.
     *
     * @param id the id of the cHV to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cHV, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chvs/{id}")
    public ResponseEntity<Chv> getCHV(@PathVariable Long id) {
        log.debug("REST request to get Chv : {}", id);
        Optional<Chv> cHV = cHVService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cHV);
    }

    /**
     * {@code DELETE  /chvs/:id} : delete the "id" cHV.
     *
     * @param id the id of the cHV to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chvs/{id}")
    public ResponseEntity<Void> deleteCHV(@PathVariable Long id) {
        log.debug("REST request to delete Chv : {}", id);
        cHVService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
