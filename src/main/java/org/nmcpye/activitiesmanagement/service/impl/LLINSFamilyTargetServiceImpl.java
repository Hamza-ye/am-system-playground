package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyTarget;
import org.nmcpye.activitiesmanagement.repository.LLINSFamilyTargetRepository;
import org.nmcpye.activitiesmanagement.service.LLINSFamilyTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LLINSFamilyTarget}.
 */
@Service
@Transactional
public class LLINSFamilyTargetServiceImpl implements LLINSFamilyTargetService {

    private final Logger log = LoggerFactory.getLogger(LLINSFamilyTargetServiceImpl.class);

    private final LLINSFamilyTargetRepository lLINSFamilyTargetRepository;

    public LLINSFamilyTargetServiceImpl(LLINSFamilyTargetRepository lLINSFamilyTargetRepository) {
        this.lLINSFamilyTargetRepository = lLINSFamilyTargetRepository;
    }

    @Override
    public LLINSFamilyTarget save(LLINSFamilyTarget lLINSFamilyTarget) {
        log.debug("Request to save LLINSFamilyTarget : {}", lLINSFamilyTarget);
        return lLINSFamilyTargetRepository.save(lLINSFamilyTarget);
    }

    @Override
    public Optional<LLINSFamilyTarget> partialUpdate(LLINSFamilyTarget lLINSFamilyTarget) {
        log.debug("Request to partially update LLINSFamilyTarget : {}", lLINSFamilyTarget);

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
    public Page<LLINSFamilyTarget> findAll(Pageable pageable) {
        log.debug("Request to get all LLINSFamilyTargets");
        return lLINSFamilyTargetRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LLINSFamilyTarget> findOne(Long id) {
        log.debug("Request to get LLINSFamilyTarget : {}", id);
        return lLINSFamilyTargetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LLINSFamilyTarget : {}", id);
        lLINSFamilyTargetRepository.deleteById(id);
    }
}
