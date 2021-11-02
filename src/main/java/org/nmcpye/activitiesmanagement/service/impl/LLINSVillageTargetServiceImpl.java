package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget;
import org.nmcpye.activitiesmanagement.repository.LLINSVillageTargetRepository;
import org.nmcpye.activitiesmanagement.service.LLINSVillageTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LLINSVillageTarget}.
 */
@Service
@Transactional
public class LLINSVillageTargetServiceImpl implements LLINSVillageTargetService {

    private final Logger log = LoggerFactory.getLogger(LLINSVillageTargetServiceImpl.class);

    private final LLINSVillageTargetRepository lLINSVillageTargetRepository;

    public LLINSVillageTargetServiceImpl(LLINSVillageTargetRepository lLINSVillageTargetRepository) {
        this.lLINSVillageTargetRepository = lLINSVillageTargetRepository;
    }

    @Override
    public LLINSVillageTarget save(LLINSVillageTarget lLINSVillageTarget) {
        log.debug("Request to save LLINSVillageTarget : {}", lLINSVillageTarget);
        return lLINSVillageTargetRepository.save(lLINSVillageTarget);
    }

    @Override
    public Optional<LLINSVillageTarget> partialUpdate(LLINSVillageTarget lLINSVillageTarget) {
        log.debug("Request to partially update LLINSVillageTarget : {}", lLINSVillageTarget);

        return lLINSVillageTargetRepository
            .findById(lLINSVillageTarget.getId())
            .map(
                existingLLINSVillageTarget -> {
                    if (lLINSVillageTarget.getUid() != null) {
                        existingLLINSVillageTarget.setUid(lLINSVillageTarget.getUid());
                    }
                    if (lLINSVillageTarget.getCreated() != null) {
                        existingLLINSVillageTarget.setCreated(lLINSVillageTarget.getCreated());
                    }
                    if (lLINSVillageTarget.getLastUpdated() != null) {
                        existingLLINSVillageTarget.setLastUpdated(lLINSVillageTarget.getLastUpdated());
                    }
                    if (lLINSVillageTarget.getResidentsIndividuals() != null) {
                        existingLLINSVillageTarget.setResidentsIndividuals(lLINSVillageTarget.getResidentsIndividuals());
                    }
                    if (lLINSVillageTarget.getIdpsIndividuals() != null) {
                        existingLLINSVillageTarget.setIdpsIndividuals(lLINSVillageTarget.getIdpsIndividuals());
                    }
                    if (lLINSVillageTarget.getResidentsFamilies() != null) {
                        existingLLINSVillageTarget.setResidentsFamilies(lLINSVillageTarget.getResidentsFamilies());
                    }
                    if (lLINSVillageTarget.getIdpsFamilies() != null) {
                        existingLLINSVillageTarget.setIdpsFamilies(lLINSVillageTarget.getIdpsFamilies());
                    }
                    if (lLINSVillageTarget.getNoOfDaysNeeded() != null) {
                        existingLLINSVillageTarget.setNoOfDaysNeeded(lLINSVillageTarget.getNoOfDaysNeeded());
                    }
                    if (lLINSVillageTarget.getQuantity() != null) {
                        existingLLINSVillageTarget.setQuantity(lLINSVillageTarget.getQuantity());
                    }

                    return existingLLINSVillageTarget;
                }
            )
            .map(lLINSVillageTargetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LLINSVillageTarget> findAll(Pageable pageable) {
        log.debug("Request to get all LLINSVillageTargets");
        return lLINSVillageTargetRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LLINSVillageTarget> findOne(Long id) {
        log.debug("Request to get LLINSVillageTarget : {}", id);
        return lLINSVillageTargetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LLINSVillageTarget : {}", id);
        lLINSVillageTargetRepository.deleteById(id);
    }
}
