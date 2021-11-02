package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.OrganisationUnitLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganisationUnitLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganisationUnitLevelRepository extends JpaRepository<OrganisationUnitLevel, Long> {}
