package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.nmcpye.activitiesmanagement.repository.ImageAlbumRepository;
import org.nmcpye.activitiesmanagement.service.ImageAlbumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ImageAlbum}.
 */
@Service
@Transactional
public class ImageAlbumServiceImpl implements ImageAlbumService {

    private final Logger log = LoggerFactory.getLogger(ImageAlbumServiceImpl.class);

    private final ImageAlbumRepository imageAlbumRepository;

    public ImageAlbumServiceImpl(ImageAlbumRepository imageAlbumRepository) {
        this.imageAlbumRepository = imageAlbumRepository;
    }

    @Override
    public ImageAlbum save(ImageAlbum imageAlbum) {
        log.debug("Request to save ImageAlbum : {}", imageAlbum);
        return imageAlbumRepository.save(imageAlbum);
    }

    @Override
    public Optional<ImageAlbum> partialUpdate(ImageAlbum imageAlbum) {
        log.debug("Request to partially update ImageAlbum : {}", imageAlbum);

        return imageAlbumRepository
            .findById(imageAlbum.getId())
            .map(
                existingImageAlbum -> {
                    if (imageAlbum.getUid() != null) {
                        existingImageAlbum.setUid(imageAlbum.getUid());
                    }
                    if (imageAlbum.getCode() != null) {
                        existingImageAlbum.setCode(imageAlbum.getCode());
                    }
                    if (imageAlbum.getName() != null) {
                        existingImageAlbum.setName(imageAlbum.getName());
                    }
                    if (imageAlbum.getCreated() != null) {
                        existingImageAlbum.setCreated(imageAlbum.getCreated());
                    }
                    if (imageAlbum.getLastUpdated() != null) {
                        existingImageAlbum.setLastUpdated(imageAlbum.getLastUpdated());
                    }
                    if (imageAlbum.getTitle() != null) {
                        existingImageAlbum.setTitle(imageAlbum.getTitle());
                    }
                    if (imageAlbum.getCoverImageUid() != null) {
                        existingImageAlbum.setCoverImageUid(imageAlbum.getCoverImageUid());
                    }
                    if (imageAlbum.getSubtitle() != null) {
                        existingImageAlbum.setSubtitle(imageAlbum.getSubtitle());
                    }

                    return existingImageAlbum;
                }
            )
            .map(imageAlbumRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageAlbum> findAll() {
        log.debug("Request to get all ImageAlbums");
        return imageAlbumRepository.findAllWithEagerRelationships();
    }

    public Page<ImageAlbum> findAllWithEagerRelationships(Pageable pageable) {
        return imageAlbumRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImageAlbum> findOne(Long id) {
        log.debug("Request to get ImageAlbum : {}", id);
        return imageAlbumRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ImageAlbum : {}", id);
        imageAlbumRepository.deleteById(id);
    }
}
