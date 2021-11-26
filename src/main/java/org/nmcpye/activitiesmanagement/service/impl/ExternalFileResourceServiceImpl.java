package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.fileresource.ExternalFileResource;
import org.nmcpye.activitiesmanagement.repository.ExternalFileResourceRepository;
import org.nmcpye.activitiesmanagement.service.ExternalFileResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ExternalFileResource}.
 */
@Service
@Transactional
public class ExternalFileResourceServiceImpl implements ExternalFileResourceService {

    private final Logger log = LoggerFactory.getLogger(ExternalFileResourceServiceImpl.class);

    private final ExternalFileResourceRepository externalFileResourceRepository;

    public ExternalFileResourceServiceImpl(ExternalFileResourceRepository externalFileResourceRepository) {
        this.externalFileResourceRepository = externalFileResourceRepository;
    }

    @Override
    public ExternalFileResource save(ExternalFileResource externalFileResource) {
        log.debug("Request to save ExternalFileResource : {}", externalFileResource);
        return externalFileResourceRepository.save(externalFileResource);
    }

    @Override
    public Optional<ExternalFileResource> partialUpdate(ExternalFileResource externalFileResource) {
        log.debug("Request to partially update ExternalFileResource : {}", externalFileResource);

        return externalFileResourceRepository
            .findById(externalFileResource.getId())
            .map(
                existingExternalFileResource -> {
                    if (externalFileResource.getUid() != null) {
                        existingExternalFileResource.setUid(externalFileResource.getUid());
                    }
                    if (externalFileResource.getCode() != null) {
                        existingExternalFileResource.setCode(externalFileResource.getCode());
                    }
                    if (externalFileResource.getName() != null) {
                        existingExternalFileResource.setName(externalFileResource.getName());
                    }
                    if (externalFileResource.getCreated() != null) {
                        existingExternalFileResource.setCreated(externalFileResource.getCreated());
                    }
                    if (externalFileResource.getLastUpdated() != null) {
                        existingExternalFileResource.setLastUpdated(externalFileResource.getLastUpdated());
                    }
                    if (externalFileResource.getAccessToken() != null) {
                        existingExternalFileResource.setAccessToken(externalFileResource.getAccessToken());
                    }
                    if (externalFileResource.getExpires() != null) {
                        existingExternalFileResource.setExpires(externalFileResource.getExpires());
                    }

                    return existingExternalFileResource;
                }
            )
            .map(externalFileResourceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExternalFileResource> findAll() {
        log.debug("Request to get all ExternalFileResources");
        return externalFileResourceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExternalFileResource> findOne(Long id) {
        log.debug("Request to get ExternalFileResource : {}", id);
        return externalFileResourceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExternalFileResource : {}", id);
        externalFileResourceRepository.deleteById(id);
    }
}
