package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageReport;
import org.nmcpye.activitiesmanagement.repository.LLINSVillageReportRepository;
import org.nmcpye.activitiesmanagement.service.LLINSVillageReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LLINSVillageReport}.
 */
@Service
@Transactional
public class LLINSVillageReportServiceImpl implements LLINSVillageReportService {

    private final Logger log = LoggerFactory.getLogger(LLINSVillageReportServiceImpl.class);

    private final LLINSVillageReportRepository lLINSVillageReportRepository;

    public LLINSVillageReportServiceImpl(LLINSVillageReportRepository lLINSVillageReportRepository) {
        this.lLINSVillageReportRepository = lLINSVillageReportRepository;
    }

    @Override
    public LLINSVillageReport save(LLINSVillageReport lLINSVillageReport) {
        log.debug("Request to save LLINSVillageReport : {}", lLINSVillageReport);
        return lLINSVillageReportRepository.save(lLINSVillageReport);
    }

    @Override
    public Optional<LLINSVillageReport> partialUpdate(LLINSVillageReport lLINSVillageReport) {
        log.debug("Request to partially update LLINSVillageReport : {}", lLINSVillageReport);

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
    public List<LLINSVillageReport> findAll() {
        log.debug("Request to get all LLINSVillageReports");
        return lLINSVillageReportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LLINSVillageReport> findOne(Long id) {
        log.debug("Request to get LLINSVillageReport : {}", id);
        return lLINSVillageReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LLINSVillageReport : {}", id);
        lLINSVillageReportRepository.deleteById(id);
    }
}
