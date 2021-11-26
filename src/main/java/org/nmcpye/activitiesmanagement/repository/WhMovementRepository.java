package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.WhMovement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WhMovement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhMovementRepository extends JpaRepository<WhMovement, Long> {
    @Query("select wHMovement from WhMovement wHMovement where wHMovement.createdBy.login = ?#{principal.username}")
    List<WhMovement> findByUserIsCurrentUser();

    @Query("select wHMovement from WhMovement wHMovement where wHMovement.lastUpdatedBy.login = ?#{principal.username}")
    List<WhMovement> findByLastUpdatedByIsCurrentUser();
}
