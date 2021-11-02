package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReportHistory;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LLINSFamilyReportHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LLINSFamilyReportHistory}.
 */
@Service
@Transactional
public class LLINSFamilyReportHistoryServiceImpl implements LLINSFamilyReportHistoryService {

    private final Logger log = LoggerFactory.getLogger(LLINSFamilyReportHistoryServiceImpl.class);

    private final LLINSFamilyReportHistoryRepository lLINSFamilyReportHistoryRepository;

    public LLINSFamilyReportHistoryServiceImpl(LLINSFamilyReportHistoryRepository lLINSFamilyReportHistoryRepository) {
        this.lLINSFamilyReportHistoryRepository = lLINSFamilyReportHistoryRepository;
    }

    @Override
    public LLINSFamilyReportHistory save(LLINSFamilyReportHistory lLINSFamilyReportHistory) {
        log.debug("Request to save LLINSFamilyReportHistory : {}", lLINSFamilyReportHistory);
        return lLINSFamilyReportHistoryRepository.save(lLINSFamilyReportHistory);
    }

    @Override
    public Optional<LLINSFamilyReportHistory> partialUpdate(LLINSFamilyReportHistory lLINSFamilyReportHistory) {
        log.debug("Request to partially update LLINSFamilyReportHistory : {}", lLINSFamilyReportHistory);

        return lLINSFamilyReportHistoryRepository
            .findById(lLINSFamilyReportHistory.getId())
            .map(
                existingLLINSFamilyReportHistory -> {
                    if (lLINSFamilyReportHistory.getUid() != null) {
                        existingLLINSFamilyReportHistory.setUid(lLINSFamilyReportHistory.getUid());
                    }
                    if (lLINSFamilyReportHistory.getCreated() != null) {
                        existingLLINSFamilyReportHistory.setCreated(lLINSFamilyReportHistory.getCreated());
                    }
                    if (lLINSFamilyReportHistory.getLastUpdated() != null) {
                        existingLLINSFamilyReportHistory.setLastUpdated(lLINSFamilyReportHistory.getLastUpdated());
                    }
                    if (lLINSFamilyReportHistory.getDocumentNo() != null) {
                        existingLLINSFamilyReportHistory.setDocumentNo(lLINSFamilyReportHistory.getDocumentNo());
                    }
                    if (lLINSFamilyReportHistory.getMaleIndividuals() != null) {
                        existingLLINSFamilyReportHistory.setMaleIndividuals(lLINSFamilyReportHistory.getMaleIndividuals());
                    }
                    if (lLINSFamilyReportHistory.getFemaleIndividuals() != null) {
                        existingLLINSFamilyReportHistory.setFemaleIndividuals(lLINSFamilyReportHistory.getFemaleIndividuals());
                    }
                    if (lLINSFamilyReportHistory.getLessThan5Males() != null) {
                        existingLLINSFamilyReportHistory.setLessThan5Males(lLINSFamilyReportHistory.getLessThan5Males());
                    }
                    if (lLINSFamilyReportHistory.getLessThan5Females() != null) {
                        existingLLINSFamilyReportHistory.setLessThan5Females(lLINSFamilyReportHistory.getLessThan5Females());
                    }
                    if (lLINSFamilyReportHistory.getPregnantWomen() != null) {
                        existingLLINSFamilyReportHistory.setPregnantWomen(lLINSFamilyReportHistory.getPregnantWomen());
                    }
                    if (lLINSFamilyReportHistory.getQuantityReceived() != null) {
                        existingLLINSFamilyReportHistory.setQuantityReceived(lLINSFamilyReportHistory.getQuantityReceived());
                    }
                    if (lLINSFamilyReportHistory.getFamilyType() != null) {
                        existingLLINSFamilyReportHistory.setFamilyType(lLINSFamilyReportHistory.getFamilyType());
                    }

                    return existingLLINSFamilyReportHistory;
                }
            )
            .map(lLINSFamilyReportHistoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LLINSFamilyReportHistory> findAll() {
        log.debug("Request to get all LLINSFamilyReportHistories");
        return lLINSFamilyReportHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LLINSFamilyReportHistory> findOne(Long id) {
        log.debug("Request to get LLINSFamilyReportHistory : {}", id);
        return lLINSFamilyReportHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LLINSFamilyReportHistory : {}", id);
        lLINSFamilyReportHistoryRepository.deleteById(id);
    }
}
