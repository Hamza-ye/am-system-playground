package org.nmcpye.activitiesmanagement.extended.period.hibernate;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.query.Query;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.RelativePeriods;
import org.nmcpye.activitiesmanagement.extended.common.DebugUtils;
import org.nmcpye.activitiesmanagement.extended.common.exception.InvalidIdentifierReferenceException;
import org.nmcpye.activitiesmanagement.extended.common.hibernate.HibernateIdentifiableObjectStore;
import org.nmcpye.activitiesmanagement.extended.hibernatemodule.dbms.DbmsUtils;
import org.nmcpye.activitiesmanagement.extended.period.PeriodStore;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Implements the PeriodStore interface.
 */
@Repository("org.nmcpye.activitiesmanagement.extended.period.PeriodStore")
public class HibernatePeriodStore extends HibernateIdentifiableObjectStore<Period> implements PeriodStore {

    private final Logger log = LoggerFactory.getLogger(HibernatePeriodStore.class);

    private static Cache<String, Long> PERIOD_ID_CACHE;

    public HibernatePeriodStore(JdbcTemplate jdbcTemplate, UserService userService) {
        //        super(sessionFactory, jdbcTemplate, Period.class, userService, true);
        super(jdbcTemplate, Period.class, userService, true);
        transientIdentifiableProperties = true;
    }

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        PERIOD_ID_CACHE = Caffeine.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).initialCapacity(200).maximumSize(10000).build();
    }

    @Override
    public void addPeriod(Period period) {
        period.setPeriodType(reloadPeriodType(period.getPeriodType()));

        saveObject(period);
    }

    @Override
    public Period getPeriod(Date startDate, Date endDate, PeriodType periodType) {
        String query = "from Period p where p.startDate =:startDate and p.endDate =:endDate and p.periodType =:periodType";

        return getSingleResult(
            getQuery(query)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("periodType", reloadPeriodType(periodType))
        );
    }

    @Override
    public List<Period> getPeriodsBetweenDates(Date startDate, Date endDate) {
        String query = "from Period p where p.startDate >=:startDate and p.endDate <=:endDate";

        Query<Period> typedQuery = getQuery(query).setParameter("startDate", startDate).setParameter("endDate", endDate);
        return getList(typedQuery);
    }

    @Override
    public List<Period> getPeriodsBetweenDates(PeriodType periodType, Date startDate, Date endDate) {
        String query = "from Period p where p.startDate >=:startDate and p.endDate <=:endDate and p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery(query)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .setParameter("periodType", reloadPeriodType(periodType).getId());
        return getList(typedQuery);
    }

    @Override
    public List<Period> getPeriodsBetweenOrSpanningDates(Date startDate, Date endDate) {
        String hql =
            "from Period p where ( p.startDate >= :startDate and p.endDate <= :endDate ) or ( p.startDate <= :startDate and p.endDate >= :endDate )";

        return getQuery(hql).setParameter("startDate", startDate).setParameter("endDate", endDate).list();
    }

    @Override
    public List<Period> getIntersectingPeriodsByPeriodType(PeriodType periodType, Date startDate, Date endDate) {
        String query = "from Period p where p.startDate <=:endDate and p.endDate >=:startDate and p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery(query)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .setParameter("periodType", reloadPeriodType(periodType).getId());
        return getList(typedQuery);
    }

    @Override
    public List<Period> getIntersectingPeriods(Date startDate, Date endDate) {
        String query = "from Period p where p.startDate <=:endDate and p.endDate >=:startDate";

        Query<Period> typedQuery = getQuery(query).setParameter("startDate", startDate).setParameter("endDate", endDate);
        return getList(typedQuery);
    }

    @Override
    public List<Period> getPeriodsByPeriodType(PeriodType periodType) {
        String query = "from Period p where p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery(query).setParameter("periodType", reloadPeriodType(periodType).getId());
        return getList(typedQuery);
    }

    @Override
    public Period getPeriodFromDates(Date startDate, Date endDate, PeriodType periodType) {
        String query = "from Period p where p.startDate =:startDate and p.endDate =:endDate and p.periodType.id =:periodType";

        Query<Period> typedQuery = getQuery(query)
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate)
            .setParameter("periodType", reloadPeriodType(periodType).getId());
        return getSingleResult(typedQuery);
    }

    @Override
    public Period reloadPeriod(Period period) {
        Session session = getSession();

        if (session.contains(period)) {
            return period; // Already in session, no reload needed
        }

        Long id = PERIOD_ID_CACHE.get(
            period.getCacheKey(),
            key -> getPeriodId(period.getStartDate(), period.getEndDate(), period.getPeriodType())
        );

        Period storedPeriod = id != null ? getSession().get(Period.class, id) : null;

        return storedPeriod != null ? storedPeriod.copyTransientProperties(period) : null;
    }

    private Long getPeriodId(Date startDate, Date endDate, PeriodType periodType) {
        Period period = getPeriod(startDate, endDate, periodType);

        return period != null ? period.getId() : null;
    }

    @Override
    public Period reloadForceAddPeriod(Period period) {
        Period storedPeriod = reloadPeriod(period);

        if (storedPeriod == null) {
            addPeriod(period);

            return period;
        }

        return storedPeriod;
    }

    // -------------------------------------------------------------------------
    // PeriodType (do not use generic store which is linked to Period)
    // -------------------------------------------------------------------------

    @Override
    public int addPeriodType(PeriodType periodType) {
        Session session = getSession();

        return (Integer) session.save(periodType);
    }

    @Override
    public void deletePeriodType(PeriodType periodType) {
        Session session = getSession();

        session.delete(periodType);
    }

    @Override
    public PeriodType getPeriodType(int id) {
        Session session = getSession();

        return session.get(PeriodType.class, id);
    }

    @Override
    public PeriodType getPeriodType(Class<? extends PeriodType> periodType) {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<PeriodType> query = builder.createQuery(PeriodType.class);
        query.select(query.from(periodType));

        return getSession().createQuery(query).setCacheable(true).uniqueResult();
    }

    @Override
    public List<PeriodType> getAllPeriodTypes() {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<PeriodType> query = builder.createQuery(PeriodType.class);
        query.select(query.from(PeriodType.class));

        return getSession().createQuery(query).setCacheable(true).getResultList();
    }

    @Override
    public PeriodType reloadPeriodType(PeriodType periodType) {
        Session session = getSession();

        if (periodType == null || session.contains(periodType)) {
            return periodType;
        }

        PeriodType reloadedPeriodType = getPeriodType(periodType.getClass());

        if (reloadedPeriodType == null) {
            throw new InvalidIdentifierReferenceException(
                "The PeriodType referenced by the Period is not in database: " + periodType.getName()
            );
        }

        return reloadedPeriodType;
    }

    @Override
    public Period insertIsoPeriodInStatelessSession(Period period) {
        StatelessSession session = getSession().getSessionFactory().openStatelessSession();
        session.beginTransaction();
        try {
            Serializable id = session.insert(period);
            Period storedPeriod = (Period) session.get(Period.class, id);

            PERIOD_ID_CACHE.put(period.getCacheKey(), storedPeriod.getId());

            return storedPeriod;
        } catch (Exception exception) {
            log.error(DebugUtils.getStackTrace(exception));
        } finally {
            DbmsUtils.closeStatelessSession(session);
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // RelativePeriods (do not use generic store which is linked to Period)
    // -------------------------------------------------------------------------

    @Override
    public void deleteRelativePeriods(RelativePeriods relativePeriods) {
        getSession().delete(relativePeriods);
    }
}
