package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LlinsVillageTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsVillageTargetRepository extends JpaRepository<LlinsVillageTarget, Long> {
    @Query(
        "select lLINSVillageTarget from LlinsVillageTarget lLINSVillageTarget where lLINSVillageTarget.createdBy.login = ?#{principal.username}"
    )
    List<LlinsVillageTarget> findByUserIsCurrentUser();

    @Query(
        "select lLINSVillageTarget from LlinsVillageTarget lLINSVillageTarget where lLINSVillageTarget.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsVillageTarget> findByLastUpdatedByIsCurrentUser();
}
