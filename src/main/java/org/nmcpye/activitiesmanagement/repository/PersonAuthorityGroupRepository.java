package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.person.PersonAuthorityGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonAuthorityGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonAuthorityGroupRepository extends JpaRepository<PersonAuthorityGroup, Long> {
    @Query(
        "select personAuthorityGroup from PersonAuthorityGroup personAuthorityGroup where personAuthorityGroup.user.login = ?#{principal.username}"
    )
    List<PersonAuthorityGroup> findByUserIsCurrentUser();

    @Query(
        "select personAuthorityGroup from PersonAuthorityGroup personAuthorityGroup where personAuthorityGroup.lastUpdatedBy.login = ?#{principal.username}"
    )
    List<PersonAuthorityGroup> findByLastUpdatedByIsCurrentUser();
}
