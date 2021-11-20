package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.MalariaUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MalariaUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MalariaUnitRepository extends JpaRepository<MalariaUnit, Long> {
    @Query("select malariaUnit from MalariaUnit malariaUnit where malariaUnit.createdBy.login = ?#{principal.username}")
    List<MalariaUnit> findByUserIsCurrentUser();

    @Query("select malariaUnit from MalariaUnit malariaUnit where malariaUnit.lastUpdatedBy.login = ?#{principal.username}")
    List<MalariaUnit> findByLastUpdatedByIsCurrentUser();
}
