package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;
import org.nmcpye.activitiesmanagement.repository.ChvMalariaCaseReportRepository;
import org.nmcpye.activitiesmanagement.service.ChvMalariaCaseReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChvMalariaCaseReport}.
 */
@Service
@Transactional
public class ChvMalariaCaseReportServiceImpl implements ChvMalariaCaseReportService {

    private final Logger log = LoggerFactory.getLogger(ChvMalariaCaseReportServiceImpl.class);

    private final ChvMalariaCaseReportRepository cHVMalariaCaseReportRepository;

    public ChvMalariaCaseReportServiceImpl(ChvMalariaCaseReportRepository cHVMalariaCaseReportRepository) {
        this.cHVMalariaCaseReportRepository = cHVMalariaCaseReportRepository;
    }

    @Override
    public ChvMalariaCaseReport save(ChvMalariaCaseReport cHVMalariaCaseReport) {
        log.debug("Request to save ChvMalariaCaseReport : {}", cHVMalariaCaseReport);
        return cHVMalariaCaseReportRepository.save(cHVMalariaCaseReport);
    }

    @Override
    public Optional<ChvMalariaCaseReport> partialUpdate(ChvMalariaCaseReport cHVMalariaCaseReport) {
        log.debug("Request to partially update ChvMalariaCaseReport : {}", cHVMalariaCaseReport);

        return cHVMalariaCaseReportRepository
            .findById(cHVMalariaCaseReport.getId())
            .map(
                existingCHVMalariaCaseReport -> {
                    if (cHVMalariaCaseReport.getUid() != null) {
                        existingCHVMalariaCaseReport.setUid(cHVMalariaCaseReport.getUid());
                    }
                    if (cHVMalariaCaseReport.getCreated() != null) {
                        existingCHVMalariaCaseReport.setCreated(cHVMalariaCaseReport.getCreated());
                    }
                    if (cHVMalariaCaseReport.getLastUpdated() != null) {
                        existingCHVMalariaCaseReport.setLastUpdated(cHVMalariaCaseReport.getLastUpdated());
                    }
                    if (cHVMalariaCaseReport.getDate() != null) {
                        existingCHVMalariaCaseReport.setDate(cHVMalariaCaseReport.getDate());
                    }
                    if (cHVMalariaCaseReport.getIndividualName() != null) {
                        existingCHVMalariaCaseReport.setIndividualName(cHVMalariaCaseReport.getIndividualName());
                    }
                    if (cHVMalariaCaseReport.getGender() != null) {
                        existingCHVMalariaCaseReport.setGender(cHVMalariaCaseReport.getGender());
                    }
                    if (cHVMalariaCaseReport.getIsPregnant() != null) {
                        existingCHVMalariaCaseReport.setIsPregnant(cHVMalariaCaseReport.getIsPregnant());
                    }
                    if (cHVMalariaCaseReport.getMalariaTestResult() != null) {
                        existingCHVMalariaCaseReport.setMalariaTestResult(cHVMalariaCaseReport.getMalariaTestResult());
                    }
                    if (cHVMalariaCaseReport.getDrugsGiven() != null) {
                        existingCHVMalariaCaseReport.setDrugsGiven(cHVMalariaCaseReport.getDrugsGiven());
                    }
                    if (cHVMalariaCaseReport.getSuppsGiven() != null) {
                        existingCHVMalariaCaseReport.setSuppsGiven(cHVMalariaCaseReport.getSuppsGiven());
                    }
                    if (cHVMalariaCaseReport.getReferral() != null) {
                        existingCHVMalariaCaseReport.setReferral(cHVMalariaCaseReport.getReferral());
                    }
                    if (cHVMalariaCaseReport.getBarImageUrl() != null) {
                        existingCHVMalariaCaseReport.setBarImageUrl(cHVMalariaCaseReport.getBarImageUrl());
                    }
                    if (cHVMalariaCaseReport.getComment() != null) {
                        existingCHVMalariaCaseReport.setComment(cHVMalariaCaseReport.getComment());
                    }

                    return existingCHVMalariaCaseReport;
                }
            )
            .map(cHVMalariaCaseReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChvMalariaCaseReport> findAll() {
        log.debug("Request to get all CHVMalariaCaseReports");
        return cHVMalariaCaseReportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChvMalariaCaseReport> findOne(Long id) {
        log.debug("Request to get ChvMalariaCaseReport : {}", id);
        return cHVMalariaCaseReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChvMalariaCaseReport : {}", id);
        cHVMalariaCaseReportRepository.deleteById(id);
    }
}
