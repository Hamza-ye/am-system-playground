package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Family;
import org.nmcpye.activitiesmanagement.repository.FamilyRepository;
import org.nmcpye.activitiesmanagement.service.FamilyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Family}.
 */
@Service
@Transactional
public class FamilyServiceImpl implements FamilyService {

    private final Logger log = LoggerFactory.getLogger(FamilyServiceImpl.class);

    private final FamilyRepository familyRepository;

    public FamilyServiceImpl(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    @Override
    public Family save(Family family) {
        log.debug("Request to save Family : {}", family);
        return familyRepository.save(family);
    }

    @Override
    public Optional<Family> partialUpdate(Family family) {
        log.debug("Request to partially update Family : {}", family);

        return familyRepository
            .findById(family.getId())
            .map(
                existingFamily -> {
                    if (family.getUid() != null) {
                        existingFamily.setUid(family.getUid());
                    }
                    if (family.getCode() != null) {
                        existingFamily.setCode(family.getCode());
                    }
                    if (family.getName() != null) {
                        existingFamily.setName(family.getName());
                    }
                    if (family.getCreated() != null) {
                        existingFamily.setCreated(family.getCreated());
                    }
                    if (family.getLastUpdated() != null) {
                        existingFamily.setLastUpdated(family.getLastUpdated());
                    }
                    if (family.getFamilyNo() != null) {
                        existingFamily.setFamilyNo(family.getFamilyNo());
                    }
                    if (family.getAddress() != null) {
                        existingFamily.setAddress(family.getAddress());
                    }

                    return existingFamily;
                }
            )
            .map(familyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Family> findAll(Pageable pageable) {
        log.debug("Request to get all Families");
        return familyRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Family> findOne(Long id) {
        log.debug("Request to get Family : {}", id);
        return familyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Family : {}", id);
        familyRepository.deleteById(id);
    }
}
