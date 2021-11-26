package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.nmcpye.activitiesmanagement.repository.LlinsFamilyTargetRepository;
import org.nmcpye.activitiesmanagement.service.LlinsFamilyTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LlinsFamilyTarget}.
 */
@Service
@Transactional
public class LlinsFamilyTargetServiceImpl implements LlinsFamilyTargetService {

    private final Logger log = LoggerFactory.getLogger(LlinsFamilyTargetServiceImpl.class);

    private final LlinsFamilyTargetRepository lLINSFamilyTargetRepository;

    public LlinsFamilyTargetServiceImpl(LlinsFamilyTargetRepository lLINSFamilyTargetRepository) {
        this.lLINSFamilyTargetRepository = lLINSFamilyTargetRepository;
    }

    @Override
    public LlinsFamilyTarget save(LlinsFamilyTarget lLINSFamilyTarget) {
        log.debug("Request to save LlinsFamilyTarget : {}", lLINSFamilyTarget);
        return lLINSFamilyTargetRepository.save(lLINSFamilyTarget);
    }

    @Override
    public Optional<LlinsFamilyTarget> partialUpdate(LlinsFamilyTarget lLINSFamilyTarget) {
        log.debug("Request to partially update LlinsFamilyTarget : {}", lLINSFamilyTarget);

        return lLINSFamilyTargetRepository
            .findById(lLINSFamilyTarget.getId())
            .map(
                existingLLINSFamilyTarget -> {
                    if (lLINSFamilyTarget.getUid() != null) {
                        existingLLINSFamilyTarget.setUid(lLINSFamilyTarget.getUid());
                    }
                    if (lLINSFamilyTarget.getCreated() != null) {
                        existingLLINSFamilyTarget.setCreated(lLINSFamilyTarget.getCreated());
                    }
                    if (lLINSFamilyTarget.getLastUpdated() != null) {
                        existingLLINSFamilyTarget.setLastUpdated(lLINSFamilyTarget.getLastUpdated());
                    }
                    if (lLINSFamilyTarget.getResidentsIndividualsPlanned() != null) {
                        existingLLINSFamilyTarget.setResidentsIndividualsPlanned(lLINSFamilyTarget.getResidentsIndividualsPlanned());
                    }
                    if (lLINSFamilyTarget.getIdpsIndividualsPlanned() != null) {
                        existingLLINSFamilyTarget.setIdpsIndividualsPlanned(lLINSFamilyTarget.getIdpsIndividualsPlanned());
                    }
                    if (lLINSFamilyTarget.getQuantityPlanned() != null) {
                        existingLLINSFamilyTarget.setQuantityPlanned(lLINSFamilyTarget.getQuantityPlanned());
                    }
                    if (lLINSFamilyTarget.getFamilyType() != null) {
                        existingLLINSFamilyTarget.setFamilyType(lLINSFamilyTarget.getFamilyType());
                    }
                    if (lLINSFamilyTarget.getStatusOfFamilyTarget() != null) {
                        existingLLINSFamilyTarget.setStatusOfFamilyTarget(lLINSFamilyTarget.getStatusOfFamilyTarget());
                    }

                    return existingLLINSFamilyTarget;
                }
            )
            .map(lLINSFamilyTargetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LlinsFamilyTarget> findAll(Pageable pageable) {
        log.debug("Request to get all LLINSFamilyTargets");
        return lLINSFamilyTargetRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsFamilyTarget> findOne(Long id) {
        log.debug("Request to get LlinsFamilyTarget : {}", id);
        return lLINSFamilyTargetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsFamilyTarget : {}", id);
        lLINSFamilyTargetRepository.deleteById(id);
    }
}
