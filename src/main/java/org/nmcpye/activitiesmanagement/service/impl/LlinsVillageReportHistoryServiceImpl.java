package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReportHistory;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportHistoryRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageReportHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link LlinsVillageReportHistory}.
 */
@Service
@Transactional
public class LlinsVillageReportHistoryServiceImpl implements LlinsVillageReportHistoryService {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageReportHistoryServiceImpl.class);

    private final LlinsVillageReportHistoryRepository llinsVillageReportHistoryRepository;

    public LlinsVillageReportHistoryServiceImpl(LlinsVillageReportHistoryRepository llinsVillageReportHistoryRepository) {
        this.llinsVillageReportHistoryRepository = llinsVillageReportHistoryRepository;
    }

    @Override
    public LlinsVillageReportHistory save(LlinsVillageReportHistory llinsVillageReportHistory) {
        log.debug("Request to save LlinsVillageReportHistory : {}", llinsVillageReportHistory);
        return llinsVillageReportHistoryRepository.save(llinsVillageReportHistory);
    }

    @Override
    public Optional<LlinsVillageReportHistory> partialUpdate(LlinsVillageReportHistory llinsVillageReportHistory) {
        log.debug("Request to partially update LlinsVillageReportHistory : {}", llinsVillageReportHistory);

        return llinsVillageReportHistoryRepository
            .findById(llinsVillageReportHistory.getId())
            .map(
                existingLlinsVillageReportHistory -> {
                    if (llinsVillageReportHistory.getUid() != null) {
                        existingLlinsVillageReportHistory.setUid(llinsVillageReportHistory.getUid());
                    }
                    if (llinsVillageReportHistory.getCreated() != null) {
                        existingLlinsVillageReportHistory.setCreated(llinsVillageReportHistory.getCreated());
                    }
                    if (llinsVillageReportHistory.getLastUpdated() != null) {
                        existingLlinsVillageReportHistory.setLastUpdated(llinsVillageReportHistory.getLastUpdated());
                    }
                    if (llinsVillageReportHistory.getHouses() != null) {
                        existingLlinsVillageReportHistory.setHouses(llinsVillageReportHistory.getHouses());
                    }
                    if (llinsVillageReportHistory.getResidentHousehold() != null) {
                        existingLlinsVillageReportHistory.setResidentHousehold(llinsVillageReportHistory.getResidentHousehold());
                    }
                    if (llinsVillageReportHistory.getIdpsHousehold() != null) {
                        existingLlinsVillageReportHistory.setIdpsHousehold(llinsVillageReportHistory.getIdpsHousehold());
                    }
                    if (llinsVillageReportHistory.getMaleIndividuals() != null) {
                        existingLlinsVillageReportHistory.setMaleIndividuals(llinsVillageReportHistory.getMaleIndividuals());
                    }
                    if (llinsVillageReportHistory.getFemaleIndividuals() != null) {
                        existingLlinsVillageReportHistory.setFemaleIndividuals(llinsVillageReportHistory.getFemaleIndividuals());
                    }
                    if (llinsVillageReportHistory.getLessThan5Males() != null) {
                        existingLlinsVillageReportHistory.setLessThan5Males(llinsVillageReportHistory.getLessThan5Males());
                    }
                    if (llinsVillageReportHistory.getLessThan5Females() != null) {
                        existingLlinsVillageReportHistory.setLessThan5Females(llinsVillageReportHistory.getLessThan5Females());
                    }
                    if (llinsVillageReportHistory.getPregnantWomen() != null) {
                        existingLlinsVillageReportHistory.setPregnantWomen(llinsVillageReportHistory.getPregnantWomen());
                    }
                    if (llinsVillageReportHistory.getQuantityReceived() != null) {
                        existingLlinsVillageReportHistory.setQuantityReceived(llinsVillageReportHistory.getQuantityReceived());
                    }

                    return existingLlinsVillageReportHistory;
                }
            )
            .map(llinsVillageReportHistoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LlinsVillageReportHistory> findAll() {
        log.debug("Request to get all LlinsVillageReportHistories");
        return llinsVillageReportHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsVillageReportHistory> findOne(Long id) {
        log.debug("Request to get LlinsVillageReportHistory : {}", id);
        return llinsVillageReportHistoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsVillageReportHistory : {}", id);
        llinsVillageReportHistoryRepository.deleteById(id);
    }
}
