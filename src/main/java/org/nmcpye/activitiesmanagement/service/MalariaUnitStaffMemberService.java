package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.MalariaUnitStaffMember;

/**
 * Service Interface for managing {@link MalariaUnitStaffMember}.
 */
public interface MalariaUnitStaffMemberService {
    /**
     * Save a malariaUnitStaffMember.
     *
     * @param malariaUnitStaffMember the entity to save.
     * @return the persisted entity.
     */
    MalariaUnitStaffMember save(MalariaUnitStaffMember malariaUnitStaffMember);

    /**
     * Partially updates a malariaUnitStaffMember.
     *
     * @param malariaUnitStaffMember the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MalariaUnitStaffMember> partialUpdate(MalariaUnitStaffMember malariaUnitStaffMember);

    /**
     * Get all the malariaUnitStaffMembers.
     *
     * @return the list of entities.
     */
    List<MalariaUnitStaffMember> findAll();

    /**
     * Get the "id" malariaUnitStaffMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MalariaUnitStaffMember> findOne(Long id);

    /**
     * Delete the "id" malariaUnitStaffMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
