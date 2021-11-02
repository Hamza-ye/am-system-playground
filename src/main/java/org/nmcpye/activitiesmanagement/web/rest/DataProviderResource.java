package org.nmcpye.activitiesmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.nmcpye.activitiesmanagement.domain.DataProvider;
import org.nmcpye.activitiesmanagement.repository.DataProviderRepository;
import org.nmcpye.activitiesmanagement.service.DataProviderService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.DataProvider}.
 */
@RestController
@RequestMapping("/api")
public class DataProviderResource {

    private final Logger log = LoggerFactory.getLogger(DataProviderResource.class);

    private static final String ENTITY_NAME = "dataProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataProviderService dataProviderService;

    private final DataProviderRepository dataProviderRepository;

    public DataProviderResource(DataProviderService dataProviderService, DataProviderRepository dataProviderRepository) {
        this.dataProviderService = dataProviderService;
        this.dataProviderRepository = dataProviderRepository;
    }

    /**
     * {@code POST  /data-providers} : Create a new dataProvider.
     *
     * @param dataProvider the dataProvider to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataProvider, or with status {@code 400 (Bad Request)} if the dataProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-providers")
    public ResponseEntity<DataProvider> createDataProvider(@Valid @RequestBody DataProvider dataProvider) throws URISyntaxException {
        log.debug("REST request to save DataProvider : {}", dataProvider);
        if (dataProvider.getId() != null) {
            throw new BadRequestAlertException("A new dataProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataProvider result = dataProviderService.save(dataProvider);
        return ResponseEntity
            .created(new URI("/api/data-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-providers/:id} : Updates an existing dataProvider.
     *
     * @param id the id of the dataProvider to save.
     * @param dataProvider the dataProvider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataProvider,
     * or with status {@code 400 (Bad Request)} if the dataProvider is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataProvider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-providers/{id}")
    public ResponseEntity<DataProvider> updateDataProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataProvider dataProvider
    ) throws URISyntaxException {
        log.debug("REST request to update DataProvider : {}, {}", id, dataProvider);
        if (dataProvider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataProvider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DataProvider result = dataProviderService.save(dataProvider);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataProvider.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /data-providers/:id} : Partial updates given fields of an existing dataProvider, field will ignore if it is null
     *
     * @param id the id of the dataProvider to save.
     * @param dataProvider the dataProvider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataProvider,
     * or with status {@code 400 (Bad Request)} if the dataProvider is not valid,
     * or with status {@code 404 (Not Found)} if the dataProvider is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataProvider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/data-providers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DataProvider> partialUpdateDataProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataProvider dataProvider
    ) throws URISyntaxException {
        log.debug("REST request to partial update DataProvider partially : {}, {}", id, dataProvider);
        if (dataProvider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataProvider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataProviderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataProvider> result = dataProviderService.partialUpdate(dataProvider);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataProvider.getId().toString())
        );
    }

    /**
     * {@code GET  /data-providers} : get all the dataProviders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataProviders in body.
     */
    @GetMapping("/data-providers")
    public List<DataProvider> getAllDataProviders() {
        log.debug("REST request to get all DataProviders");
        return dataProviderService.findAll();
    }

    /**
     * {@code GET  /data-providers/:id} : get the "id" dataProvider.
     *
     * @param id the id of the dataProvider to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataProvider, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-providers/{id}")
    public ResponseEntity<DataProvider> getDataProvider(@PathVariable Long id) {
        log.debug("REST request to get DataProvider : {}", id);
        Optional<DataProvider> dataProvider = dataProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataProvider);
    }

    /**
     * {@code DELETE  /data-providers/:id} : delete the "id" dataProvider.
     *
     * @param id the id of the dataProvider to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-providers/{id}")
    public ResponseEntity<Void> deleteDataProvider(@PathVariable Long id) {
        log.debug("REST request to delete DataProvider : {}", id);
        dataProviderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
