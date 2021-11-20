package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LLINSFamilyTarget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LLINSFamilyTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LLINSFamilyTargetRepository extends JpaRepository<LLINSFamilyTarget, Long> {
    @Query("select lLINSFamilyTarget from LLINSFamilyTarget lLINSFamilyTarget where lLINSFamilyTarget.createdBy.login = ?#{principal.username}")
    List<LLINSFamilyTarget> findByUserIsCurrentUser();

    @Query(
        "select lLINSFamilyTarget from LLINSFamilyTarget lLINSFamilyTarget where lLINSFamilyTarget.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LLINSFamilyTarget> findByLastUpdatedByIsCurrentUser();
}
