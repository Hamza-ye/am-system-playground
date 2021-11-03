package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Person entity.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("select person from Person person where person.user.login = ?#{principal.username}")
    List<Person> findByUserIsCurrentUser();

    @Query("select person from Person person where person.lastUpdatedBy.login = ?#{principal.username}")
    List<Person> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct person from Person person left join fetch person.organisationUnits left join fetch person.dataViewOrganisationUnits left join fetch person.personAuthorityGroups",
        countQuery = "select count(distinct person) from Person person"
    )
    Page<Person> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct person from Person person left join fetch person.organisationUnits left join fetch person.dataViewOrganisationUnits left join fetch person.personAuthorityGroups"
    )
    List<Person> findAllWithEagerRelationships();

    @Query(
        "select person from Person person left join fetch person.organisationUnits left join fetch person.dataViewOrganisationUnits left join fetch person.personAuthorityGroups where person.id =:id"
    )
    Optional<Person> findOneWithEagerRelationships(@Param("id") Long id);
}
