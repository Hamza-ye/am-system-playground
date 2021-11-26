package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LlinsFamilyTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsFamilyTargetRepository extends JpaRepository<LlinsFamilyTarget, Long> {
    @Query("select lLINSFamilyTarget from LlinsFamilyTarget lLINSFamilyTarget where lLINSFamilyTarget.createdBy.login = ?#{principal.username}")
    List<LlinsFamilyTarget> findByUserIsCurrentUser();

    @Query(
        "select lLINSFamilyTarget from LlinsFamilyTarget lLINSFamilyTarget where lLINSFamilyTarget.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyTarget> findByLastUpdatedByIsCurrentUser();
}
