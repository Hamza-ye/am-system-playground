package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReportHistory;
import org.nmcpye.activitiesmanagement.repository.LLINSVillageReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LLINSVillageReportHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LLINSVillageReportHistory}.
 */
@Service
@Transactional
public class LLINSVillageReportHistoryServiceImpl implements LLINSVillageReportHistoryService {

    private final Logger log = LoggerFactory.getLogger(LLINSVillageReportHistoryServiceImpl.class);

    private final LLINSVillageReportHistoryRepository lLINSVillageReportHistoryRepository;

    public LLINSVillageReportHistoryServiceImpl(LLINSVillageReportHistoryRepository lLINSVillageReportHistoryRepository) {
        this.lLINSVillageReportHistoryRepository = lLINSVillageReportHistoryRepository;
    }

    @Override
    public LLINSVillageReportHistory save(LLINSVillageReportHistory lLINSVillageReportHistory) {
        log.debug("Request to save LLINSVillageReportHistory : {}", lLINSVillageReportHistory);
        return lLINSVillageReportHistoryRepository.save(lLINSVillageReportHistory);
    }

    @Override
    public Optional<LLINSVillageReportHistory> partialUpdate(LLINSVillageReportHistory lLINSVillageReportHistory) {
        log.debug("Request to partially update LLINSVillageReportHistory : {}", lLINSVillageReportHistory);

        return lLINSVillageReportHistoryRepository
            .findById(lLINSVillageReportHistory.getId())
            .map(
                existingLLINSVillageReportHistory -> {
                    if (lLINSVillageReportHistory.getUid() != null) {
                        existingLLINSVillageReportHistory.setUid(lLINSVillageReportHistory.getUid());
                    }
                    if (lLINSVillageReportHistory.getCreated() != null) {
                        existingLLINSVillageReportHistory.setCreated(lLINSVillageReportHistory.getCreated());
                    }
                    if (lLINSVillageReportHistory.getLastUpdated() != null) {
                        existingLLINSVillageReportHistory.setLastUpdated(lLINSVillageReportHistory.getLastUpdated());
                    }
                    if (lLINSVillageReportHistory.getHouses() != null) {
                        existingLLINSVillageReportHistory.setHouses(lLINSVillageReportHistory.getHouses());
                    }
                    if (lLINSVillageReportHistory.getResidentHousehold() != null) {
                        existingLLINSVillageReportHistory.setResidentHousehold(lLINSVillageReportHistory.getResidentHousehold());
                    }
                    if (lLINSVillageReportHistory.getIdpsHousehold() != null) {
                        existingLLINSVillageReportHistory.setIdpsHousehold(lLINSVillageReportHistory.getIdpsHousehold());
                    }
                    if (lLINSVillageReportHistory.getMaleIndividuals() != null) {
                        existingLLINSVillageReportHistory.setMaleIndividuals(lLINSVillageReportHistory.getMaleIndividuals());
                    }
                    if (lLINSVillageReportHistory.getFemaleIndividuals() != null) {
                        existingLLINSVillageReportHistory.setFemaleIndividuals(lLINSVillageReportHistory.getFemaleIndividuals());
                    }
                    if (lLINSVillageReportHistory.getLessThan5Males() != null) {
                        existingLLINSVillageReportHistory.setLessThan5Males(lLINSVillageReportHistory.getLessThan5Males());
                    }
                    if (lLINSVillageReportHistory.getLessThan5Females() != null) {
                        existingLLINSVillageReportHistory.setLessThan5Females(lLINSVillageReportHistory.getLessThan5Females());
                    }
                    if (lLINSVillageReportHistory.getPregnantWomen() != null) {
                        existingLLINSVillageReportHistory.setPregnantWomen(lLINSVillageReportHistory.getPregnantWomen());
                    }
                    if (lLINSVillageReportHistory.getQuantityReceived() != null) {
                        existingLLINSVillageReportHistory.setQuantityReceived(lLINSVillageReportHistory.getQuantityReceived());
                    }

                    return existingLLINSVillageReportHistory;
                }
            )
            .map(lLINSVillageReportHistoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LLINSVillageReportHistory> findAll() {
        log.debug("Request to get all LLINSVillageReportHistories");
        return lLINSVillageReportHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LLINSVillageReportHistory> findOne(Long id) {
        log.debug("Request to get LLINSVillageReportHistory : {}", id);
        return lLINSVillageReportHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LLINSVillageReportHistory : {}", id);
        lLINSVillageReportHistoryRepository.deleteById(id);
    }
}
