package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.MalariaUnitStaffMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MalariaUnitStaffMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MalariaUnitStaffMemberRepository extends JpaRepository<MalariaUnitStaffMember, Long> {
    @Query(
        "select malariaUnitStaffMember from MalariaUnitStaffMember malariaUnitStaffMember where malariaUnitStaffMember.createdBy.login = ?#{principal.username}"
    )
    List<MalariaUnitStaffMember> findByUserIsCurrentUser();

    @Query(
        "select malariaUnitStaffMember from MalariaUnitStaffMember malariaUnitStaffMember where malariaUnitStaffMember.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<MalariaUnitStaffMember> findByLastUpdatedByIsCurrentUser();
}
