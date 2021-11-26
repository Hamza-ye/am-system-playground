package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.nmcpye.activitiesmanagement.repository.LlinsVillageTargetRepository;
import org.nmcpye.activitiesmanagement.service.LlinsVillageTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LlinsVillageTarget}.
 */
@Service
@Transactional
public class LlinsVillageTargetServiceImpl implements LlinsVillageTargetService {

    private final Logger log = LoggerFactory.getLogger(LlinsVillageTargetServiceImpl.class);

    private final LlinsVillageTargetRepository llinsVillageTargetRepository;

    public LlinsVillageTargetServiceImpl(LlinsVillageTargetRepository llinsVillageTargetRepository) {
        this.llinsVillageTargetRepository = llinsVillageTargetRepository;
    }

    @Override
    public LlinsVillageTarget save(LlinsVillageTarget llinsVillageTarget) {
        log.debug("Request to save LlinsVillageTarget : {}", llinsVillageTarget);
        return llinsVillageTargetRepository.save(llinsVillageTarget);
    }

    @Override
    public Optional<LlinsVillageTarget> partialUpdate(LlinsVillageTarget llinsVillageTarget) {
        log.debug("Request to partially update LlinsVillageTarget : {}", llinsVillageTarget);

        return llinsVillageTargetRepository
            .findById(llinsVillageTarget.getId())
            .map(
                existingLlinsVillageTarget -> {
                    if (llinsVillageTarget.getUid() != null) {
                        existingLlinsVillageTarget.setUid(llinsVillageTarget.getUid());
                    }
                    if (llinsVillageTarget.getCreated() != null) {
                        existingLlinsVillageTarget.setCreated(llinsVillageTarget.getCreated());
                    }
                    if (llinsVillageTarget.getLastUpdated() != null) {
                        existingLlinsVillageTarget.setLastUpdated(llinsVillageTarget.getLastUpdated());
                    }
                    if (llinsVillageTarget.getResidentsIndividuals() != null) {
                        existingLlinsVillageTarget.setResidentsIndividuals(llinsVillageTarget.getResidentsIndividuals());
                    }
                    if (llinsVillageTarget.getIdpsIndividuals() != null) {
                        existingLlinsVillageTarget.setIdpsIndividuals(llinsVillageTarget.getIdpsIndividuals());
                    }
                    if (llinsVillageTarget.getResidentsFamilies() != null) {
                        existingLlinsVillageTarget.setResidentsFamilies(llinsVillageTarget.getResidentsFamilies());
                    }
                    if (llinsVillageTarget.getIdpsFamilies() != null) {
                        existingLlinsVillageTarget.setIdpsFamilies(llinsVillageTarget.getIdpsFamilies());
                    }
                    if (llinsVillageTarget.getNoOfDaysNeeded() != null) {
                        existingLlinsVillageTarget.setNoOfDaysNeeded(llinsVillageTarget.getNoOfDaysNeeded());
                    }
                    if (llinsVillageTarget.getQuantity() != null) {
                        existingLlinsVillageTarget.setQuantity(llinsVillageTarget.getQuantity());
                    }

                    return existingLlinsVillageTarget;
                }
            )
            .map(llinsVillageTargetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LlinsVillageTarget> findAll(Pageable pageable) {
        log.debug("Request to get all LlinsVillageTargets");
        return llinsVillageTargetRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LlinsVillageTarget> findOne(Long id) {
        log.debug("Request to get LlinsVillageTarget : {}", id);
        return llinsVillageTargetRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LlinsVillageTarget : {}", id);
        llinsVillageTargetRepository.deleteById(id);
    }
}
