package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.period.RelativePeriods;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RelativePeriods entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelativePeriodsRepository extends JpaRepository<RelativePeriods, Long> {}
