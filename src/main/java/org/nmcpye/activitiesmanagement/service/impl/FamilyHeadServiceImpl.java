package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.FamilyHead;
import org.nmcpye.activitiesmanagement.repository.FamilyHeadRepository;
import org.nmcpye.activitiesmanagement.service.FamilyHeadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FamilyHead}.
 */
@Service
@Transactional
public class FamilyHeadServiceImpl implements FamilyHeadService {

    private final Logger log = LoggerFactory.getLogger(FamilyHeadServiceImpl.class);

    private final FamilyHeadRepository familyHeadRepository;

    public FamilyHeadServiceImpl(FamilyHeadRepository familyHeadRepository) {
        this.familyHeadRepository = familyHeadRepository;
    }

    @Override
    public FamilyHead save(FamilyHead familyHead) {
        log.debug("Request to save FamilyHead : {}", familyHead);
        return familyHeadRepository.save(familyHead);
    }

    @Override
    public Optional<FamilyHead> partialUpdate(FamilyHead familyHead) {
        log.debug("Request to partially update FamilyHead : {}", familyHead);

        return familyHeadRepository
            .findById(familyHead.getId())
            .map(
                existingFamilyHead -> {
                    if (familyHead.getUid() != null) {
                        existingFamilyHead.setUid(familyHead.getUid());
                    }
                    if (familyHead.getCode() != null) {
                        existingFamilyHead.setCode(familyHead.getCode());
                    }
                    if (familyHead.getName() != null) {
                        existingFamilyHead.setName(familyHead.getName());
                    }
                    if (familyHead.getDescription() != null) {
                        existingFamilyHead.setDescription(familyHead.getDescription());
                    }
                    if (familyHead.getCreated() != null) {
                        existingFamilyHead.setCreated(familyHead.getCreated());
                    }
                    if (familyHead.getLastUpdated() != null) {
                        existingFamilyHead.setLastUpdated(familyHead.getLastUpdated());
                    }
                    if (familyHead.getMobile() != null) {
                        existingFamilyHead.setMobile(familyHead.getMobile());
                    }

                    return existingFamilyHead;
                }
            )
            .map(familyHeadRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FamilyHead> findAll(Pageable pageable) {
        log.debug("Request to get all FamilyHeads");
        return familyHeadRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FamilyHead> findOne(Long id) {
        log.debug("Request to get FamilyHead : {}", id);
        return familyHeadRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FamilyHead : {}", id);
        familyHeadRepository.deleteById(id);
    }
}
