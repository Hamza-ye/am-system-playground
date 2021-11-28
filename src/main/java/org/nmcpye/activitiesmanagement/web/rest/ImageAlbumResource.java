package org.nmcpye.activitiesmanagement.web.rest;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.nmcpye.activitiesmanagement.repository.ImageAlbumRepository;
import org.nmcpye.activitiesmanagement.service.ImageAlbumService;
import org.nmcpye.activitiesmanagement.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link org.nmcpye.activitiesmanagement.domain.ImageAlbum}.
 */
@RestController
@RequestMapping("/api")
public class ImageAlbumResource {

    private final Logger log = LoggerFactory.getLogger(ImageAlbumResource.class);

    private static final String ENTITY_NAME = "imageAlbum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageAlbumService imageAlbumService;

    private final ImageAlbumRepository imageAlbumRepository;

    public ImageAlbumResource(ImageAlbumService imageAlbumService, ImageAlbumRepository imageAlbumRepository) {
        this.imageAlbumService = imageAlbumService;
        this.imageAlbumRepository = imageAlbumRepository;
    }

    /**
     * {@code POST  /image-albums} : Create a new imageAlbum.
     *
     * @param imageAlbum the imageAlbum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageAlbum, or with status {@code 400 (Bad Request)} if the imageAlbum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/image-albums")
    public ResponseEntity<ImageAlbum> createImageAlbum(@Valid @RequestBody ImageAlbum imageAlbum) throws URISyntaxException {
        log.debug("REST request to save ImageAlbum : {}", imageAlbum);
        if (imageAlbum.getId() != null) {
            throw new BadRequestAlertException("A new imageAlbum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImageAlbum result = imageAlbumService.save(imageAlbum);
        return ResponseEntity
            .created(new URI("/api/image-albums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /image-albums/:id} : Updates an existing imageAlbum.
     *
     * @param id the id of the imageAlbum to save.
     * @param imageAlbum the imageAlbum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageAlbum,
     * or with status {@code 400 (Bad Request)} if the imageAlbum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageAlbum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-albums/{id}")
    public ResponseEntity<ImageAlbum> updateImageAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageAlbum imageAlbum
    ) throws URISyntaxException {
        log.debug("REST request to update ImageAlbum : {}, {}", id, imageAlbum);
        if (imageAlbum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageAlbum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageAlbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageAlbum result = imageAlbumService.save(imageAlbum);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageAlbum.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /image-albums/:id} : Partial updates given fields of an existing imageAlbum, field will ignore if it is null
     *
     * @param id the id of the imageAlbum to save.
     * @param imageAlbum the imageAlbum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageAlbum,
     * or with status {@code 400 (Bad Request)} if the imageAlbum is not valid,
     * or with status {@code 404 (Not Found)} if the imageAlbum is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageAlbum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-albums/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ImageAlbum> partialUpdateImageAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageAlbum imageAlbum
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImageAlbum partially : {}, {}", id, imageAlbum);
        if (imageAlbum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageAlbum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageAlbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageAlbum> result = imageAlbumService.partialUpdate(imageAlbum);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageAlbum.getId().toString())
        );
    }

    /**
     * {@code GET  /image-albums} : get all the imageAlbums.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageAlbums in body.
     */
    @GetMapping("/image-albums")
    public List<ImageAlbum> getAllImageAlbums(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ImageAlbums");
        return imageAlbumService.findAll();
    }

    /**
     * {@code GET  /image-albums/:id} : get the "id" imageAlbum.
     *
     * @param id the id of the imageAlbum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageAlbum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-albums/{id}")
    public ResponseEntity<ImageAlbum> getImageAlbum(@PathVariable Long id) {
        log.debug("REST request to get ImageAlbum : {}", id);
        Optional<ImageAlbum> imageAlbum = imageAlbumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageAlbum);
    }

    /**
     * {@code DELETE  /image-albums/:id} : delete the "id" imageAlbum.
     *
     * @param id the id of the imageAlbum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-albums/{id}")
    public ResponseEntity<Void> deleteImageAlbum(@PathVariable Long id) {
        log.debug("REST request to delete ImageAlbum : {}", id);
        imageAlbumService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
