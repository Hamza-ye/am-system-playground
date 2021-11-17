package org.nmcpye.activitiesmanagement.extended.dataset.hibernate;

import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.dataset.DataSetStore;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.JpaQueryParameters;
import org.nmcpye.activitiesmanagement.extended.period.PeriodService;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Hamza on 17-11-2021.
 */
public class DataSetStoreImpl
    extends HibernateIdentifiableObjectStore<DataSet>
    implements DataSetStore {
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final PeriodService periodService;

    public DataSetStoreImpl(JdbcTemplate jdbcTemplate, UserService currentUserService, AclService aclService,
                            PeriodService periodService) {
        super(jdbcTemplate, DataSet.class, currentUserService, aclService, true);

        checkNotNull(periodService);

        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    @Override
    public void saveObject(DataSet dataSet) {
        PeriodType periodType = periodService.reloadPeriodType(dataSet.getPeriodType());

        dataSet.setPeriodType(periodType);

        super.saveObject(dataSet);
    }

    @Override
    public void update(DataSet dataSet) {
        PeriodType periodType = periodService.reloadPeriodType(dataSet.getPeriodType());

        dataSet.setPeriodType(periodType);

        super.update(dataSet);
    }

    @Override
    public List<DataSet> getDataSetsByPeriodType(PeriodType periodType) {
        PeriodType refreshedPeriodType = periodService.reloadPeriodType(periodType);

        CriteriaBuilder builder = getCriteriaBuilder();

        JpaQueryParameters<DataSet> parameters = newJpaParameters()
            .addPredicate(root -> builder.equal(root.get("periodType"), refreshedPeriodType));

        return getList(builder, parameters);
    }

    @Override
    public List<DataSet> getDataSetsNotAssignedToOrganisationUnits() {
        return getQuery("from DataSet ds where size(ds.sources) = 0").list();
    }
}
