package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageReportHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LlinsVillageReportHistory}.
 */
@Service
@Transactional
public class LlinsVillageReportHistoryServiceImpl implements LlinsVillageReportHistoryService {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageReportHistoryServiceImpl.class);

    private final LlinsVillageReportHistoryRepository lLINSVillageReportHistoryRepository;

    public LlinsVillageReportHistoryServiceImpl(LlinsVillageReportHistoryRepository lLINSVillageReportHistoryRepository) {
        this.lLINSVillageReportHistoryRepository = lLINSVillageReportHistoryRepository;
    }

    @Override
    public LlinsVillageReportHistory save(LlinsVillageReportHistory lLINSVillageReportHistory) {
        log.debug("Request to save LlinsVillageReportHistory : {}", lLINSVillageReportHistory);
        return lLINSVillageReportHistoryRepository.save(lLINSVillageReportHistory);
    }

    @Override
    public Optional<LlinsVillageReportHistory> partialUpdate(LlinsVillageReportHistory lLINSVillageReportHistory) {
        log.debug("Request to partially update LlinsVillageReportHistory : {}", lLINSVillageReportHistory);

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
    public List<LlinsVillageReportHistory> findAll() {
        log.debug("Request to get all LLINSVillageReportHistories");
        return lLINSVillageReportHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsVillageReportHistory> findOne(Long id) {
        log.debug("Request to get LlinsVillageReportHistory : {}", id);
        return lLINSVillageReportHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsVillageReportHistory : {}", id);
        lLINSVillageReportHistoryRepository.deleteById(id);
    }
}
