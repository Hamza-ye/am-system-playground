package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LLINSVillageTarget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LLINSVillageTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LLINSVillageTargetRepository extends JpaRepository<LLINSVillageTarget, Long> {
    @Query(
        "select lLINSVillageTarget from LLINSVillageTarget lLINSVillageTarget where lLINSVillageTarget.createdBy.login = ?#{principal.username}"
    )
    List<LLINSVillageTarget> findByUserIsCurrentUser();

    @Query(
        "select lLINSVillageTarget from LLINSVillageTarget lLINSVillageTarget where lLINSVillageTarget.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LLINSVillageTarget> findByLastUpdatedByIsCurrentUser();
}
