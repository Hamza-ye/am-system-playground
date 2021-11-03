package org.nmcpye.activitiesmanagement.extended.repository;

import org.nmcpye.activitiesmanagement.domain.period.RelativePeriods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RelativePeriods entity.
 * Created by Hamza on 28/09/2021.
 */
@SuppressWarnings("unused")
@Repository
public interface RelativePeriodsRepository extends JpaRepository<RelativePeriods, Integer> {}
