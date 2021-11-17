package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.repository.DataSetRepository;
import org.nmcpye.activitiesmanagement.service.DataSetService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link DataSet}.
 */
@RestController
@RequestMapping("/api")
public class DataSetResource {

    private final Logger log = LoggerFactory.getLogger(DataSetResource.class);

    private static final String ENTITY_NAME = "dataSet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataSetService dataSetService;

    private final DataSetRepository dataSetRepository;

    public DataSetResource(DataSetService dataSetService, DataSetRepository dataSetRepository) {
        this.dataSetService = dataSetService;
        this.dataSetRepository = dataSetRepository;
    }

    /**
     * {@code POST  /data-sets} : Create a new dataSet.
     *
     * @param dataSet the dataSet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataSet, or with status {@code 400 (Bad Request)} if the dataSet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-sets")
    public ResponseEntity<DataSet> createDataSet(@Valid @RequestBody DataSet dataSet) throws URISyntaxException {
        log.debug("REST request to save DataSet : {}", dataSet);
        if (dataSet.getId() != null) {
            throw new BadRequestAlertException("A new dataSet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataSet result = dataSetService.save(dataSet);
        return ResponseEntity
            .created(new URI("/api/data-sets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-sets/:id} : Updates an existing dataSet.
     *
     * @param id the id of the dataSet to save.
     * @param dataSet the dataSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSet,
     * or with status {@code 400 (Bad Request)} if the dataSet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-sets/{id}")
    public ResponseEntity<DataSet> updateDataSet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataSet dataSet
    ) throws URISyntaxException {
        log.debug("REST request to update DataSet : {}, {}", id, dataSet);
        if (dataSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataSet result = dataSetService.save(dataSet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataSet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-sets/:id} : Partial updates given fields of an existing dataSet, field will ignore if it is null
     *
     * @param id the id of the dataSet to save.
     * @param dataSet the dataSet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataSet,
     * or with status {@code 400 (Bad Request)} if the dataSet is not valid,
     * or with status {@code 404 (Not Found)} if the dataSet is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataSet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-sets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DataSet> partialUpdateDataSet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataSet dataSet
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataSet partially : {}, {}", id, dataSet);
        if (dataSet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataSet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataSetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataSet> result = dataSetService.partialUpdate(dataSet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataSet.getId().toString())
        );
    }

    /**
     * {@code GET  /data-sets} : get all the dataSets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataSets in body.
     */
    @GetMapping("/data-sets")
    public List<DataSet> getAllDataSets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DataSets");
        return dataSetService.findAll();
    }

    /**
     * {@code GET  /data-sets/:id} : get the "id" dataSet.
     *
     * @param id the id of the dataSet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataSet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-sets/{id}")
    public ResponseEntity<DataSet> getDataSet(@PathVariable Long id) {
        log.debug("REST request to get DataSet : {}", id);
        Optional<DataSet> dataSet = dataSetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataSet);
    }

    /**
     * {@code DELETE  /data-sets/:id} : delete the "id" dataSet.
     *
     * @param id the id of the dataSet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-sets/{id}")
    public ResponseEntity<Void> deleteDataSet(@PathVariable Long id) {
        log.debug("REST request to delete DataSet : {}", id);
        dataSetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
