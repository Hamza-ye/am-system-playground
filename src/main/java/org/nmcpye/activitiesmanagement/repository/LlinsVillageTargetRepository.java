package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.LlinsVillageTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LlinsVillageTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsVillageTargetRepository extends JpaRepository<LlinsVillageTarget, Long> {
    @Query(
        "select llinsVillageTarget from LlinsVillageTarget llinsVillageTarget where llinsVillageTarget.createdBy.login = ?#{principal.username}"
    )
    List<LlinsVillageTarget> findByCreatedByIsCurrentUser();

    @Query(
        "select llinsVillageTarget from LlinsVillageTarget llinsVillageTarget where llinsVillageTarget.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsVillageTarget> findByLastUpdatedByIsCurrentUser();
}
