package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageTargetRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LlinsVillageTarget}.
 */
@Service
@Transactional
public class LlinsVillageTargetServiceImpl implements LlinsVillageTargetService {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageTargetServiceImpl.class);

    private final LlinsVillageTargetRepository lLINSVillageTargetRepository;

    public LlinsVillageTargetServiceImpl(LlinsVillageTargetRepository lLINSVillageTargetRepository) {
        this.lLINSVillageTargetRepository = lLINSVillageTargetRepository;
    }

    @Override
    public LlinsVillageTarget save(LlinsVillageTarget lLINSVillageTarget) {
        log.debug("Request to save LlinsVillageTarget : {}", lLINSVillageTarget);
        return lLINSVillageTargetRepository.save(lLINSVillageTarget);
    }

    @Override
    public Optional<LlinsVillageTarget> partialUpdate(LlinsVillageTarget lLINSVillageTarget) {
        log.debug("Request to partially update LlinsVillageTarget : {}", lLINSVillageTarget);

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
    public Page<LlinsVillageTarget> findAll(Pageable pageable) {
        log.debug("Request to get all LLINSVillageTargets");
        return lLINSVillageTargetRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsVillageTarget> findOne(Long id) {
        log.debug("Request to get LlinsVillageTarget : {}", id);
        return lLINSVillageTargetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsVillageTarget : {}", id);
        lLINSVillageTargetRepository.deleteById(id);
    }
}
