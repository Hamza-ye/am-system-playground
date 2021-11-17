package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.extended.demographicdata.pagingrepository.DemographicDataSourcePagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultDemographicDataSourceService implements DemographicDataSourceServiceExt {
    private final DemographicDataSourcePagingRepository demographicDataSourcePagingRepository;

    public DefaultDemographicDataSourceService(DemographicDataSourcePagingRepository demographicDataSourcePagingRepository) {
        this.demographicDataSourcePagingRepository = demographicDataSourcePagingRepository;
    }

    @Override
    public Long addDemographicDataSource(DemographicDataSource demographicDataSource) {
        demographicDataSourcePagingRepository.saveObject(demographicDataSource);
        return demographicDataSource.getId();
    }

    @Override
    public void updateDemographicDataSource(DemographicDataSource demographicDataSource) {
        demographicDataSourcePagingRepository.update(demographicDataSource);
    }

    @Override
    public void deleteDemographicDataSource(DemographicDataSource demographicDataSource) {
        demographicDataSourcePagingRepository.delete(demographicDataSource);
    }

    @Override
    public DemographicDataSource getDemographicDataSource(Long id) {
        return demographicDataSourcePagingRepository.get(id);
    }

    @Override
    public DemographicDataSource getDemographicDataSource(String uid) {
        return demographicDataSourcePagingRepository.getByUid(uid);
    }

    @Override
    public DemographicDataSource getDemographicDataSourceNoAcl(String uid) {
        return demographicDataSourcePagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<DemographicDataSource> getAllDemographicDataSources() {
        return demographicDataSourcePagingRepository.getAll();
    }

    @Override
    public List<DemographicDataSource> getAllDataRead() {
        return demographicDataSourcePagingRepository.getDataReadAll();
    }

    @Override
    public List<DemographicDataSource> getUserDataRead(User user) {
        return demographicDataSourcePagingRepository.getDataReadAll(user);
    }

    @Override
    public List<DemographicDataSource> getAllDataWrite() {
        return demographicDataSourcePagingRepository.getDataWriteAll();
    }

    @Override
    public List<DemographicDataSource> getUserDataWrite(User user) {
        return demographicDataSourcePagingRepository.getDataWriteAll(user);
    }
}
