package org.nmcpye.activitiesmanagement.extended.repository;

import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PeriodType entity.
 * Created by Hamza on 28/09/2021.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodTypeRepository extends JpaRepository<PeriodType, Integer>, JpaSpecificationExecutor<PeriodType> {
    //    PeriodType findFirstByName(String name);
}
