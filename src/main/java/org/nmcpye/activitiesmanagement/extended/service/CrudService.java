package org.nmcpye.activitiesmanagement.extended.service;

import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hamza on 26/04/2021.
 */
public interface CrudService<T extends BaseIdentifiableObject, ID extends Serializable> {


    /**
     * Adds an entity.
     *
     * @param entity the entity to add.
     * @return a generated unique id of the added entity.
     */
    Long add(T entity);

    /**
     * Updates an Entity.
     *
     * @param entity the entity to update.
     */
    void update(T entity);

    /**
     * Deletes an entity.
     *
     * @param entity the entity to delete.
     */
    void delete(T entity);

    /**
     * Returns an entity.
     *
     * @param id the id of the entity.
     * @return the entity with the given id, or null if no match.
     */
    T get(ID id);

    /**
     * Returns the entity with the given UID.
     *
     * @param uid the UID of the entity.
     * @return the entity with the given UID, or null if no match.
     */
    T getByUid(String uid);

    /**
     * Returns the entity with the given code.
     *
     * @param code the code of the OrganisationUnit to return.
     * @return the entity with the given code, or null if no match.
     */
    T getByCode(String code);

    /**
     * Returns all entity.
     *
     * @return a list of all the entity, or an empty
     * list if no entity exists.
     */
    List<T> getAll();

    /**
     * Returns all entities with corresponding identifiers.
     *
     * @param identifiers the collection of identifiers.
     * @return a list of entities.
     */
    List<T> getById(Collection<Long> identifiers);

    /**
     * Returns all entities with corresponding identifiers.
     *
     * @param uids the collection of uids.
     * @return a list of entities.
     */
    List<T> getByUid(Collection<String> uids);

    /**
     * Returns all entities with a given names.
     *
     * @param names the names of the OrganisationUnits to return.
     * @return the entities with the one of the given name, or null if not match.
     */
    List<T> getByName(Collection<String> names);

    /**
     * Returns the entity with a given name.
     *
     * @param name the name of the OrganisationUnit to return.
     * @return the entities with the given name, or null if not match.
     */
    T getByName(String name);

    /**
     * Retrieves a List of objects where the name is like the given name.
     *
     * @param name the name.
     * @return a List of objects.
     */
    List<T> getAllLikeName(String name);

    /**
     * Returns all entities sorted by the given options.
     *
     * @param sort
     * @return all entities sorted by the given options
     */
    Iterable<T> findAll(Sort sort);

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable
     * @return a page of entities
     */
    Page<T> findAll(Pageable pageable);
}
