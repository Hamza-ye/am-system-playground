package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.CHV;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CHV entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CHVRepository extends JpaRepository<CHV, Long> {
    @Query("select cHV from CHV cHV where cHV.user.login = ?#{principal.username}")
    List<CHV> findByUserIsCurrentUser();

    @Query("select cHV from CHV cHV where cHV.lastUpdatedBy.login = ?#{principal.username}")
    List<CHV> findByLastUpdatedByIsCurrentUser();
}
