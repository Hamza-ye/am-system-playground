package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DataProvider;
import org.nmcpye.activitiesmanagement.repository.DataProviderRepository;
import org.nmcpye.activitiesmanagement.service.DataProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DataProvider}.
 */
@Service
@Transactional
public class DataProviderServiceImpl implements DataProviderService {

    private final Logger log = LoggerFactory.getLogger(DataProviderServiceImpl.class);

    private final DataProviderRepository dataProviderRepository;

    public DataProviderServiceImpl(DataProviderRepository dataProviderRepository) {
        this.dataProviderRepository = dataProviderRepository;
    }

    @Override
    public DataProvider save(DataProvider dataProvider) {
        log.debug("Request to save DataProvider : {}", dataProvider);
        return dataProviderRepository.save(dataProvider);
    }

    @Override
    public Optional<DataProvider> partialUpdate(DataProvider dataProvider) {
        log.debug("Request to partially update DataProvider : {}", dataProvider);

        return dataProviderRepository
            .findById(dataProvider.getId())
            .map(
                existingDataProvider -> {
                    if (dataProvider.getUid() != null) {
                        existingDataProvider.setUid(dataProvider.getUid());
                    }
                    if (dataProvider.getCode() != null) {
                        existingDataProvider.setCode(dataProvider.getCode());
                    }
                    if (dataProvider.getName() != null) {
                        existingDataProvider.setName(dataProvider.getName());
                    }
                    if (dataProvider.getDescription() != null) {
                        existingDataProvider.setDescription(dataProvider.getDescription());
                    }
                    if (dataProvider.getCreated() != null) {
                        existingDataProvider.setCreated(dataProvider.getCreated());
                    }
                    if (dataProvider.getLastUpdated() != null) {
                        existingDataProvider.setLastUpdated(dataProvider.getLastUpdated());
                    }
                    if (dataProvider.getMobile() != null) {
                        existingDataProvider.setMobile(dataProvider.getMobile());
                    }

                    return existingDataProvider;
                }
            )
            .map(dataProviderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataProvider> findAll() {
        log.debug("Request to get all DataProviders");
        return dataProviderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataProvider> findOne(Long id) {
        log.debug("Request to get DataProvider : {}", id);
        return dataProviderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataProvider : {}", id);
        dataProviderRepository.deleteById(id);
    }
}
