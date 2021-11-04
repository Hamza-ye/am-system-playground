package org.nmcpye.activitiesmanagement.extended.person;

import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hamza on 20/10/2021.
 */
public interface PersonStore extends IdentifiableObjectStore<Person> {
    String ID = PersonStore.class.getName();

    /**
     * Retrieves the Person associated with the User with the given
     * name.
     *
     * @param username the name of the User.
     * @return the Person.
     */
    Person getPersonByLogin(String username);

    /**
     * Retrieves the Person associated with the User with the given
     * UUID.
     *
     * @param uuid UUID.
     * @return the Person.
     */
    Person getPersonByUuid(UUID uuid);

    //////////////////////////////////////
    //
    // Extended PersonStore Methods
    //
    //////////////////////////////////////

    /**
     * Returns a list of people based on the given query parameters.
     *
     *
     * @param params the person query parameters.
     * @return a List of people.
     */
    List<Person> getPeople(PersonQueryParams params);

    /**
     * Returns a list of people based on the given query parameters.
     * If the specified list of orders are empty, default order of
     * last name and first name will be applied.
     *
     * @param params the person query parameters.
     * @param orders the already validated order strings (e.g. email:asc).
     * @return a List of people.
     */
    List<Person> getPeople(PersonQueryParams params, @Nullable List<String> orders);

    /**
     * Returns the number of people based on the given query parameters.
     *
     * @param params the person query parameters.
     * @return number of people.
     */
    int getPersonCount(PersonQueryParams params);

    /**
     * Returns number of all people
     * @return number of people
     */
    int getPersonCount();

    List<Person> getExpiringPeople(PersonQueryParams personQueryParams);

    /**
     * Returns Person with given personId.
     *
     * @param personId UserId
     * @return User with given personId
     */
    Person getPerson(Long personId);
}
