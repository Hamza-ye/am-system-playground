package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Period;
import org.nmcpye.activitiesmanagement.repository.PeriodRepository;
import org.nmcpye.activitiesmanagement.service.PeriodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Period}.
 */
@Service
@Transactional
public class PeriodServiceImpl implements PeriodService {

    private final Logger log = LoggerFactory.getLogger(PeriodServiceImpl.class);

    private final PeriodRepository periodRepository;

    public PeriodServiceImpl(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    @Override
    public Period save(Period period) {
        log.debug("Request to save Period : {}", period);
        return periodRepository.save(period);
    }

    @Override
    public Optional<Period> partialUpdate(Period period) {
        log.debug("Request to partially update Period : {}", period);

        return periodRepository
            .findById(period.getId())
            .map(
                existingPeriod -> {
                    if (period.getName() != null) {
                        existingPeriod.setName(period.getName());
                    }
                    if (period.getStartDate() != null) {
                        existingPeriod.setStartDate(period.getStartDate());
                    }
                    if (period.getEndDate() != null) {
                        existingPeriod.setEndDate(period.getEndDate());
                    }

                    return existingPeriod;
                }
            )
            .map(periodRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> findAll() {
        log.debug("Request to get all Periods");
        return periodRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Period> findOne(Long id) {
        log.debug("Request to get Period : {}", id);
        return periodRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Period : {}", id);
        periodRepository.deleteById(id);
    }
}
