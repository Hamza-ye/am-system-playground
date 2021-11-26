package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link LlinsVillageReport}.
 */
@Service
@Transactional
public class LlinsVillageReportServiceImpl implements LlinsVillageReportService {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageReportServiceImpl.class);

    private final LlinsVillageReportRepository llinsVillageReportRepository;

    public LlinsVillageReportServiceImpl(LlinsVillageReportRepository llinsVillageReportRepository) {
        this.llinsVillageReportRepository = llinsVillageReportRepository;
    }

    @Override
    public LlinsVillageReport save(LlinsVillageReport llinsVillageReport) {
        log.debug("Request to save LlinsVillageReport : {}", llinsVillageReport);
        return llinsVillageReportRepository.save(llinsVillageReport);
    }

    @Override
    public Optional<LlinsVillageReport> partialUpdate(LlinsVillageReport llinsVillageReport) {
        log.debug("Request to partially update LlinsVillageReport : {}", llinsVillageReport);

        return llinsVillageReportRepository
            .findById(llinsVillageReport.getId())
            .map(
                existingLlinsVillageReport -> {
                    if (llinsVillageReport.getUid() != null) {
                        existingLlinsVillageReport.setUid(llinsVillageReport.getUid());
                    }
                    if (llinsVillageReport.getCreated() != null) {
                        existingLlinsVillageReport.setCreated(llinsVillageReport.getCreated());
                    }
                    if (llinsVillageReport.getLastUpdated() != null) {
                        existingLlinsVillageReport.setLastUpdated(llinsVillageReport.getLastUpdated());
                    }
                    if (llinsVillageReport.getHouses() != null) {
                        existingLlinsVillageReport.setHouses(llinsVillageReport.getHouses());
                    }
                    if (llinsVillageReport.getResidentHousehold() != null) {
                        existingLlinsVillageReport.setResidentHousehold(llinsVillageReport.getResidentHousehold());
                    }
                    if (llinsVillageReport.getIdpsHousehold() != null) {
                        existingLlinsVillageReport.setIdpsHousehold(llinsVillageReport.getIdpsHousehold());
                    }
                    if (llinsVillageReport.getMaleIndividuals() != null) {
                        existingLlinsVillageReport.setMaleIndividuals(llinsVillageReport.getMaleIndividuals());
                    }
                    if (llinsVillageReport.getFemaleIndividuals() != null) {
                        existingLlinsVillageReport.setFemaleIndividuals(llinsVillageReport.getFemaleIndividuals());
                    }
                    if (llinsVillageReport.getLessThan5Males() != null) {
                        existingLlinsVillageReport.setLessThan5Males(llinsVillageReport.getLessThan5Males());
                    }
                    if (llinsVillageReport.getLessThan5Females() != null) {
                        existingLlinsVillageReport.setLessThan5Females(llinsVillageReport.getLessThan5Females());
                    }
                    if (llinsVillageReport.getPregnantWomen() != null) {
                        existingLlinsVillageReport.setPregnantWomen(llinsVillageReport.getPregnantWomen());
                    }
                    if (llinsVillageReport.getQuantityReceived() != null) {
                        existingLlinsVillageReport.setQuantityReceived(llinsVillageReport.getQuantityReceived());
                    }
                    if (llinsVillageReport.getComment() != null) {
                        existingLlinsVillageReport.setComment(llinsVillageReport.getComment());
                    }

                    return existingLlinsVillageReport;
                }
            )
            .map(llinsVillageReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LlinsVillageReport> findAll() {
        log.debug("Request to get all LlinsVillageReports");
        return llinsVillageReportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsVillageReport> findOne(Long id) {
        log.debug("Request to get LlinsVillageReport : {}", id);
        return llinsVillageReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsVillageReport : {}", id);
        llinsVillageReportRepository.deleteById(id);
    }
}
