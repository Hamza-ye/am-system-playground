package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.Family;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Family entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {
    @Query("select family from Family family where family.user.login = ?#{principal.username}")
    List<Family> findByUserIsCurrentUser();

    @Query("select family from Family family where family.lastUpdatedBy.login = ?#{principal.username}")
    List<Family> findByLastUpdatedByIsCurrentUser();
}
