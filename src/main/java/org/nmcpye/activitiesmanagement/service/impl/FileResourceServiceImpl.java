package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.fileresource.FileResource;
import org.nmcpye.activitiesmanagement.repository.FileResourceRepository;
import org.nmcpye.activitiesmanagement.service.FileResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link FileResource}.
 */
@Service
@Transactional
public class FileResourceServiceImpl implements FileResourceService {

    private final Logger log = LoggerFactory.getLogger(FileResourceServiceImpl.class);

    private final FileResourceRepository fileResourceRepository;

    public FileResourceServiceImpl(FileResourceRepository fileResourceRepository) {
        this.fileResourceRepository = fileResourceRepository;
    }

    @Override
    public FileResource save(FileResource fileResource) {
        log.debug("Request to save FileResource : {}", fileResource);
        return fileResourceRepository.save(fileResource);
    }

    @Override
    public Optional<FileResource> partialUpdate(FileResource fileResource) {
        log.debug("Request to partially update FileResource : {}", fileResource);

        return fileResourceRepository
            .findById(fileResource.getId())
            .map(
                existingFileResource -> {
                    if (fileResource.getUid() != null) {
                        existingFileResource.setUid(fileResource.getUid());
                    }
                    if (fileResource.getCode() != null) {
                        existingFileResource.setCode(fileResource.getCode());
                    }
                    if (fileResource.getName() != null) {
                        existingFileResource.setName(fileResource.getName());
                    }
                    if (fileResource.getCreated() != null) {
                        existingFileResource.setCreated(fileResource.getCreated());
                    }
                    if (fileResource.getLastUpdated() != null) {
                        existingFileResource.setLastUpdated(fileResource.getLastUpdated());
                    }
                    if (fileResource.getContentType() != null) {
                        existingFileResource.setContentType(fileResource.getContentType());
                    }
                    if (fileResource.getContentLength() != 0) {
                        existingFileResource.setContentLength(fileResource.getContentLength());
                    }
                    if (fileResource.getContentMd5() != null) {
                        existingFileResource.setContentMd5(fileResource.getContentMd5());
                    }
                    if (fileResource.getStorageKey() != null) {
                        existingFileResource.setStorageKey(fileResource.getStorageKey());
                    }
//                    if (fileResource.getAssigned() != null) {
//                        existingFileResource.setAssigned(fileResource.getAssigned());
//                    }
                    if (fileResource.getDomain() != null) {
                        existingFileResource.setDomain(fileResource.getDomain());
                    }
//                    if (fileResource.getHasMultipleStorageFiles() != null) {
//                        existingFileResource.setHasMultipleStorageFiles(fileResource.getHasMultipleStorageFiles());
//                    }

                    return existingFileResource;
                }
            )
            .map(fileResourceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileResource> findAll() {
        log.debug("Request to get all FileResources");
        return fileResourceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileResource> findOne(Long id) {
        log.debug("Request to get FileResource : {}", id);
        return fileResourceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileResource : {}", id);
        fileResourceRepository.deleteById(id);
    }
}
