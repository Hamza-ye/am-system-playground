package org.nmcpye.activitiesmanagement.extended.period;

import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.RelativePeriods;
import org.nmcpye.activitiesmanagement.extended.i18n.I18nFormat;
import org.nmcpye.activitiesmanagement.extended.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectUtils.getIdentifiers;

@Service
public class DefaultPeriodService implements PeriodServiceExt {

    private final Logger log = LoggerFactory.getLogger(DefaultPeriodService.class);

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PeriodStore periodStore;

    public DefaultPeriodService(PeriodStore periodStore) {
        checkNotNull(periodStore);

        this.periodStore = periodStore;
    }

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addPeriod(Period period) {
        periodStore.addPeriod(period);
        return period.getId();
    }

    @Override
    @Transactional
    public void deletePeriod(Period period) {
        periodStore.delete(period);
    }

    @Override
    @Transactional(readOnly = true)
    public Period getPeriod(Long id) {
        return periodStore.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Period getPeriod(String isoPeriod) {
        Period period = PeriodType.getPeriodFromIsoString(isoPeriod);

        if (period != null) {
            period = periodStore.getPeriod(period.getStartDate(), period.getEndDate(), period.getPeriodType());
        }

        return period;
    }

    @Override
    @Transactional(readOnly = true)
    public Period getPeriod(Date startDate, Date endDate, PeriodType periodType) {
        return periodStore.getPeriod(startDate, endDate, periodType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getAllPeriods() {
        return periodStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsByPeriodType(PeriodType periodType) {
        return periodStore.getPeriodsByPeriodType(periodType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsBetweenDates(Date startDate, Date endDate) {
        return periodStore.getPeriodsBetweenDates(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsBetweenDates(PeriodType periodType, Date startDate, Date endDate) {
        return periodStore.getPeriodsBetweenDates(periodType, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsBetweenOrSpanningDates(Date startDate, Date endDate) {
        return periodStore.getPeriodsBetweenOrSpanningDates(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getIntersectingPeriodsByPeriodType(PeriodType periodType, Date startDate, Date endDate) {
        return periodStore.getIntersectingPeriodsByPeriodType(periodType, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getIntersectingPeriods(Date startDate, Date endDate) {
        return periodStore.getIntersectingPeriods(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getIntersectionPeriods(Collection<Period> periods) {
        Set<Period> intersecting = new HashSet<>();

        for (Period period : periods) {
            intersecting.addAll(getIntersectingPeriods(period.getStartDate(), period.getEndDate()));
        }

        return new ArrayList<>(intersecting);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getBoundaryPeriods(Period period, Collection<Period> periods) {
        List<Period> immutablePeriods = new ArrayList<>(periods);

        Iterator<Period> iterator = immutablePeriods.iterator();

        while (iterator.hasNext()) {
            Period iterated = iterator.next();

            if (
                !DateUtils.strictlyBetween(period.getStartDate(), iterated.getStartDate(), iterated.getEndDate()) &&
                !DateUtils.strictlyBetween(period.getEndDate(), iterated.getStartDate(), iterated.getEndDate())
            ) {
                iterator.remove();
            }
        }

        return immutablePeriods;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getInclusivePeriods(Period period, Collection<Period> periods) {
        List<Period> immutablePeriods = new ArrayList<>(periods);

        Iterator<Period> iterator = immutablePeriods.iterator();

        while (iterator.hasNext()) {
            Period iterated = iterator.next();

            if (
                !DateUtils.between(iterated.getStartDate(), period.getStartDate(), period.getEndDate()) ||
                !DateUtils.between(iterated.getEndDate(), period.getStartDate(), period.getEndDate())
            ) {
                iterator.remove();
            }
        }

        return immutablePeriods;
    }

    @Override
    @Transactional
    public List<Period> reloadPeriods(List<Period> periods) {
        List<Period> reloaded = new ArrayList<>();

        for (Period period : periods) {
            reloaded.add(periodStore.reloadForceAddPeriod(period));
        }

        return reloaded;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriods(Period lastPeriod, int historyLength) {
        List<Period> periods = new ArrayList<>(historyLength);

        lastPeriod = periodStore.reloadForceAddPeriod(lastPeriod);

        PeriodType periodType = lastPeriod.getPeriodType();

        for (int i = 0; i < historyLength; ++i) {
            Period pe = getPeriodFromDates(lastPeriod.getStartDate(), lastPeriod.getEndDate(), periodType);

            periods.add(pe != null ? pe : lastPeriod);

            lastPeriod = periodType.getPreviousPeriod(lastPeriod);
        }

        Collections.reverse(periods);

        return periods;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> namePeriods(Collection<Period> periods, I18nFormat format) {
        for (Period period : periods) {
            period.setName(format.formatPeriod(period));
        }

        return new ArrayList<>(periods);
    }

    @Override
    @Transactional(readOnly = true)
    public Period getPeriodFromDates(Date startDate, Date endDate, PeriodType periodType) {
        return periodStore.getPeriodFromDates(startDate, endDate, periodType);
    }

    @Override
    @Transactional
    public Period reloadPeriod(Period period) {
        return periodStore.reloadForceAddPeriod(period);
    }

    /**
     * Fix issue -7539
     * If period doesn't exist in cache and database.
     * Need to add and sync with database right away in a separate session/transaction.
     * Otherwise will get foreign key constraint error in subsequence calls of batch.flush()
     **/
    @Override
    @Transactional(readOnly = true)
    public Period reloadIsoPeriodInStatelessSession(String isoPeriod) {
        Period period = PeriodType.getPeriodFromIsoString(isoPeriod);

        if (period == null) {
            return null;
        }

        Period reloadedPeriod = periodStore.reloadPeriod(period);

        if (reloadedPeriod != null) {
            return reloadedPeriod;
        }

        period.setPeriodType(reloadPeriodType(period.getPeriodType()));

        return periodStore.insertIsoPeriodInStatelessSession(period);
    }

    @Override
    @Transactional
    public Period reloadIsoPeriod(String isoPeriod) {
        Period period = PeriodType.getPeriodFromIsoString(isoPeriod);

        return period != null ? reloadPeriod(period) : null;
    }

    @Override
    @Transactional
    public List<Period> reloadIsoPeriods(List<String> isoPeriods) {
        List<Period> periods = new ArrayList<>();

        for (String iso : isoPeriods) {
            Period period = reloadIsoPeriod(iso);

            if (period != null) {
                periods.add(period);
            }
        }

        return periods;
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodHierarchy getPeriodHierarchy(Collection<Period> periods) {
        PeriodHierarchy hierarchy = new PeriodHierarchy();

        for (Period period : periods) {
            hierarchy
                .getIntersectingPeriods()
                .put(period.getId(), new HashSet<>(getIdentifiers(getIntersectingPeriods(period.getStartDate(), period.getEndDate()))));
            hierarchy
                .getPeriodsBetween()
                .put(period.getId(), new HashSet<>(getIdentifiers(getPeriodsBetweenDates(period.getStartDate(), period.getEndDate()))));
        }

        return hierarchy;
    }

    @Override
    @Transactional(readOnly = true)
    public int getDayInPeriod(Period period, Date date) {
        int days = (int) TimeUnit.DAYS.convert(date.getTime() - period.getStartDate().getTime(), TimeUnit.MILLISECONDS);

        return Math.min(Math.max(0, days), period.getDaysInPeriod());
    }

    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public PeriodType getPeriodType(int id) {
        return periodStore.getPeriodType(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeriodType> getAllPeriodTypes() {
        return PeriodType.getAvailablePeriodTypes();
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodType getPeriodTypeByName(String name) {
        return PeriodType.getPeriodTypeByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodType getPeriodTypeByClass(Class<? extends PeriodType> periodType) {
        return periodStore.getPeriodType(periodType);
    }

    @Override
    @Transactional(readOnly = true)
    public PeriodType reloadPeriodType(PeriodType periodType) {
        return periodStore.reloadPeriodType(periodType);
    }

    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void deleteRelativePeriods(RelativePeriods relativePeriods) {
        periodStore.deleteRelativePeriods(relativePeriods);
    }

    //////////////////////////////////////////////////////
    //
    // JHipster Methods
    //
    //////////////////////////////////////////////////////

    @Override
    public Period save(Period period) {
        log.debug("Request to save Period : {}", period);
        periodStore.saveObject(period);
        return period;
    }

    @Override
    public Optional<Period> partialUpdate(Period period) {
        log.debug("Request to partially update Period : {}", period);

        return Optional
            .of(periodStore.get(period.getId()))
            .map(
                existingPeriod -> {
                    if (period.getName() != null) {
                        existingPeriod.setName(period.getName());
                    }
                    if (period.getStartDate() != null) {
                        existingPeriod.setStartDate(period.getStartDate());
                    }
                    if (period.getEndDate() != null) {
                        existingPeriod.setEndDate(period.getEndDate());
                    }

                    return existingPeriod;
                }
            )
            .map(
                period1 -> {
                    periodStore.saveObject(period);
                    return period;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> findAll() {
        log.debug("Request to get all Periods");
        return periodStore.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Period> findOne(Long id) {
        log.debug("Request to get Period : {}", id);
        return Optional.of(periodStore.get(id));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Period : {}", id);
        periodStore.delete(periodStore.get(id));
    }
}
