package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.PeriodType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PeriodType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodTypeRepository extends JpaRepository<PeriodType, Long> {}
