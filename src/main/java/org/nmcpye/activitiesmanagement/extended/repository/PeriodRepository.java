package org.nmcpye.activitiesmanagement.extended.repository;

import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Spring Data SQL repository for the Period entity.
 * Created by Hamza on 28/09/2021.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodRepository extends JpaRepository<Period, Long>, JpaSpecificationExecutor<Period> {
    @Query("select p from Period p where p.startDate = ?1 and p.endDate = ?2 and p.periodType = ?3")
    Period getPeriod(Date startDate, Date endDate, PeriodType periodType);

    @Query("select p from Period p where p.startDate >= ?1 and p.endDate <= ?2")
    List<Period> getPeriodsBetweenDates(Date startDate, Date endDate);

    @Query("select p from Period p where p.periodType = ?1 and p.startDate >= ?2 and p.endDate <= ?3")
    List<Period> getPeriodsBetweenDates(PeriodType periodType, Date startDate, Date endDate);

    @Query("select p from Period p " + "where ( p.startDate >= ?1 and p.endDate <= ?2 ) or " + "( p.startDate <= ?1 and p.endDate >= ?2 )")
    List<Period> getPeriodsBetweenOrSpanningDates(Date startDate, Date endDate);

    @Query("select p from Period p where p.startDate <= ?3 and p.endDate >= ?2 and p.periodType = ?1")
    List<Period> getIntersectingPeriodsByPeriodType(PeriodType periodType, Date startDate, Date endDate);

    @Query("select p from Period p where p.endDate >= ?1 and p.startDate <= ?2")
    List<Period> getIntersectingPeriods(Date startDate, Date endDate);

    @Query("select p from Period p where p.periodType = ?1")
    List<Period> getPeriodsByPeriodType(PeriodType periodType);

    @Query("select p from Period p where p.startDate = ?1 and p.endDate = ?2 and p.periodType = ?3")
    Period getPeriodFromDates(Date startDate, Date endDate, PeriodType periodType);
}
