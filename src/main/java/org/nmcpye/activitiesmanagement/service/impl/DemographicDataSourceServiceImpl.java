package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.repository.DemographicDataSourceRepository;
import org.nmcpye.activitiesmanagement.service.DemographicDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DemographicDataSource}.
 */
@Service
@Transactional
public class DemographicDataSourceServiceImpl implements DemographicDataSourceService {

    private final Logger log = LoggerFactory.getLogger(DemographicDataSourceServiceImpl.class);

    private final DemographicDataSourceRepository demographicDataSourceRepository;

    public DemographicDataSourceServiceImpl(DemographicDataSourceRepository demographicDataSourceRepository) {
        this.demographicDataSourceRepository = demographicDataSourceRepository;
    }

    @Override
    public DemographicDataSource save(DemographicDataSource demographicDataSource) {
        log.debug("Request to save DemographicDataSource : {}", demographicDataSource);
        return demographicDataSourceRepository.save(demographicDataSource);
    }

    @Override
    public Optional<DemographicDataSource> partialUpdate(DemographicDataSource demographicDataSource) {
        log.debug("Request to partially update DemographicDataSource : {}", demographicDataSource);

        return demographicDataSourceRepository
            .findById(demographicDataSource.getId())
            .map(
                existingDemographicDataSource -> {
                    if (demographicDataSource.getUid() != null) {
                        existingDemographicDataSource.setUid(demographicDataSource.getUid());
                    }
                    if (demographicDataSource.getCode() != null) {
                        existingDemographicDataSource.setCode(demographicDataSource.getCode());
                    }
                    if (demographicDataSource.getName() != null) {
                        existingDemographicDataSource.setName(demographicDataSource.getName());
                    }
                    if (demographicDataSource.getCreated() != null) {
                        existingDemographicDataSource.setCreated(demographicDataSource.getCreated());
                    }
                    if (demographicDataSource.getLastUpdated() != null) {
                        existingDemographicDataSource.setLastUpdated(demographicDataSource.getLastUpdated());
                    }

                    return existingDemographicDataSource;
                }
            )
            .map(demographicDataSourceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DemographicDataSource> findAll() {
        log.debug("Request to get all DemographicDataSources");
        return demographicDataSourceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemographicDataSource> findOne(Long id) {
        log.debug("Request to get DemographicDataSource : {}", id);
        return demographicDataSourceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemographicDataSource : {}", id);
        demographicDataSourceRepository.deleteById(id);
    }
}
