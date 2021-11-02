package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.RelativePeriods;
import org.nmcpye.activitiesmanagement.repository.RelativePeriodsRepository;
import org.nmcpye.activitiesmanagement.service.RelativePeriodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RelativePeriods}.
 */
@Service
@Transactional
public class RelativePeriodsServiceImpl implements RelativePeriodsService {

    private final Logger log = LoggerFactory.getLogger(RelativePeriodsServiceImpl.class);

    private final RelativePeriodsRepository relativePeriodsRepository;

    public RelativePeriodsServiceImpl(RelativePeriodsRepository relativePeriodsRepository) {
        this.relativePeriodsRepository = relativePeriodsRepository;
    }

    @Override
    public RelativePeriods save(RelativePeriods relativePeriods) {
        log.debug("Request to save RelativePeriods : {}", relativePeriods);
        return relativePeriodsRepository.save(relativePeriods);
    }

    @Override
    public Optional<RelativePeriods> partialUpdate(RelativePeriods relativePeriods) {
        log.debug("Request to partially update RelativePeriods : {}", relativePeriods);

        return relativePeriodsRepository
            .findById(relativePeriods.getId())
            .map(
                existingRelativePeriods -> {
                    if (relativePeriods.getThisDay() != null) {
                        existingRelativePeriods.setThisDay(relativePeriods.getThisDay());
                    }
                    if (relativePeriods.getYesterday() != null) {
                        existingRelativePeriods.setYesterday(relativePeriods.getYesterday());
                    }
                    if (relativePeriods.getLast3Days() != null) {
                        existingRelativePeriods.setLast3Days(relativePeriods.getLast3Days());
                    }
                    if (relativePeriods.getLast7Days() != null) {
                        existingRelativePeriods.setLast7Days(relativePeriods.getLast7Days());
                    }
                    if (relativePeriods.getLast14Days() != null) {
                        existingRelativePeriods.setLast14Days(relativePeriods.getLast14Days());
                    }
                    if (relativePeriods.getThisMonth() != null) {
                        existingRelativePeriods.setThisMonth(relativePeriods.getThisMonth());
                    }
                    if (relativePeriods.getLastMonth() != null) {
                        existingRelativePeriods.setLastMonth(relativePeriods.getLastMonth());
                    }
                    if (relativePeriods.getThisBimonth() != null) {
                        existingRelativePeriods.setThisBimonth(relativePeriods.getThisBimonth());
                    }
                    if (relativePeriods.getLastBimonth() != null) {
                        existingRelativePeriods.setLastBimonth(relativePeriods.getLastBimonth());
                    }
                    if (relativePeriods.getThisQuarter() != null) {
                        existingRelativePeriods.setThisQuarter(relativePeriods.getThisQuarter());
                    }
                    if (relativePeriods.getLastQuarter() != null) {
                        existingRelativePeriods.setLastQuarter(relativePeriods.getLastQuarter());
                    }
                    if (relativePeriods.getThisSixMonth() != null) {
                        existingRelativePeriods.setThisSixMonth(relativePeriods.getThisSixMonth());
                    }
                    if (relativePeriods.getLastSixMonth() != null) {
                        existingRelativePeriods.setLastSixMonth(relativePeriods.getLastSixMonth());
                    }
                    if (relativePeriods.getWeeksThisYear() != null) {
                        existingRelativePeriods.setWeeksThisYear(relativePeriods.getWeeksThisYear());
                    }
                    if (relativePeriods.getMonthsThisYear() != null) {
                        existingRelativePeriods.setMonthsThisYear(relativePeriods.getMonthsThisYear());
                    }
                    if (relativePeriods.getBiMonthsThisYear() != null) {
                        existingRelativePeriods.setBiMonthsThisYear(relativePeriods.getBiMonthsThisYear());
                    }
                    if (relativePeriods.getQuartersThisYear() != null) {
                        existingRelativePeriods.setQuartersThisYear(relativePeriods.getQuartersThisYear());
                    }
                    if (relativePeriods.getThisYear() != null) {
                        existingRelativePeriods.setThisYear(relativePeriods.getThisYear());
                    }
                    if (relativePeriods.getMonthsLastYear() != null) {
                        existingRelativePeriods.setMonthsLastYear(relativePeriods.getMonthsLastYear());
                    }
                    if (relativePeriods.getQuartersLastYear() != null) {
                        existingRelativePeriods.setQuartersLastYear(relativePeriods.getQuartersLastYear());
                    }
                    if (relativePeriods.getLastYear() != null) {
                        existingRelativePeriods.setLastYear(relativePeriods.getLastYear());
                    }
                    if (relativePeriods.getLast5Years() != null) {
                        existingRelativePeriods.setLast5Years(relativePeriods.getLast5Years());
                    }
                    if (relativePeriods.getLast12Months() != null) {
                        existingRelativePeriods.setLast12Months(relativePeriods.getLast12Months());
                    }
                    if (relativePeriods.getLast6Months() != null) {
                        existingRelativePeriods.setLast6Months(relativePeriods.getLast6Months());
                    }
                    if (relativePeriods.getLast3Months() != null) {
                        existingRelativePeriods.setLast3Months(relativePeriods.getLast3Months());
                    }
                    if (relativePeriods.getLast6BiMonths() != null) {
                        existingRelativePeriods.setLast6BiMonths(relativePeriods.getLast6BiMonths());
                    }
                    if (relativePeriods.getLast4Quarters() != null) {
                        existingRelativePeriods.setLast4Quarters(relativePeriods.getLast4Quarters());
                    }
                    if (relativePeriods.getLast2SixMonths() != null) {
                        existingRelativePeriods.setLast2SixMonths(relativePeriods.getLast2SixMonths());
                    }
                    if (relativePeriods.getThisFinancialYear() != null) {
                        existingRelativePeriods.setThisFinancialYear(relativePeriods.getThisFinancialYear());
                    }
                    if (relativePeriods.getLastFinancialYear() != null) {
                        existingRelativePeriods.setLastFinancialYear(relativePeriods.getLastFinancialYear());
                    }
                    if (relativePeriods.getLast5FinancialYears() != null) {
                        existingRelativePeriods.setLast5FinancialYears(relativePeriods.getLast5FinancialYears());
                    }
                    if (relativePeriods.getThisWeek() != null) {
                        existingRelativePeriods.setThisWeek(relativePeriods.getThisWeek());
                    }
                    if (relativePeriods.getLastWeek() != null) {
                        existingRelativePeriods.setLastWeek(relativePeriods.getLastWeek());
                    }
                    if (relativePeriods.getThisBiWeek() != null) {
                        existingRelativePeriods.setThisBiWeek(relativePeriods.getThisBiWeek());
                    }
                    if (relativePeriods.getLastBiWeek() != null) {
                        existingRelativePeriods.setLastBiWeek(relativePeriods.getLastBiWeek());
                    }
                    if (relativePeriods.getLast4Weeks() != null) {
                        existingRelativePeriods.setLast4Weeks(relativePeriods.getLast4Weeks());
                    }
                    if (relativePeriods.getLast4BiWeeks() != null) {
                        existingRelativePeriods.setLast4BiWeeks(relativePeriods.getLast4BiWeeks());
                    }
                    if (relativePeriods.getLast12Weeks() != null) {
                        existingRelativePeriods.setLast12Weeks(relativePeriods.getLast12Weeks());
                    }
                    if (relativePeriods.getLast52Weeks() != null) {
                        existingRelativePeriods.setLast52Weeks(relativePeriods.getLast52Weeks());
                    }

                    return existingRelativePeriods;
                }
            )
            .map(relativePeriodsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelativePeriods> findAll() {
        log.debug("Request to get all RelativePeriods");
        return relativePeriodsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RelativePeriods> findOne(Long id) {
        log.debug("Request to get RelativePeriods : {}", id);
        return relativePeriodsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RelativePeriods : {}", id);
        relativePeriodsRepository.deleteById(id);
    }
}
