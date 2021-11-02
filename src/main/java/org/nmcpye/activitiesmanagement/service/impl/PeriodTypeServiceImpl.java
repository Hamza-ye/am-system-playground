package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.PeriodType;
import org.nmcpye.activitiesmanagement.repository.PeriodTypeRepository;
import org.nmcpye.activitiesmanagement.service.PeriodTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PeriodType}.
 */
@Service
@Transactional
public class PeriodTypeServiceImpl implements PeriodTypeService {

    private final Logger log = LoggerFactory.getLogger(PeriodTypeServiceImpl.class);

    private final PeriodTypeRepository periodTypeRepository;

    public PeriodTypeServiceImpl(PeriodTypeRepository periodTypeRepository) {
        this.periodTypeRepository = periodTypeRepository;
    }

    @Override
    public PeriodType save(PeriodType periodType) {
        log.debug("Request to save PeriodType : {}", periodType);
        return periodTypeRepository.save(periodType);
    }

    @Override
    public Optional<PeriodType> partialUpdate(PeriodType periodType) {
        log.debug("Request to partially update PeriodType : {}", periodType);

        return periodTypeRepository
            .findById(periodType.getId())
            .map(
                existingPeriodType -> {
                    if (periodType.getName() != null) {
                        existingPeriodType.setName(periodType.getName());
                    }

                    return existingPeriodType;
                }
            )
            .map(periodTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeriodType> findAll() {
        log.debug("Request to get all PeriodTypes");
        return periodTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PeriodType> findOne(Long id) {
        log.debug("Request to get PeriodType : {}", id);
        return periodTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PeriodType : {}", id);
        periodTypeRepository.deleteById(id);
    }
}
