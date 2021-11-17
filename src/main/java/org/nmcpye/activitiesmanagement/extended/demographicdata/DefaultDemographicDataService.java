package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.extended.demographicdata.pagingrepository.DemographicDataPagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultDemographicDataService implements DemographicDataServiceExt {
    private final DemographicDataPagingRepository demographicDataPagingRepository;

    public DefaultDemographicDataService(DemographicDataPagingRepository demographicDataPagingRepository) {
        this.demographicDataPagingRepository = demographicDataPagingRepository;
    }

    @Override
    public long addDemographicData(DemographicData demographicData) {
        demographicDataPagingRepository.saveObject(demographicData);
        return demographicData.getId();
    }

    @Override
    public void updateDemographicData(DemographicData demographicData) {
        demographicDataPagingRepository.update(demographicData);
    }

    @Override
    public void deleteDemographicData(DemographicData demographicData) {
        demographicDataPagingRepository.delete(demographicData);
    }

    @Override
    public DemographicData getDemographicData(long id) {
        return demographicDataPagingRepository.get(id);
    }

    @Override
    public DemographicData getDemographicData(String uid) {
        return demographicDataPagingRepository.getByUid(uid);
    }

    @Override
    public DemographicData getDemographicDataNoAcl(String uid) {
        return demographicDataPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<DemographicData> getAllDemographicDatas() {
        return demographicDataPagingRepository.getAll();
    }

    @Override
    public List<DemographicData> getAllDataRead() {
        return demographicDataPagingRepository.getDataReadAll();
    }

    @Override
    public List<DemographicData> getUserDataRead(User user) {
        return demographicDataPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<DemographicData> getAllDataWrite() {
        return demographicDataPagingRepository.getDataWriteAll();
    }

    @Override
    public List<DemographicData> getUserDataWrite(User user) {
        return demographicDataPagingRepository.getDataWriteAll(user);
    }
}
