package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyReport;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyReportRepository;
import org.nmcpye.activitiesmanagement.service.LlinsFamilyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LlinsFamilyReport}.
 */
@Service
@Transactional
public class LlinsFamilyReportServiceImpl implements LlinsFamilyReportService {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyReportServiceImpl.class);

    private final LlinsFamilyReportRepository llinsFamilyReportRepository;

    public LlinsFamilyReportServiceImpl(LlinsFamilyReportRepository llinsFamilyReportRepository) {
        this.llinsFamilyReportRepository = llinsFamilyReportRepository;
    }

    @Override
    public LlinsFamilyReport save(LlinsFamilyReport llinsFamilyReport) {
        log.debug("Request to save LlinsFamilyReport : {}", llinsFamilyReport);
        return llinsFamilyReportRepository.save(llinsFamilyReport);
    }

    @Override
    public Optional<LlinsFamilyReport> partialUpdate(LlinsFamilyReport llinsFamilyReport) {
        log.debug("Request to partially update LlinsFamilyReport : {}", llinsFamilyReport);

        return llinsFamilyReportRepository
            .findById(llinsFamilyReport.getId())
            .map(
                existingLlinsFamilyReport -> {
                    if (llinsFamilyReport.getUid() != null) {
                        existingLlinsFamilyReport.setUid(llinsFamilyReport.getUid());
                    }
                    if (llinsFamilyReport.getCreated() != null) {
                        existingLlinsFamilyReport.setCreated(llinsFamilyReport.getCreated());
                    }
                    if (llinsFamilyReport.getLastUpdated() != null) {
                        existingLlinsFamilyReport.setLastUpdated(llinsFamilyReport.getLastUpdated());
                    }
                    if (llinsFamilyReport.getCheckNo() != null) {
                        existingLlinsFamilyReport.setCheckNo(llinsFamilyReport.getCheckNo());
                    }
                    if (llinsFamilyReport.getMaleIndividuals() != null) {
                        existingLlinsFamilyReport.setMaleIndividuals(llinsFamilyReport.getMaleIndividuals());
                    }
                    if (llinsFamilyReport.getFemaleIndividuals() != null) {
                        existingLlinsFamilyReport.setFemaleIndividuals(llinsFamilyReport.getFemaleIndividuals());
                    }
                    if (llinsFamilyReport.getLessThan5Males() != null) {
                        existingLlinsFamilyReport.setLessThan5Males(llinsFamilyReport.getLessThan5Males());
                    }
                    if (llinsFamilyReport.getLessThan5Females() != null) {
                        existingLlinsFamilyReport.setLessThan5Females(llinsFamilyReport.getLessThan5Females());
                    }
                    if (llinsFamilyReport.getPregnantWomen() != null) {
                        existingLlinsFamilyReport.setPregnantWomen(llinsFamilyReport.getPregnantWomen());
                    }
                    if (llinsFamilyReport.getQuantityReceived() != null) {
                        existingLlinsFamilyReport.setQuantityReceived(llinsFamilyReport.getQuantityReceived());
                    }
                    if (llinsFamilyReport.getFamilyType() != null) {
                        existingLlinsFamilyReport.setFamilyType(llinsFamilyReport.getFamilyType());
                    }
                    if (llinsFamilyReport.getComment() != null) {
                        existingLlinsFamilyReport.setComment(llinsFamilyReport.getComment());
                    }

                    return existingLlinsFamilyReport;
                }
            )
            .map(llinsFamilyReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LlinsFamilyReport> findAll(Pageable pageable) {
        log.debug("Request to get all LlinsFamilyReports");
        return llinsFamilyReportRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsFamilyReport> findOne(Long id) {
        log.debug("Request to get LlinsFamilyReport : {}", id);
        return llinsFamilyReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsFamilyReport : {}", id);
        llinsFamilyReportRepository.deleteById(id);
    }
}
