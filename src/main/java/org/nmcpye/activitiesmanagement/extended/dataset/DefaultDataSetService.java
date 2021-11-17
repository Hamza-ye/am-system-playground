package org.nmcpye.activitiesmanagement.extended.dataset;

import com.google.common.collect.Lists;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository.DataSetPagingRepository;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.query.QueryParserException;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class DefaultDataSetService
    implements DataSetServiceExt {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final DataSetPagingRepository dataSetPagingRepository;

    private UserService currentUserService;

    public DefaultDataSetService(DataSetPagingRepository dataSetPagingRepository, UserService currentUserService) {
        checkNotNull(dataSetPagingRepository);
        checkNotNull(currentUserService);

        this.dataSetPagingRepository = dataSetPagingRepository;
        this.currentUserService = currentUserService;
    }

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public long addDataSet(DataSet dataSet) {
        dataSetPagingRepository.saveObject(dataSet);
        return dataSet.getId();
    }

    @Override
    @Transactional
    public void updateDataSet(DataSet dataSet) {
        dataSetPagingRepository.update(dataSet);
    }

    @Override
    @Transactional
    public void deleteDataSet(DataSet dataSet) {
        dataSetPagingRepository.delete(dataSet);
    }

    @Override
    @Transactional(readOnly = true)
    public DataSet getDataSet(long id) {
        return dataSetPagingRepository.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DataSet getDataSet(String uid) {
        return dataSetPagingRepository.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public DataSet getDataSetNoAcl(String uid) {
        return dataSetPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> getAllDataSets() {
        return dataSetPagingRepository.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> getDataSetsByPeriodType(PeriodType periodType) {
        return dataSetPagingRepository.getDataSetsByPeriodType(periodType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> getUserDataRead(User user) {
        if (user == null) {
            return Lists.newArrayList();
        }

        return user.isSuper() ? getAllDataSets() : dataSetPagingRepository.getDataReadAll(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> getAllDataRead() {
        User user = currentUserService.getUserWithAuthorities().orElse(null);

        return getUserDataRead(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> getAllDataWrite() {
        User user = currentUserService.getUserWithAuthorities().orElse(null);

        return getUserDataWrite(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> getUserDataWrite(User user) {
        if (user == null) {
            return Lists.newArrayList();
        }

        return user.isSuper() ? getAllDataSets() : dataSetPagingRepository.getDataWriteAll(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSet> getDataSetsNotAssignedToOrganisationUnits() {
        return dataSetPagingRepository.getDataSetsNotAssignedToOrganisationUnits();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLocked(User user, DataSet dataSet, Period period, OrganisationUnit organisationUnit, Date now) {
        return dataSet.isLocked(user, period, now);
//            && lockExceptionStore.getCount(dataSet, period, organisationUnit) == 0L;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLocked(User user, DataSet dataSet, Period period, OrganisationUnit organisationUnit,
                            Date now, boolean useOrgUnitChildren) {
        if (!useOrgUnitChildren) {
            return isLocked(user, dataSet, period, organisationUnit, now);
        }

        if (organisationUnit == null || !organisationUnit.hasChild()) {
            return false;
        }

        for (OrganisationUnit child : organisationUnit.getChildren()) {
            if (isLocked(user, dataSet, period, child, now)) {
                return true;
            }
        }

        return false;
    }

    private List<String> parseIdFromString(String input, String operator) {
        List<String> ids = new ArrayList<>();

        if ("in".equalsIgnoreCase(operator)) {
            if (input.startsWith("[") && input.endsWith("]")) {
                String[] split = input.substring(1, input.length() - 1).split(",");
                Collections.addAll(ids, split);
            } else {
                throw new QueryParserException("Invalid query: " + input);
            }
        } else if ("eq".equalsIgnoreCase(operator)) {
            ids.add(input);
        }
        return ids;
    }
}
