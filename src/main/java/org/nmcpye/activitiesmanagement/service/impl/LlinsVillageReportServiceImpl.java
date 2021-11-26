package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageReport;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageReportRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LlinsVillageReport}.
 */
@Service
@Transactional
public class LlinsVillageReportServiceImpl implements LlinsVillageReportService {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageReportServiceImpl.class);

    private final LlinsVillageReportRepository lLINSVillageReportRepository;

    public LlinsVillageReportServiceImpl(LlinsVillageReportRepository lLINSVillageReportRepository) {
        this.lLINSVillageReportRepository = lLINSVillageReportRepository;
    }

    @Override
    public LlinsVillageReport save(LlinsVillageReport lLINSVillageReport) {
        log.debug("Request to save LlinsVillageReport : {}", lLINSVillageReport);
        return lLINSVillageReportRepository.save(lLINSVillageReport);
    }

    @Override
    public Optional<LlinsVillageReport> partialUpdate(LlinsVillageReport lLINSVillageReport) {
        log.debug("Request to partially update LlinsVillageReport : {}", lLINSVillageReport);

        return lLINSVillageReportRepository
            .findById(lLINSVillageReport.getId())
            .map(
                existingLLINSVillageReport -> {
                    if (lLINSVillageReport.getUid() != null) {
                        existingLLINSVillageReport.setUid(lLINSVillageReport.getUid());
                    }
                    if (lLINSVillageReport.getCreated() != null) {
                        existingLLINSVillageReport.setCreated(lLINSVillageReport.getCreated());
                    }
                    if (lLINSVillageReport.getLastUpdated() != null) {
                        existingLLINSVillageReport.setLastUpdated(lLINSVillageReport.getLastUpdated());
                    }
                    if (lLINSVillageReport.getHouses() != null) {
                        existingLLINSVillageReport.setHouses(lLINSVillageReport.getHouses());
                    }
                    if (lLINSVillageReport.getResidentHousehold() != null) {
                        existingLLINSVillageReport.setResidentHousehold(lLINSVillageReport.getResidentHousehold());
                    }
                    if (lLINSVillageReport.getIdpsHousehold() != null) {
                        existingLLINSVillageReport.setIdpsHousehold(lLINSVillageReport.getIdpsHousehold());
                    }
                    if (lLINSVillageReport.getMaleIndividuals() != null) {
                        existingLLINSVillageReport.setMaleIndividuals(lLINSVillageReport.getMaleIndividuals());
                    }
                    if (lLINSVillageReport.getFemaleIndividuals() != null) {
                        existingLLINSVillageReport.setFemaleIndividuals(lLINSVillageReport.getFemaleIndividuals());
                    }
                    if (lLINSVillageReport.getLessThan5Males() != null) {
                        existingLLINSVillageReport.setLessThan5Males(lLINSVillageReport.getLessThan5Males());
                    }
                    if (lLINSVillageReport.getLessThan5Females() != null) {
                        existingLLINSVillageReport.setLessThan5Females(lLINSVillageReport.getLessThan5Females());
                    }
                    if (lLINSVillageReport.getPregnantWomen() != null) {
                        existingLLINSVillageReport.setPregnantWomen(lLINSVillageReport.getPregnantWomen());
                    }
                    if (lLINSVillageReport.getQuantityReceived() != null) {
                        existingLLINSVillageReport.setQuantityReceived(lLINSVillageReport.getQuantityReceived());
                    }
                    if (lLINSVillageReport.getComment() != null) {
                        existingLLINSVillageReport.setComment(lLINSVillageReport.getComment());
                    }

                    return existingLLINSVillageReport;
                }
            )
            .map(lLINSVillageReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LlinsVillageReport> findAll() {
        log.debug("Request to get all LLINSVillageReports");
        return lLINSVillageReportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsVillageReport> findOne(Long id) {
        log.debug("Request to get LlinsVillageReport : {}", id);
        return lLINSVillageReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsVillageReport : {}", id);
        lLINSVillageReportRepository.deleteById(id);
    }
}
