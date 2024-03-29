package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.repository.DemographicDataRepository;
import org.nmcpye.activitiesmanagement.service.DemographicDataService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link DemographicData}.
 */
@RestController
@RequestMapping("/api")
public class DemographicDataResource {

    private final Logger log = LoggerFactory.getLogger(DemographicDataResource.class);

    private static final String ENTITY_NAME = "demographicData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemographicDataService demographicDataService;

    private final DemographicDataRepository demographicDataRepository;

    public DemographicDataResource(DemographicDataService demographicDataService, DemographicDataRepository demographicDataRepository) {
        this.demographicDataService = demographicDataService;
        this.demographicDataRepository = demographicDataRepository;
    }

    /**
     * {@code POST  /demographic-data} : Create a new demographicData.
     *
     * @param demographicData the demographicData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demographicData, or with status {@code 400 (Bad Request)} if the demographicData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demographic-data")
    public ResponseEntity<DemographicData> createDemographicData(@Valid @RequestBody DemographicData demographicData)
        throws URISyntaxException {
        log.debug("REST request to save DemographicData : {}", demographicData);
        if (demographicData.getId() != null) {
            throw new BadRequestAlertException("A new demographicData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemographicData result = demographicDataService.save(demographicData);
        return ResponseEntity
            .created(new URI("/api/demographic-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demographic-data/:id} : Updates an existing demographicData.
     *
     * @param id the id of the demographicData to save.
     * @param demographicData the demographicData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demographicData,
     * or with status {@code 400 (Bad Request)} if the demographicData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demographicData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demographic-data/{id}")
    public ResponseEntity<DemographicData> updateDemographicData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemographicData demographicData
    ) throws URISyntaxException {
        log.debug("REST request to update DemographicData : {}, {}", id, demographicData);
        if (demographicData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demographicData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demographicDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemographicData result = demographicDataService.save(demographicData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demographicData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demographic-data/:id} : Partial updates given fields of an existing demographicData, field will ignore if it is null
     *
     * @param id the id of the demographicData to save.
     * @param demographicData the demographicData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demographicData,
     * or with status {@code 400 (Bad Request)} if the demographicData is not valid,
     * or with status {@code 404 (Not Found)} if the demographicData is not found,
     * or with status {@code 500 (Internal Server Error)} if the demographicData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demographic-data/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DemographicData> partialUpdateDemographicData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemographicData demographicData
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemographicData partially : {}, {}", id, demographicData);
        if (demographicData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demographicData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demographicDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemographicData> result = demographicDataService.partialUpdate(demographicData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demographicData.getId().toString())
        );
    }

    /**
     * {@code GET  /demographic-data} : get all the demographicData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demographicData in body.
     */
    @GetMapping("/demographic-data")
    public List<DemographicData> getAllDemographicData() {
        log.debug("REST request to get all DemographicData");
        return demographicDataService.findAll();
    }

    /**
     * {@code GET  /demographic-data/:id} : get the "id" demographicData.
     *
     * @param id the id of the demographicData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demographicData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demographic-data/{id}")
    public ResponseEntity<DemographicData> getDemographicData(@PathVariable Long id) {
        log.debug("REST request to get DemographicData : {}", id);
        Optional<DemographicData> demographicData = demographicDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demographicData);
    }

    /**
     * {@code DELETE  /demographic-data/:id} : delete the "id" demographicData.
     *
     * @param id the id of the demographicData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demographic-data/{id}")
    public ResponseEntity<Void> deleteDemographicData(@PathVariable Long id) {
        log.debug("REST request to delete DemographicData : {}", id);
        demographicDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
