package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyTargetRepository;
import org.nmcpye.activitiesmanagement.service.LlinsFamilyTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LlinsFamilyTarget}.
 */
@Service
@Transactional
public class LlinsFamilyTargetServiceImpl implements LlinsFamilyTargetService {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyTargetServiceImpl.class);

    private final LlinsFamilyTargetRepository llinsFamilyTargetRepository;

    public LlinsFamilyTargetServiceImpl(LlinsFamilyTargetRepository llinsFamilyTargetRepository) {
        this.llinsFamilyTargetRepository = llinsFamilyTargetRepository;
    }

    @Override
    public LlinsFamilyTarget save(LlinsFamilyTarget llinsFamilyTarget) {
        log.debug("Request to save LlinsFamilyTarget : {}", llinsFamilyTarget);
        return llinsFamilyTargetRepository.save(llinsFamilyTarget);
    }

    @Override
    public Optional<LlinsFamilyTarget> partialUpdate(LlinsFamilyTarget llinsFamilyTarget) {
        log.debug("Request to partially update LlinsFamilyTarget : {}", llinsFamilyTarget);

        return llinsFamilyTargetRepository
            .findById(llinsFamilyTarget.getId())
            .map(
                existingLlinsFamilyTarget -> {
                    if (llinsFamilyTarget.getUid() != null) {
                        existingLlinsFamilyTarget.setUid(llinsFamilyTarget.getUid());
                    }
                    if (llinsFamilyTarget.getCreated() != null) {
                        existingLlinsFamilyTarget.setCreated(llinsFamilyTarget.getCreated());
                    }
                    if (llinsFamilyTarget.getLastUpdated() != null) {
                        existingLlinsFamilyTarget.setLastUpdated(llinsFamilyTarget.getLastUpdated());
                    }
                    if (llinsFamilyTarget.getResidentsIndividualsPlanned() != null) {
                        existingLlinsFamilyTarget.setResidentsIndividualsPlanned(llinsFamilyTarget.getResidentsIndividualsPlanned());
                    }
                    if (llinsFamilyTarget.getIdpsIndividualsPlanned() != null) {
                        existingLlinsFamilyTarget.setIdpsIndividualsPlanned(llinsFamilyTarget.getIdpsIndividualsPlanned());
                    }
                    if (llinsFamilyTarget.getQuantityPlanned() != null) {
                        existingLlinsFamilyTarget.setQuantityPlanned(llinsFamilyTarget.getQuantityPlanned());
                    }
                    if (llinsFamilyTarget.getFamilyType() != null) {
                        existingLlinsFamilyTarget.setFamilyType(llinsFamilyTarget.getFamilyType());
                    }
                    if (llinsFamilyTarget.getStatusOfFamilyTarget() != null) {
                        existingLlinsFamilyTarget.setStatusOfFamilyTarget(llinsFamilyTarget.getStatusOfFamilyTarget());
                    }

                    return existingLlinsFamilyTarget;
                }
            )
            .map(llinsFamilyTargetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LlinsFamilyTarget> findAll(Pageable pageable) {
        log.debug("Request to get all LlinsFamilyTargets");
        return llinsFamilyTargetRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsFamilyTarget> findOne(Long id) {
        log.debug("Request to get LlinsFamilyTarget : {}", id);
        return llinsFamilyTargetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsFamilyTarget : {}", id);
        llinsFamilyTargetRepository.deleteById(id);
    }
}
