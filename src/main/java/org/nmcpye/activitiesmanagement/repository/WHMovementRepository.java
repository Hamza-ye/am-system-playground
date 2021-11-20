package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.WHMovement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WHMovement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WHMovementRepository extends JpaRepository<WHMovement, Long> {
    @Query("select wHMovement from WHMovement wHMovement where wHMovement.createdBy.login = ?#{principal.username}")
    List<WHMovement> findByUserIsCurrentUser();

    @Query("select wHMovement from WHMovement wHMovement where wHMovement.lastUpdatedBy.login = ?#{principal.username}")
    List<WHMovement> findByLastUpdatedByIsCurrentUser();
}
