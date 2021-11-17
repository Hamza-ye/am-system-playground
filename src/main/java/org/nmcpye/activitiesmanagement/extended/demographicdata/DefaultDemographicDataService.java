package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultDemographicDataService implements DemographicDataServiceExt {
    private final DemographicDataStore demographicDataStore;

    public DefaultDemographicDataService(DemographicDataStore demographicDataStore) {
        this.demographicDataStore = demographicDataStore;
    }

    @Override
    public long addDemographicData(DemographicData demographicData) {
        demographicDataStore.saveObject(demographicData);
        return demographicData.getId();
    }

    @Override
    public void updateDemographicData(DemographicData demographicData) {
        demographicDataStore.update(demographicData);
    }

    @Override
    public void deleteDemographicData(DemographicData demographicData) {
        demographicDataStore.delete(demographicData);
    }

    @Override
    public DemographicData getDemographicData(long id) {
        return demographicDataStore.get(id);
    }

    @Override
    public DemographicData getDemographicData(String uid) {
        return demographicDataStore.getByUid(uid);
    }

    @Override
    public DemographicData getDemographicDataNoAcl(String uid) {
        return demographicDataStore.getByUidNoAcl(uid);
    }

    @Override
    public List<DemographicData> getAllDemographicDatas() {
        return demographicDataStore.getAll();
    }

    @Override
    public List<DemographicData> getAllDataRead() {
        return demographicDataStore.getDataReadAll();
    }

    @Override
    public List<DemographicData> getUserDataRead(User user) {
        return demographicDataStore.getDataReadAll(user);
    }

    @Override
    public List<DemographicData> getAllDataWrite() {
        return demographicDataStore.getDataWriteAll();
    }

    @Override
    public List<DemographicData> getUserDataWrite(User user) {
        return demographicDataStore.getDataWriteAll(user);
    }
}
