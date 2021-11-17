package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.repository.DataSetRepository;
import org.nmcpye.activitiesmanagement.service.DataSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DataSet}.
 */
@Service
@Transactional
public class DataSetServiceImpl implements DataSetService {

    private final Logger log = LoggerFactory.getLogger(DataSetServiceImpl.class);

    private final DataSetRepository dataSetRepository;

    public DataSetServiceImpl(DataSetRepository dataSetRepository) {
        this.dataSetRepository = dataSetRepository;
    }

    @Override
    public DataSet save(DataSet dataSet) {
        log.debug("Request to save DataSet : {}", dataSet);
        return dataSetRepository.save(dataSet);
    }

    @Override
    public Optional<DataSet> partialUpdate(DataSet dataSet) {
        log.debug("Request to partially update DataSet : {}", dataSet);

        return dataSetRepository
            .findById(dataSet.getId())
            .map(
                existingDataSet -> {
                    if (dataSet.getUid() != null) {
                        existingDataSet.setUid(dataSet.getUid());
                    }
                    if (dataSet.getCode() != null) {
                        existingDataSet.setCode(dataSet.getCode());
                    }
                    if (dataSet.getName() != null) {
                        existingDataSet.setName(dataSet.getName());
                    }
                    if (dataSet.getShortName() != null) {
                        existingDataSet.setShortName(dataSet.getShortName());
                    }
                    if (dataSet.getDescription() != null) {
                        existingDataSet.setDescription(dataSet.getDescription());
                    }
                    if (dataSet.getCreated() != null) {
                        existingDataSet.setCreated(dataSet.getCreated());
                    }
                    if (dataSet.getLastUpdated() != null) {
                        existingDataSet.setLastUpdated(dataSet.getLastUpdated());
                    }
                    if (dataSet.getExpiryDays() != null) {
                        existingDataSet.setExpiryDays(dataSet.getExpiryDays());
                    }
                    if (dataSet.getTimelyDays() != null) {
                        existingDataSet.setTimelyDays(dataSet.getTimelyDays());
                    }

                    return existingDataSet;
                }
            )
            .map(dataSetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> findAll() {
        log.debug("Request to get all DataSets");
        return dataSetRepository.findAllWithEagerRelationships();
    }

    public Page<DataSet> findAllWithEagerRelationships(Pageable pageable) {
        return dataSetRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataSet> findOne(Long id) {
        log.debug("Request to get DataSet : {}", id);
        return dataSetRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataSet : {}", id);
        dataSetRepository.deleteById(id);
    }
}
