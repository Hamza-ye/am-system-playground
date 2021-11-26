package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.document.Document;
import org.nmcpye.activitiesmanagement.repository.DocumentRepository;
import org.nmcpye.activitiesmanagement.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document save(Document document) {
        log.debug("Request to save Document : {}", document);
        return documentRepository.save(document);
    }

    @Override
    public Optional<Document> partialUpdate(Document document) {
        log.debug("Request to partially update Document : {}", document);

        return documentRepository
            .findById(document.getId())
            .map(
                existingDocument -> {
                    if (document.getUid() != null) {
                        existingDocument.setUid(document.getUid());
                    }
                    if (document.getCode() != null) {
                        existingDocument.setCode(document.getCode());
                    }
                    if (document.getName() != null) {
                        existingDocument.setName(document.getName());
                    }
                    if (document.getCreated() != null) {
                        existingDocument.setCreated(document.getCreated());
                    }
                    if (document.getLastUpdated() != null) {
                        existingDocument.setLastUpdated(document.getLastUpdated());
                    }
                    if (document.getUrl() != null) {
                        existingDocument.setUrl(document.getUrl());
                    }
//                    if (document.getExternal() != null) {
//                        existingDocument.setExternal(document.getExternal());
//                    }
                    if (document.getContentType() != null) {
                        existingDocument.setContentType(document.getContentType());
                    }
                    if (document.getAttachment() != null) {
                        existingDocument.setAttachment(document.getAttachment());
                    }

                    return existingDocument;
                }
            )
            .map(documentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> findAll() {
        log.debug("Request to get all Documents");
        return documentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
    }
}
