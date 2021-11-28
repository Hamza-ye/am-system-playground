package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;
import org.nmcpye.activitiesmanagement.repository.RelatedLinkRepository;
import org.nmcpye.activitiesmanagement.service.RelatedLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link RelatedLink}.
 */
@Service
@Transactional
public class RelatedLinkServiceImpl implements RelatedLinkService {

    private final Logger log = LoggerFactory.getLogger(RelatedLinkServiceImpl.class);

    private final RelatedLinkRepository relatedLinkRepository;

    public RelatedLinkServiceImpl(RelatedLinkRepository relatedLinkRepository) {
        this.relatedLinkRepository = relatedLinkRepository;
    }

    @Override
    public RelatedLink save(RelatedLink relatedLink) {
        log.debug("Request to save RelatedLink : {}", relatedLink);
        return relatedLinkRepository.save(relatedLink);
    }

    @Override
    public Optional<RelatedLink> partialUpdate(RelatedLink relatedLink) {
        log.debug("Request to partially update RelatedLink : {}", relatedLink);

        return relatedLinkRepository
            .findById(relatedLink.getId())
            .map(
                existingRelatedLink -> {
                    if (relatedLink.getUid() != null) {
                        existingRelatedLink.setUid(relatedLink.getUid());
                    }
                    if (relatedLink.getName() != null) {
                        existingRelatedLink.setName(relatedLink.getName());
                    }
                    if (relatedLink.getCreated() != null) {
                        existingRelatedLink.setCreated(relatedLink.getCreated());
                    }
                    if (relatedLink.getLastUpdated() != null) {
                        existingRelatedLink.setLastUpdated(relatedLink.getLastUpdated());
                    }
                    if (relatedLink.getUrl() != null) {
                        existingRelatedLink.setUrl(relatedLink.getUrl());
                    }

                    return existingRelatedLink;
                }
            )
            .map(relatedLinkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelatedLink> findAll() {
        log.debug("Request to get all RelatedLinks");
        return relatedLinkRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RelatedLink> findOne(Long id) {
        log.debug("Request to get RelatedLink : {}", id);
        return relatedLinkRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RelatedLink : {}", id);
        relatedLinkRepository.deleteById(id);
    }
}
