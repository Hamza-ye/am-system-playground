package org.nmcpye.activitiesmanagement.extended.service;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.domain.period.RelativePeriods;
import org.nmcpye.activitiesmanagement.extended.common.exception.InvalidIdentifierReferenceException;
import org.nmcpye.activitiesmanagement.extended.i18n.I18nFormat;
import org.nmcpye.activitiesmanagement.extended.period.PeriodHierarchy;
import org.nmcpye.activitiesmanagement.extended.repository.PeriodRepository;
import org.nmcpye.activitiesmanagement.extended.repository.PeriodTypeRepository;
import org.nmcpye.activitiesmanagement.extended.repository.RelativePeriodsRepository;
import org.nmcpye.activitiesmanagement.extended.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

//@Service
public class DefaultPeriodService implements PeriodService {

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger(DefaultPeriodService.class);

    private PeriodRepository periodRepository;

    private PeriodTypeRepository periodTypeRepository;

    private RelativePeriodsRepository relativePeriodsRepository;

    private static Cache<String, Long> PERIOD_ID_CACHE = Caffeine
        .newBuilder()
        .expireAfterWrite(24, TimeUnit.HOURS)
        .initialCapacity(200)
        .maximumSize(10000)
        .build();

    public DefaultPeriodService(
        PeriodRepository periodRepository,
        PeriodTypeRepository periodTypeRepository,
        RelativePeriodsRepository relativePeriodsRepository
    ) {
        checkNotNull(periodRepository);
        checkNotNull(periodTypeRepository);
        checkNotNull(relativePeriodsRepository);

        this.periodRepository = periodRepository;
        this.periodTypeRepository = periodTypeRepository;
        this.relativePeriodsRepository = relativePeriodsRepository;
    }

    // -------------------------------------------------------------------------
    // Period
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public Long addPeriod(Period period) {
        period.setPeriodType(reloadPeriodType(period.getPeriodType()));
        periodRepository.save(period);
        return period.getId();
    }

    @Override
    @Transactional
    public void deletePeriod(Period period) {
        periodRepository.delete(period);
    }

    @Override
    @Transactional(readOnly = true)
    public Period getPeriod(Long id) {
        return periodRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Period getPeriod(String isoPeriod) {
        Period period = PeriodType.getPeriodFromIsoString(isoPeriod);

        if (period != null) {
            period = periodRepository.getPeriod(period.getStartDate(), period.getEndDate(), reloadPeriodType(period.getPeriodType()));
        }

        return period;
    }

    @Override
    @Transactional(readOnly = true)
    public Period getPeriod(Date startDate, Date endDate, PeriodType periodType) {
        return periodRepository.getPeriod(startDate, endDate, reloadPeriodType(periodType));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getAllPeriods() {
        return periodRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsByPeriodType(PeriodType periodType) {
        return periodRepository.getPeriodsByPeriodType(reloadPeriodType(periodType));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsBetweenDates(Date startDate, Date endDate) {
        return periodRepository.getPeriodsBetweenDates(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsBetweenDates(PeriodType periodType, Date startDate, Date endDate) {
        return periodRepository.getPeriodsBetweenDates(reloadPeriodType(periodType), startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriodsBetweenOrSpanningDates(Date startDate, Date endDate) {
        return periodRepository.getPeriodsBetweenOrSpanningDates(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getIntersectingPeriodsByPeriodType(PeriodType periodType, Date startDate, Date endDate) {
        return periodRepository.getIntersectingPeriodsByPeriodType(reloadPeriodType(periodType), startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getIntersectingPeriods(Date startDate, Date endDate) {
        return periodRepository.getIntersectingPeriods(startDate, endDate);
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
            reloaded.add(reloadForceAddPeriod(period));
        }

        return reloaded;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> getPeriods(Period lastPeriod, int historyLength) {
        List<Period> periods = new ArrayList<>(historyLength);

        lastPeriod = reloadForceAddPeriod(lastPeriod);

        PeriodType periodType = lastPeriod.getPeriodType();

        for (int i = 0; i < historyLength; ++i) {
            Period pe = getPeriodFromDates(lastPeriod.getStartDate(), lastPeriod.getEndDate(), reloadPeriodType(periodType));

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
        return periodRepository.getPeriodFromDates(startDate, endDate, reloadPeriodType(periodType));
    }

    @Override
    @Transactional
    public Period reloadPeriod(Period period) {
        Long id = PERIOD_ID_CACHE.get(
            period.getCacheKey(),
            key -> getPeriodId(period.getStartDate(), period.getEndDate(), period.getPeriodType())
        );

        Period storedPeriod = id != null ? periodRepository.findById(id).orElse(null) : null;

        return storedPeriod != null ? storedPeriod.copyTransientProperties(period) : null;
    }

    /**
     * Fix issue If period doesn't exist in cache and database
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

        Period reloadedPeriod = periodRepository.getPeriod(
            period.getStartDate(),
            period.getEndDate(),
            reloadPeriodType(period.getPeriodType())
        );

        if (reloadedPeriod != null) {
            return reloadedPeriod;
        }

        period.setPeriodType(reloadPeriodType(period.getPeriodType()));

        return periodRepository.saveAndFlush(period);
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

    private static List<Long> getIdentifiers(Collection<Period> periods) {
        return periods != null ? periods.stream().map(Period::getId).collect(Collectors.toList()) : null;
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
        return periodTypeRepository.findById(id).orElse(null);
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
    public PeriodType reloadPeriodType(PeriodType periodType) {
        if (periodType == null || periodTypeRepository.findById(periodType.getId()).isPresent()) {
            return periodType;
        }

        List<PeriodType> periodTypes = periodTypeRepository.findAll();
        PeriodType reloadedPeriodType = periodTypes.get(periodTypes.indexOf(periodType));

        if (reloadedPeriodType == null) {
            throw new InvalidIdentifierReferenceException(
                "The PeriodType referenced by the Period is not in database: " + periodType.getName()
            );
        }

        return reloadedPeriodType;
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

    private Long getPeriodId(Date startDate, Date endDate, PeriodType periodType) {
        Period period = getPeriod(startDate, endDate, periodType);

        return period != null ? period.getId() : null;
    }

    // -------------------------------------------------------------------------
    // PeriodType
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public void deleteRelativePeriods(RelativePeriods relativePeriods) {
        relativePeriodsRepository.delete(relativePeriods);
    }

    //////////////////////////////////////////////////////
    //
    // JHipster Methods
    //
    //////////////////////////////////////////////////////

    @Override
    public Period save(Period period) {
        log.debug("Request to save Period : {}", period);
        periodRepository.save(period);
        //        return periodRepository.save(period);
        return period;
    }

    @Override
    public Optional<Period> partialUpdate(Period period) {
        log.debug("Request to partially update Period : {}", period);

        //        return periodRepository
        //            .findById(period.getId())
        //            .map(
        //                existingPeriod -> {
        //                    if (period.getName() != null) {
        //                        existingPeriod.setName(period.getName());
        //                    }
        //                    if (period.getStartDate() != null) {
        //                        existingPeriod.setStartDate(period.getStartDate());
        //                    }
        //                    if (period.getEndDate() != null) {
        //                        existingPeriod.setEndDate(period.getEndDate());
        //                    }
        //
        //                    return existingPeriod;
        //                }
        //            )
        //            .map(periodRepository::save);
        return periodRepository
            .findById(period.getId())
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
                    periodRepository.save(period1);
                    return period1;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Period> findAll() {
        log.debug("Request to get all Periods");
        return periodRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Period> findOne(Long id) {
        log.debug("Request to get Period : {}", id);
        return periodRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Period : {}", id);
        periodRepository.deleteById(id);
    }
}
