package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyReport;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyReportRepository;
import org.nmcpye.activitiesmanagement.service.LLINSFamilyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LLINSFamilyReport}.
 */
@Service
@Transactional
public class LLINSFamilyReportServiceImpl implements LLINSFamilyReportService {

    private final Logger log = LoggerFactory.getLogger(LLINSFamilyReportServiceImpl.class);

    private final LLINSFamilyReportRepository lLINSFamilyReportRepository;

    public LLINSFamilyReportServiceImpl(LLINSFamilyReportRepository lLINSFamilyReportRepository) {
        this.lLINSFamilyReportRepository = lLINSFamilyReportRepository;
    }

    @Override
    public LLINSFamilyReport save(LLINSFamilyReport lLINSFamilyReport) {
        log.debug("Request to save LLINSFamilyReport : {}", lLINSFamilyReport);
        return lLINSFamilyReportRepository.save(lLINSFamilyReport);
    }

    @Override
    public Optional<LLINSFamilyReport> partialUpdate(LLINSFamilyReport lLINSFamilyReport) {
        log.debug("Request to partially update LLINSFamilyReport : {}", lLINSFamilyReport);

        return lLINSFamilyReportRepository
            .findById(lLINSFamilyReport.getId())
            .map(
                existingLLINSFamilyReport -> {
                    if (lLINSFamilyReport.getUid() != null) {
                        existingLLINSFamilyReport.setUid(lLINSFamilyReport.getUid());
                    }
                    if (lLINSFamilyReport.getCreated() != null) {
                        existingLLINSFamilyReport.setCreated(lLINSFamilyReport.getCreated());
                    }
                    if (lLINSFamilyReport.getLastUpdated() != null) {
                        existingLLINSFamilyReport.setLastUpdated(lLINSFamilyReport.getLastUpdated());
                    }
                    if (lLINSFamilyReport.getCheckNo() != null) {
                        existingLLINSFamilyReport.setCheckNo(lLINSFamilyReport.getCheckNo());
                    }
                    if (lLINSFamilyReport.getMaleIndividuals() != null) {
                        existingLLINSFamilyReport.setMaleIndividuals(lLINSFamilyReport.getMaleIndividuals());
                    }
                    if (lLINSFamilyReport.getFemaleIndividuals() != null) {
                        existingLLINSFamilyReport.setFemaleIndividuals(lLINSFamilyReport.getFemaleIndividuals());
                    }
                    if (lLINSFamilyReport.getLessThan5Males() != null) {
                        existingLLINSFamilyReport.setLessThan5Males(lLINSFamilyReport.getLessThan5Males());
                    }
                    if (lLINSFamilyReport.getLessThan5Females() != null) {
                        existingLLINSFamilyReport.setLessThan5Females(lLINSFamilyReport.getLessThan5Females());
                    }
                    if (lLINSFamilyReport.getPregnantWomen() != null) {
                        existingLLINSFamilyReport.setPregnantWomen(lLINSFamilyReport.getPregnantWomen());
                    }
                    if (lLINSFamilyReport.getQuantityReceived() != null) {
                        existingLLINSFamilyReport.setQuantityReceived(lLINSFamilyReport.getQuantityReceived());
                    }
                    if (lLINSFamilyReport.getFamilyType() != null) {
                        existingLLINSFamilyReport.setFamilyType(lLINSFamilyReport.getFamilyType());
                    }
                    if (lLINSFamilyReport.getComment() != null) {
                        existingLLINSFamilyReport.setComment(lLINSFamilyReport.getComment());
                    }

                    return existingLLINSFamilyReport;
                }
            )
            .map(lLINSFamilyReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LLINSFamilyReport> findAll(Pageable pageable) {
        log.debug("Request to get all LLINSFamilyReports");
        return lLINSFamilyReportRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LLINSFamilyReport> findOne(Long id) {
        log.debug("Request to get LLINSFamilyReport : {}", id);
        return lLINSFamilyReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LLINSFamilyReport : {}", id);
        lLINSFamilyReportRepository.deleteById(id);
    }
}
