package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.LlinsFamilyTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LlinsFamilyTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LlinsFamilyTargetRepository extends JpaRepository<LlinsFamilyTarget, Long> {
    @Query(
        "select llinsFamilyTarget from LlinsFamilyTarget llinsFamilyTarget where llinsFamilyTarget.createdBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyTarget> findByCreatedByIsCurrentUser();

    @Query(
        "select llinsFamilyTarget from LlinsFamilyTarget llinsFamilyTarget where llinsFamilyTarget.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<LlinsFamilyTarget> findByLastUpdatedByIsCurrentUser();
}
