package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.nmcpye.activitiesmanagement.repository.ContentPageRepository;
import org.nmcpye.activitiesmanagement.service.ContentPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ContentPage}.
 */
@Service
@Transactional
public class ContentPageServiceImpl implements ContentPageService {

    private final Logger log = LoggerFactory.getLogger(ContentPageServiceImpl.class);

    private final ContentPageRepository contentPageRepository;

    public ContentPageServiceImpl(ContentPageRepository contentPageRepository) {
        this.contentPageRepository = contentPageRepository;
    }

    @Override
    public ContentPage save(ContentPage contentPage) {
        log.debug("Request to save ContentPage : {}", contentPage);
        return contentPageRepository.save(contentPage);
    }

    @Override
    public Optional<ContentPage> partialUpdate(ContentPage contentPage) {
        log.debug("Request to partially update ContentPage : {}", contentPage);

        return contentPageRepository
            .findById(contentPage.getId())
            .map(
                existingContentPage -> {
                    if (contentPage.getUid() != null) {
                        existingContentPage.setUid(contentPage.getUid());
                    }
                    if (contentPage.getCode() != null) {
                        existingContentPage.setCode(contentPage.getCode());
                    }
                    if (contentPage.getName() != null) {
                        existingContentPage.setName(contentPage.getName());
                    }
                    if (contentPage.getCreated() != null) {
                        existingContentPage.setCreated(contentPage.getCreated());
                    }
                    if (contentPage.getLastUpdated() != null) {
                        existingContentPage.setLastUpdated(contentPage.getLastUpdated());
                    }
                    if (contentPage.getTitle() != null) {
                        existingContentPage.setTitle(contentPage.getTitle());
                    }
                    if (contentPage.getSubtitle() != null) {
                        existingContentPage.setSubtitle(contentPage.getSubtitle());
                    }
                    if (contentPage.getContent() != null) {
                        existingContentPage.setContent(contentPage.getContent());
                    }
                    if (contentPage.getActive() != null) {
                        existingContentPage.setActive(contentPage.getActive());
                    }
                    if (contentPage.getVisitedCount() != null) {
                        existingContentPage.setVisitedCount(contentPage.getVisitedCount());
                    }

                    return existingContentPage;
                }
            )
            .map(contentPageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentPage> findAll() {
        log.debug("Request to get all ContentPages");
        return contentPageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentPage> findOne(Long id) {
        log.debug("Request to get ContentPage : {}", id);
        return contentPageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContentPage : {}", id);
        contentPageRepository.deleteById(id);
    }
}
