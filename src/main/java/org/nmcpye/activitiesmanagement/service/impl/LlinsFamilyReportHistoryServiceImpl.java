package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReportHistory;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LlinsFamilyReportHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link LlinsFamilyReportHistory}.
 */
@Service
@Transactional
public class LlinsFamilyReportHistoryServiceImpl implements LlinsFamilyReportHistoryService {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyReportHistoryServiceImpl.class);

    private final LlinsFamilyReportHistoryRepository llinsFamilyReportHistoryRepository;

    public LlinsFamilyReportHistoryServiceImpl(LlinsFamilyReportHistoryRepository llinsFamilyReportHistoryRepository) {
        this.llinsFamilyReportHistoryRepository = llinsFamilyReportHistoryRepository;
    }

    @Override
    public LlinsFamilyReportHistory save(LlinsFamilyReportHistory llinsFamilyReportHistory) {
        log.debug("Request to save LlinsFamilyReportHistory : {}", llinsFamilyReportHistory);
        return llinsFamilyReportHistoryRepository.save(llinsFamilyReportHistory);
    }

    @Override
    public Optional<LlinsFamilyReportHistory> partialUpdate(LlinsFamilyReportHistory llinsFamilyReportHistory) {
        log.debug("Request to partially update LlinsFamilyReportHistory : {}", llinsFamilyReportHistory);

        return llinsFamilyReportHistoryRepository
            .findById(llinsFamilyReportHistory.getId())
            .map(
                existingLlinsFamilyReportHistory -> {
                    if (llinsFamilyReportHistory.getUid() != null) {
                        existingLlinsFamilyReportHistory.setUid(llinsFamilyReportHistory.getUid());
                    }
                    if (llinsFamilyReportHistory.getCreated() != null) {
                        existingLlinsFamilyReportHistory.setCreated(llinsFamilyReportHistory.getCreated());
                    }
                    if (llinsFamilyReportHistory.getLastUpdated() != null) {
                        existingLlinsFamilyReportHistory.setLastUpdated(llinsFamilyReportHistory.getLastUpdated());
                    }
                    if (llinsFamilyReportHistory.getDocumentNo() != null) {
                        existingLlinsFamilyReportHistory.setDocumentNo(llinsFamilyReportHistory.getDocumentNo());
                    }
                    if (llinsFamilyReportHistory.getMaleIndividuals() != null) {
                        existingLlinsFamilyReportHistory.setMaleIndividuals(llinsFamilyReportHistory.getMaleIndividuals());
                    }
                    if (llinsFamilyReportHistory.getFemaleIndividuals() != null) {
                        existingLlinsFamilyReportHistory.setFemaleIndividuals(llinsFamilyReportHistory.getFemaleIndividuals());
                    }
                    if (llinsFamilyReportHistory.getLessThan5Males() != null) {
                        existingLlinsFamilyReportHistory.setLessThan5Males(llinsFamilyReportHistory.getLessThan5Males());
                    }
                    if (llinsFamilyReportHistory.getLessThan5Females() != null) {
                        existingLlinsFamilyReportHistory.setLessThan5Females(llinsFamilyReportHistory.getLessThan5Females());
                    }
                    if (llinsFamilyReportHistory.getPregnantWomen() != null) {
                        existingLlinsFamilyReportHistory.setPregnantWomen(llinsFamilyReportHistory.getPregnantWomen());
                    }
                    if (llinsFamilyReportHistory.getQuantityReceived() != null) {
                        existingLlinsFamilyReportHistory.setQuantityReceived(llinsFamilyReportHistory.getQuantityReceived());
                    }
                    if (llinsFamilyReportHistory.getFamilyType() != null) {
                        existingLlinsFamilyReportHistory.setFamilyType(llinsFamilyReportHistory.getFamilyType());
                    }

                    return existingLlinsFamilyReportHistory;
                }
            )
            .map(llinsFamilyReportHistoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LlinsFamilyReportHistory> findAll() {
        log.debug("Request to get all LlinsFamilyReportHistories");
        return llinsFamilyReportHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsFamilyReportHistory> findOne(Long id) {
        log.debug("Request to get LlinsFamilyReportHistory : {}", id);
        return llinsFamilyReportHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsFamilyReportHistory : {}", id);
        llinsFamilyReportHistoryRepository.deleteById(id);
    }
}
