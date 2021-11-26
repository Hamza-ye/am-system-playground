package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaCaseReport;
import org.nmcpye.activitiesmanagement.repository.ChvMalariaCaseReportRepository;
import org.nmcpye.activitiesmanagement.service.ChvMalariaCaseReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ChvMalariaCaseReport}.
 */
@Service
@Transactional
public class ChvMalariaCaseReportServiceImpl implements ChvMalariaCaseReportService {

    private final Logger log = LoggerFactory.getLogger(ChvMalariaCaseReportServiceImpl.class);

    private final ChvMalariaCaseReportRepository chvMalariaCaseReportRepository;

    public ChvMalariaCaseReportServiceImpl(ChvMalariaCaseReportRepository chvMalariaCaseReportRepository) {
        this.chvMalariaCaseReportRepository = chvMalariaCaseReportRepository;
    }

    @Override
    public ChvMalariaCaseReport save(ChvMalariaCaseReport chvMalariaCaseReport) {
        log.debug("Request to save ChvMalariaCaseReport : {}", chvMalariaCaseReport);
        return chvMalariaCaseReportRepository.save(chvMalariaCaseReport);
    }

    @Override
    public Optional<ChvMalariaCaseReport> partialUpdate(ChvMalariaCaseReport chvMalariaCaseReport) {
        log.debug("Request to partially update ChvMalariaCaseReport : {}", chvMalariaCaseReport);

        return chvMalariaCaseReportRepository
            .findById(chvMalariaCaseReport.getId())
            .map(
                existingChvMalariaCaseReport -> {
                    if (chvMalariaCaseReport.getUid() != null) {
                        existingChvMalariaCaseReport.setUid(chvMalariaCaseReport.getUid());
                    }
                    if (chvMalariaCaseReport.getCreated() != null) {
                        existingChvMalariaCaseReport.setCreated(chvMalariaCaseReport.getCreated());
                    }
                    if (chvMalariaCaseReport.getLastUpdated() != null) {
                        existingChvMalariaCaseReport.setLastUpdated(chvMalariaCaseReport.getLastUpdated());
                    }
                    if (chvMalariaCaseReport.getDate() != null) {
                        existingChvMalariaCaseReport.setDate(chvMalariaCaseReport.getDate());
                    }
                    if (chvMalariaCaseReport.getIndividualName() != null) {
                        existingChvMalariaCaseReport.setIndividualName(chvMalariaCaseReport.getIndividualName());
                    }
                    if (chvMalariaCaseReport.getGender() != null) {
                        existingChvMalariaCaseReport.setGender(chvMalariaCaseReport.getGender());
                    }
                    if (chvMalariaCaseReport.getIsPregnant() != null) {
                        existingChvMalariaCaseReport.setIsPregnant(chvMalariaCaseReport.getIsPregnant());
                    }
                    if (chvMalariaCaseReport.getMalariaTestResult() != null) {
                        existingChvMalariaCaseReport.setMalariaTestResult(chvMalariaCaseReport.getMalariaTestResult());
                    }
                    if (chvMalariaCaseReport.getDrugsGiven() != null) {
                        existingChvMalariaCaseReport.setDrugsGiven(chvMalariaCaseReport.getDrugsGiven());
                    }
                    if (chvMalariaCaseReport.getSuppsGiven() != null) {
                        existingChvMalariaCaseReport.setSuppsGiven(chvMalariaCaseReport.getSuppsGiven());
                    }
                    if (chvMalariaCaseReport.getReferral() != null) {
                        existingChvMalariaCaseReport.setReferral(chvMalariaCaseReport.getReferral());
                    }
                    if (chvMalariaCaseReport.getBarImageUrl() != null) {
                        existingChvMalariaCaseReport.setBarImageUrl(chvMalariaCaseReport.getBarImageUrl());
                    }
                    if (chvMalariaCaseReport.getComment() != null) {
                        existingChvMalariaCaseReport.setComment(chvMalariaCaseReport.getComment());
                    }

                    return existingChvMalariaCaseReport;
                }
            )
            .map(chvMalariaCaseReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChvMalariaCaseReport> findAll() {
        log.debug("Request to get all ChvMalariaCaseReports");
        return chvMalariaCaseReportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChvMalariaCaseReport> findOne(Long id) {
        log.debug("Request to get ChvMalariaCaseReport : {}", id);
        return chvMalariaCaseReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChvMalariaCaseReport : {}", id);
        chvMalariaCaseReportRepository.deleteById(id);
    }
}
