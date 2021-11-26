package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.WhMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the WhMovement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhMovementRepository extends JpaRepository<WhMovement, Long> {
    @Query("select whMovement from WhMovement whMovement where whMovement.createdBy.login = ?#{principal.username}")
    List<WhMovement> findByCreatedByIsCurrentUser();

    @Query("select whMovement from WhMovement whMovement where whMovement.lastUpdatedBy.login = ?#{principal.username}")
    List<WhMovement> findByLastUpdatedByIsCurrentUser();
}
