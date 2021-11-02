package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.nmcpye.activitiesmanagement.repository.WorkingDayRepository;
import org.nmcpye.activitiesmanagement.service.WorkingDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkingDay}.
 */
@Service
@Transactional
public class WorkingDayServiceImpl implements WorkingDayService {

    private final Logger log = LoggerFactory.getLogger(WorkingDayServiceImpl.class);

    private final WorkingDayRepository workingDayRepository;

    public WorkingDayServiceImpl(WorkingDayRepository workingDayRepository) {
        this.workingDayRepository = workingDayRepository;
    }

    @Override
    public WorkingDay save(WorkingDay workingDay) {
        log.debug("Request to save WorkingDay : {}", workingDay);
        return workingDayRepository.save(workingDay);
    }

    @Override
    public Optional<WorkingDay> partialUpdate(WorkingDay workingDay) {
        log.debug("Request to partially update WorkingDay : {}", workingDay);

        return workingDayRepository
            .findById(workingDay.getId())
            .map(
                existingWorkingDay -> {
                    if (workingDay.getDayNo() != null) {
                        existingWorkingDay.setDayNo(workingDay.getDayNo());
                    }
                    if (workingDay.getDayLabel() != null) {
                        existingWorkingDay.setDayLabel(workingDay.getDayLabel());
                    }

                    return existingWorkingDay;
                }
            )
            .map(workingDayRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkingDay> findAll() {
        log.debug("Request to get all WorkingDays");
        return workingDayRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkingDay> findOne(Long id) {
        log.debug("Request to get WorkingDay : {}", id);
        return workingDayRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkingDay : {}", id);
        workingDayRepository.deleteById(id);
    }
}
