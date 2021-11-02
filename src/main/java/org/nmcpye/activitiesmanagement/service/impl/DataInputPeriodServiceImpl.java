package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DataInputPeriod;
import org.nmcpye.activitiesmanagement.repository.DataInputPeriodRepository;
import org.nmcpye.activitiesmanagement.service.DataInputPeriodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DataInputPeriod}.
 */
@Service
@Transactional
public class DataInputPeriodServiceImpl implements DataInputPeriodService {

    private final Logger log = LoggerFactory.getLogger(DataInputPeriodServiceImpl.class);

    private final DataInputPeriodRepository dataInputPeriodRepository;

    public DataInputPeriodServiceImpl(DataInputPeriodRepository dataInputPeriodRepository) {
        this.dataInputPeriodRepository = dataInputPeriodRepository;
    }

    @Override
    public DataInputPeriod save(DataInputPeriod dataInputPeriod) {
        log.debug("Request to save DataInputPeriod : {}", dataInputPeriod);
        return dataInputPeriodRepository.save(dataInputPeriod);
    }

    @Override
    public Optional<DataInputPeriod> partialUpdate(DataInputPeriod dataInputPeriod) {
        log.debug("Request to partially update DataInputPeriod : {}", dataInputPeriod);

        return dataInputPeriodRepository
            .findById(dataInputPeriod.getId())
            .map(
                existingDataInputPeriod -> {
                    if (dataInputPeriod.getOpeningDate() != null) {
                        existingDataInputPeriod.setOpeningDate(dataInputPeriod.getOpeningDate());
                    }
                    if (dataInputPeriod.getClosingDate() != null) {
                        existingDataInputPeriod.setClosingDate(dataInputPeriod.getClosingDate());
                    }

                    return existingDataInputPeriod;
                }
            )
            .map(dataInputPeriodRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataInputPeriod> findAll() {
        log.debug("Request to get all DataInputPeriods");
        return dataInputPeriodRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataInputPeriod> findOne(Long id) {
        log.debug("Request to get DataInputPeriod : {}", id);
        return dataInputPeriodRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataInputPeriod : {}", id);
        dataInputPeriodRepository.deleteById(id);
    }
}
