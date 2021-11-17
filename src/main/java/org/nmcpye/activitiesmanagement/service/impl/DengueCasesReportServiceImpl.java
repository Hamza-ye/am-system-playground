package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.repository.DengueCasesReportRepository;
import org.nmcpye.activitiesmanagement.service.DengueCasesReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DengueCasesReport}.
 */
@Service
@Transactional
public class DengueCasesReportServiceImpl implements DengueCasesReportService {

    private final Logger log = LoggerFactory.getLogger(DengueCasesReportServiceImpl.class);

    private final DengueCasesReportRepository dengueCasesReportRepository;

    public DengueCasesReportServiceImpl(DengueCasesReportRepository dengueCasesReportRepository) {
        this.dengueCasesReportRepository = dengueCasesReportRepository;
    }

    @Override
    public DengueCasesReport save(DengueCasesReport dengueCasesReport) {
        log.debug("Request to save DengueCasesReport : {}", dengueCasesReport);
        return dengueCasesReportRepository.save(dengueCasesReport);
    }

    @Override
    public Optional<DengueCasesReport> partialUpdate(DengueCasesReport dengueCasesReport) {
        log.debug("Request to partially update DengueCasesReport : {}", dengueCasesReport);

        return dengueCasesReportRepository
            .findById(dengueCasesReport.getId())
            .map(
                existingDengueCasesReport -> {
                    if (dengueCasesReport.getUid() != null) {
                        existingDengueCasesReport.setUid(dengueCasesReport.getUid());
                    }
                    if (dengueCasesReport.getCreated() != null) {
                        existingDengueCasesReport.setCreated(dengueCasesReport.getCreated());
                    }
                    if (dengueCasesReport.getLastUpdated() != null) {
                        existingDengueCasesReport.setLastUpdated(dengueCasesReport.getLastUpdated());
                    }
                    if (dengueCasesReport.getRdtTested() != null) {
                        existingDengueCasesReport.setRdtTested(dengueCasesReport.getRdtTested());
                    }
                    if (dengueCasesReport.getRdtPositive() != null) {
                        existingDengueCasesReport.setRdtPositive(dengueCasesReport.getRdtPositive());
                    }
                    if (dengueCasesReport.getProbableCases() != null) {
                        existingDengueCasesReport.setProbableCases(dengueCasesReport.getProbableCases());
                    }
                    if (dengueCasesReport.getInpatientCases() != null) {
                        existingDengueCasesReport.setInpatientCases(dengueCasesReport.getInpatientCases());
                    }
                    if (dengueCasesReport.getDeathCases() != null) {
                        existingDengueCasesReport.setDeathCases(dengueCasesReport.getDeathCases());
                    }
                    if (dengueCasesReport.getTreated() != null) {
                        existingDengueCasesReport.setTreated(dengueCasesReport.getTreated());
                    }
                    if (dengueCasesReport.getSuspectedCases() != null) {
                        existingDengueCasesReport.setSuspectedCases(dengueCasesReport.getSuspectedCases());
                    }
                    if (dengueCasesReport.getComment() != null) {
                        existingDengueCasesReport.setComment(dengueCasesReport.getComment());
                    }

                    return existingDengueCasesReport;
                }
            )
            .map(dengueCasesReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DengueCasesReport> findAll() {
        log.debug("Request to get all DengueCasesReports");
        return dengueCasesReportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DengueCasesReport> findOne(Long id) {
        log.debug("Request to get DengueCasesReport : {}", id);
        return dengueCasesReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DengueCasesReport : {}", id);
        dengueCasesReportRepository.deleteById(id);
    }
}
