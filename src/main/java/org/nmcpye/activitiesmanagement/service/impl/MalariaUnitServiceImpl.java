package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.MalariaUnit;
import org.nmcpye.activitiesmanagement.repository.MalariaUnitRepository;
import org.nmcpye.activitiesmanagement.service.MalariaUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MalariaUnit}.
 */
@Service
@Transactional
public class MalariaUnitServiceImpl implements MalariaUnitService {

    private final Logger log = LoggerFactory.getLogger(MalariaUnitServiceImpl.class);

    private final MalariaUnitRepository malariaUnitRepository;

    public MalariaUnitServiceImpl(MalariaUnitRepository malariaUnitRepository) {
        this.malariaUnitRepository = malariaUnitRepository;
    }

    @Override
    public MalariaUnit save(MalariaUnit malariaUnit) {
        log.debug("Request to save MalariaUnit : {}", malariaUnit);
        return malariaUnitRepository.save(malariaUnit);
    }

    @Override
    public Optional<MalariaUnit> partialUpdate(MalariaUnit malariaUnit) {
        log.debug("Request to partially update MalariaUnit : {}", malariaUnit);

        return malariaUnitRepository
            .findById(malariaUnit.getId())
            .map(
                existingMalariaUnit -> {
                    if (malariaUnit.getUid() != null) {
                        existingMalariaUnit.setUid(malariaUnit.getUid());
                    }
                    if (malariaUnit.getCode() != null) {
                        existingMalariaUnit.setCode(malariaUnit.getCode());
                    }
                    if (malariaUnit.getName() != null) {
                        existingMalariaUnit.setName(malariaUnit.getName());
                    }
                    if (malariaUnit.getShortName() != null) {
                        existingMalariaUnit.setShortName(malariaUnit.getShortName());
                    }
                    if (malariaUnit.getDescription() != null) {
                        existingMalariaUnit.setDescription(malariaUnit.getDescription());
                    }
                    if (malariaUnit.getCreated() != null) {
                        existingMalariaUnit.setCreated(malariaUnit.getCreated());
                    }
                    if (malariaUnit.getLastUpdated() != null) {
                        existingMalariaUnit.setLastUpdated(malariaUnit.getLastUpdated());
                    }

                    return existingMalariaUnit;
                }
            )
            .map(malariaUnitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MalariaUnit> findAll() {
        log.debug("Request to get all MalariaUnits");
        return malariaUnitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MalariaUnit> findOne(Long id) {
        log.debug("Request to get MalariaUnit : {}", id);
        return malariaUnitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MalariaUnit : {}", id);
        malariaUnitRepository.deleteById(id);
    }
}
