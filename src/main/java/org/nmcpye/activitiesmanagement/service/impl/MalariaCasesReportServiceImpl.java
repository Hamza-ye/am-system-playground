package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.repository.MalariaCasesReportRepository;
import org.nmcpye.activitiesmanagement.service.MalariaCasesReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MalariaCasesReport}.
 */
@Service
@Transactional
public class MalariaCasesReportServiceImpl implements MalariaCasesReportService {

    private final Logger log = LoggerFactory.getLogger(MalariaCasesReportServiceImpl.class);

    private final MalariaCasesReportRepository malariaCasesReportRepository;

    public MalariaCasesReportServiceImpl(MalariaCasesReportRepository malariaCasesReportRepository) {
        this.malariaCasesReportRepository = malariaCasesReportRepository;
    }

    @Override
    public MalariaCasesReport save(MalariaCasesReport malariaCasesReport) {
        log.debug("Request to save MalariaCasesReport : {}", malariaCasesReport);
        return malariaCasesReportRepository.save(malariaCasesReport);
    }

    @Override
    public Optional<MalariaCasesReport> partialUpdate(MalariaCasesReport malariaCasesReport) {
        log.debug("Request to partially update MalariaCasesReport : {}", malariaCasesReport);

        return malariaCasesReportRepository
            .findById(malariaCasesReport.getId())
            .map(
                existingMalariaCasesReport -> {
                    if (malariaCasesReport.getUid() != null) {
                        existingMalariaCasesReport.setUid(malariaCasesReport.getUid());
                    }
                    if (malariaCasesReport.getCreated() != null) {
                        existingMalariaCasesReport.setCreated(malariaCasesReport.getCreated());
                    }
                    if (malariaCasesReport.getLastUpdated() != null) {
                        existingMalariaCasesReport.setLastUpdated(malariaCasesReport.getLastUpdated());
                    }
                    if (malariaCasesReport.getRdtTested() != null) {
                        existingMalariaCasesReport.setRdtTested(malariaCasesReport.getRdtTested());
                    }
                    if (malariaCasesReport.getRdtPositive() != null) {
                        existingMalariaCasesReport.setRdtPositive(malariaCasesReport.getRdtPositive());
                    }
                    if (malariaCasesReport.getRdtPf() != null) {
                        existingMalariaCasesReport.setRdtPf(malariaCasesReport.getRdtPf());
                    }
                    if (malariaCasesReport.getRdtPv() != null) {
                        existingMalariaCasesReport.setRdtPv(malariaCasesReport.getRdtPv());
                    }
                    if (malariaCasesReport.getRdtPother() != null) {
                        existingMalariaCasesReport.setRdtPother(malariaCasesReport.getRdtPother());
                    }
                    if (malariaCasesReport.getMicroTested() != null) {
                        existingMalariaCasesReport.setMicroTested(malariaCasesReport.getMicroTested());
                    }
                    if (malariaCasesReport.getMicroPositive() != null) {
                        existingMalariaCasesReport.setMicroPositive(malariaCasesReport.getMicroPositive());
                    }
                    if (malariaCasesReport.getMicroPf() != null) {
                        existingMalariaCasesReport.setMicroPf(malariaCasesReport.getMicroPf());
                    }
                    if (malariaCasesReport.getMicroPv() != null) {
                        existingMalariaCasesReport.setMicroPv(malariaCasesReport.getMicroPv());
                    }
                    if (malariaCasesReport.getMicroMix() != null) {
                        existingMalariaCasesReport.setMicroMix(malariaCasesReport.getMicroMix());
                    }
                    if (malariaCasesReport.getMicroPother() != null) {
                        existingMalariaCasesReport.setMicroPother(malariaCasesReport.getMicroPother());
                    }
                    if (malariaCasesReport.getProbableCases() != null) {
                        existingMalariaCasesReport.setProbableCases(malariaCasesReport.getProbableCases());
                    }
                    if (malariaCasesReport.getInpatientCases() != null) {
                        existingMalariaCasesReport.setInpatientCases(malariaCasesReport.getInpatientCases());
                    }
                    if (malariaCasesReport.getDeathCases() != null) {
                        existingMalariaCasesReport.setDeathCases(malariaCasesReport.getDeathCases());
                    }
                    if (malariaCasesReport.getTreated() != null) {
                        existingMalariaCasesReport.setTreated(malariaCasesReport.getTreated());
                    }
                    if (malariaCasesReport.getSuspectedCases() != null) {
                        existingMalariaCasesReport.setSuspectedCases(malariaCasesReport.getSuspectedCases());
                    }
                    if (malariaCasesReport.getTotalFrequents() != null) {
                        existingMalariaCasesReport.setTotalFrequents(malariaCasesReport.getTotalFrequents());
                    }
                    if (malariaCasesReport.getComment() != null) {
                        existingMalariaCasesReport.setComment(malariaCasesReport.getComment());
                    }

                    return existingMalariaCasesReport;
                }
            )
            .map(malariaCasesReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MalariaCasesReport> findAll() {
        log.debug("Request to get all MalariaCasesReports");
        return malariaCasesReportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MalariaCasesReport> findOne(Long id) {
        log.debug("Request to get MalariaCasesReport : {}", id);
        return malariaCasesReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MalariaCasesReport : {}", id);
        malariaCasesReportRepository.deleteById(id);
    }
}
