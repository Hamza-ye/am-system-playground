package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.Activity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Activity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("select activity from Activity activity where activity.user.login = ?#{principal.username}")
    List<Activity> findByUserIsCurrentUser();

    @Query("select activity from Activity activity where activity.lastUpdatedBy.login = ?#{principal.username}")
    List<Activity> findByLastUpdatedByIsCurrentUser();
}
