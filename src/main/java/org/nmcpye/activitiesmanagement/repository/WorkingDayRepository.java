package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.WorkingDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkingDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {}
