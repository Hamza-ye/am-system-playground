package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.DataInputPeriod;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataInputPeriod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataInputPeriodRepository extends JpaRepository<DataInputPeriod, Long> {}
