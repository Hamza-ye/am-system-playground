package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CasesReportClass;
import org.nmcpye.activitiesmanagement.repository.CasesReportClassRepository;
import org.nmcpye.activitiesmanagement.service.CasesReportClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CasesReportClass}.
 */
@Service
@Transactional
public class CasesReportClassServiceImpl implements CasesReportClassService {

    private final Logger log = LoggerFactory.getLogger(CasesReportClassServiceImpl.class);

    private final CasesReportClassRepository casesReportClassRepository;

    public CasesReportClassServiceImpl(CasesReportClassRepository casesReportClassRepository) {
        this.casesReportClassRepository = casesReportClassRepository;
    }

    @Override
    public CasesReportClass save(CasesReportClass casesReportClass) {
        log.debug("Request to save CasesReportClass : {}", casesReportClass);
        return casesReportClassRepository.save(casesReportClass);
    }

    @Override
    public Optional<CasesReportClass> partialUpdate(CasesReportClass casesReportClass) {
        log.debug("Request to partially update CasesReportClass : {}", casesReportClass);

        return casesReportClassRepository
            .findById(casesReportClass.getId())
            .map(
                existingCasesReportClass -> {
                    if (casesReportClass.getUid() != null) {
                        existingCasesReportClass.setUid(casesReportClass.getUid());
                    }
                    if (casesReportClass.getCode() != null) {
                        existingCasesReportClass.setCode(casesReportClass.getCode());
                    }
                    if (casesReportClass.getName() != null) {
                        existingCasesReportClass.setName(casesReportClass.getName());
                    }
                    if (casesReportClass.getShortName() != null) {
                        existingCasesReportClass.setShortName(casesReportClass.getShortName());
                    }
                    if (casesReportClass.getDescription() != null) {
                        existingCasesReportClass.setDescription(casesReportClass.getDescription());
                    }
                    if (casesReportClass.getCreated() != null) {
                        existingCasesReportClass.setCreated(casesReportClass.getCreated());
                    }
                    if (casesReportClass.getLastUpdated() != null) {
                        existingCasesReportClass.setLastUpdated(casesReportClass.getLastUpdated());
                    }

                    return existingCasesReportClass;
                }
            )
            .map(casesReportClassRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasesReportClass> findAll() {
        log.debug("Request to get all CasesReportClasses");
        return casesReportClassRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CasesReportClass> findOne(Long id) {
        log.debug("Request to get CasesReportClass : {}", id);
        return casesReportClassRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CasesReportClass : {}", id);
        casesReportClassRepository.deleteById(id);
    }
}
