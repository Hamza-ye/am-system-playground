package org.nmcpye.activitiesmanagement.extended.dataset.hibernate;

import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.dataset.DataSetStore;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.hibernate.JpaQueryParameters;
import org.nmcpye.activitiesmanagement.extended.period.PeriodServiceExt;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
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

    private final PeriodServiceExt periodServiceExt;

    public DataSetStoreImpl(JdbcTemplate jdbcTemplate, ApplicationEventPublisher publisher,
                            UserService userService, AclService aclService, PeriodServiceExt periodServiceExt) {
        super(jdbcTemplate, publisher, DataSet.class, userService, aclService, true);

        checkNotNull(periodServiceExt);

        this.periodServiceExt = periodServiceExt;
    }

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    @Override
    public void saveObject(DataSet dataSet) {
        PeriodType periodType = periodServiceExt.reloadPeriodType(dataSet.getPeriodType());

        dataSet.setPeriodType(periodType);

        super.saveObject(dataSet);
    }

    @Override
    public void update(DataSet dataSet) {
        PeriodType periodType = periodServiceExt.reloadPeriodType(dataSet.getPeriodType());

        dataSet.setPeriodType(periodType);

        super.update(dataSet);
    }

    @Override
    public List<DataSet> getDataSetsByPeriodType(PeriodType periodType) {
        PeriodType refreshedPeriodType = periodServiceExt.reloadPeriodType(periodType);

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
