package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.FamilyHead;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FamilyHead entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyHeadRepository extends JpaRepository<FamilyHead, Long> {
    @Query("select familyHead from FamilyHead familyHead where familyHead.user.login = ?#{principal.username}")
    List<FamilyHead> findByUserIsCurrentUser();

    @Query("select familyHead from FamilyHead familyHead where familyHead.lastUpdatedBy.login = ?#{principal.username}")
    List<FamilyHead> findByLastUpdatedByIsCurrentUser();
}
