package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultDemographicDataSourceService implements DemographicDataSourceServiceExt {
    private final DemographicDataSourceStore demographicDataSourceStore;

    public DefaultDemographicDataSourceService(DemographicDataSourceStore demographicDataSourceStore) {
        this.demographicDataSourceStore = demographicDataSourceStore;
    }

    @Override
    public long addDemographicDataSource(DemographicDataSource demographicDataSource) {
        demographicDataSourceStore.saveObject(demographicDataSource);
        return demographicDataSource.getId();
    }

    @Override
    public void updateDemographicDataSource(DemographicDataSource demographicDataSource) {
        demographicDataSourceStore.update(demographicDataSource);
    }

    @Override
    public void deleteDemographicDataSource(DemographicDataSource demographicDataSource) {
        demographicDataSourceStore.delete(demographicDataSource);
    }

    @Override
    public DemographicDataSource getDemographicDataSource(long id) {
        return demographicDataSourceStore.get(id);
    }

    @Override
    public DemographicDataSource getDemographicDataSource(String uid) {
        return demographicDataSourceStore.getByUid(uid);
    }

    @Override
    public DemographicDataSource getDemographicDataSourceNoAcl(String uid) {
        return demographicDataSourceStore.getByUidNoAcl(uid);
    }

    @Override
    public List<DemographicDataSource> getAllDemographicDataSources() {
        return demographicDataSourceStore.getAll();
    }

    @Override
    public List<DemographicDataSource> getAllDataRead() {
        return demographicDataSourceStore.getDataReadAll();
    }

    @Override
    public List<DemographicDataSource> getUserDataRead(User user) {
        return demographicDataSourceStore.getDataReadAll(user);
    }

    @Override
    public List<DemographicDataSource> getAllDataWrite() {
        return demographicDataSourceStore.getDataWriteAll();
    }

    @Override
    public List<DemographicDataSource> getUserDataWrite(User user) {
        return demographicDataSourceStore.getDataWriteAll(user);
    }
}
