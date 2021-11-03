package org.nmcpye.activitiesmanagement.service;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;

/**
 * Service Interface for managing {@link PersonAuthorityGroup}.
 */
public interface PersonAuthorityGroupService {
    /**
     * Save a personAuthorityGroup.
     *
     * @param personAuthorityGroup the entity to save.
     * @return the persisted entity.
     */
    PersonAuthorityGroup save(PersonAuthorityGroup personAuthorityGroup);

    /**
     * Partially updates a personAuthorityGroup.
     *
     * @param personAuthorityGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonAuthorityGroup> partialUpdate(PersonAuthorityGroup personAuthorityGroup);

    /**
     * Get all the personAuthorityGroups.
     *
     * @return the list of entities.
     */
    List<PersonAuthorityGroup> findAll();

    /**
     * Get the "id" personAuthorityGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonAuthorityGroup> findOne(Long id);

    /**
     * Delete the "id" personAuthorityGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
