package org.nmcpye.activitiesmanagement.extended.common;

import org.nmcpye.activitiesmanagement.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface IdentifiableObjectManager {
    String ID = IdentifiableObjectManager.class.getName();

    void save(IdentifiableObject object);

    void save(IdentifiableObject object, boolean clearSharing);

    void save(List<IdentifiableObject> objects);

    void update(IdentifiableObject object);

    void update(IdentifiableObject object, User user);

    void update(List<IdentifiableObject> objects);

    void update(List<IdentifiableObject> objects, User user);

    void delete(IdentifiableObject object);

    void delete(IdentifiableObject object, User user);

    <T extends IdentifiableObject> T get(String uid);

    <T extends IdentifiableObject> T get(Class<T> clazz, long id);

    <T extends IdentifiableObject> T get(Class<T> clazz, String uid);

    <T extends IdentifiableObject> boolean exists(Class<T> clazz, String uid);

    <T extends IdentifiableObject> T get(Collection<Class<? extends IdentifiableObject>> classes, String uid);

    <T extends IdentifiableObject> T get(Collection<Class<? extends IdentifiableObject>> classes, IdScheme idScheme, String value);

    <T extends IdentifiableObject> T getByCode(Class<T> clazz, String code);

    <T extends IdentifiableObject> List<T> getByCode(Class<T> clazz, Collection<String> codes);

    <T extends IdentifiableObject> T getByName(Class<T> clazz, String name);

    <T extends IdentifiableObject> T search(Class<T> clazz, String query);

    <T extends IdentifiableObject> List<T> filter(Class<T> clazz, String query);

    <T extends IdentifiableObject> List<T> getAll(Class<T> clazz);

    <T extends IdentifiableObject> List<T> getDataWriteAll(Class<T> clazz);

    <T extends IdentifiableObject> List<T> getDataReadAll(Class<T> clazz);

    <T extends IdentifiableObject> List<T> getAllSorted(Class<T> clazz);

    <T extends IdentifiableObject> List<T> getByUid(Class<T> clazz, Collection<String> uids);

    <T extends IdentifiableObject> List<T> getById(Class<T> clazz, Collection<Long> ids);

    <T extends IdentifiableObject> List<T> getByUidOrdered(Class<T> clazz, List<String> uids);

    <T extends IdentifiableObject> List<T> getLikeName(Class<T> clazz, String name);

    <T extends IdentifiableObject> List<T> getLikeName(Class<T> clazz, String name, boolean caseSensitive);

    <T extends IdentifiableObject> List<T> getBetweenSorted(Class<T> clazz, int first, int max);

    <T extends IdentifiableObject> List<T> getBetweenLikeName(Class<T> clazz, Set<String> words, int first, int max);

    <T extends IdentifiableObject> Date getLastUpdated(Class<T> clazz);

    <T extends IdentifiableObject> Map<String, T> getIdMap(Class<T> clazz, IdentifiableProperty property);

    <T extends IdentifiableObject> Map<String, T> getIdMap(Class<T> clazz, IdScheme idScheme);

    <T extends IdentifiableObject> Map<String, T> getIdMapNoAcl(Class<T> clazz, IdentifiableProperty property);

    <T extends IdentifiableObject> Map<String, T> getIdMapNoAcl(Class<T> clazz, IdScheme idScheme);

    <T extends IdentifiableObject> List<T> getObjects(Class<T> clazz, IdentifiableProperty property, Collection<String> identifiers);

    <T extends IdentifiableObject> List<T> getObjects(Class<T> clazz, Collection<Long> identifiers);

    <T extends IdentifiableObject> T getObject(Class<T> clazz, IdentifiableProperty property, String value);

    <T extends IdentifiableObject> T getObject(Class<T> clazz, IdScheme idScheme, String value);

    IdentifiableObject getObject(String uid, String simpleClassName);

    IdentifiableObject getObject(long id, String simpleClassName);

    <T extends IdentifiableObject> int getCount(Class<T> clazz);

    <T extends IdentifiableObject> int getCountByCreated(Class<T> clazz, Date created);

    <T extends IdentifiableObject> int getCountByLastUpdated(Class<T> clazz, Date lastUpdated);

    <T extends DimensionalObject> List<T> getDataDimensions(Class<T> clazz);

    <T extends DimensionalObject> List<T> getDataDimensionsNoAcl(Class<T> clazz);

    <T extends IdentifiableObject> Page<T> findAll(Class<T> clazz, Pageable pageable);

    void refresh(Object object);

    void flush();

    void evict(Object object);

    Map<Class<? extends IdentifiableObject>, IdentifiableObject> getDefaults();

    //    void updateTranslations(IdentifiableObject persistedObject, Set<Translation> translations);

    <T extends IdentifiableObject> List<T> get(Class<T> clazz, Collection<String> uids);

    <T extends IdentifiableObject> List<T> getNoAcl(Class<T> clazz, Collection<String> uids);

    boolean isDefault(IdentifiableObject object);

    List<String> getUidsCreatedBefore(Class<? extends IdentifiableObject> klass, Date date);

    // -------------------------------------------------------------------------
    // NO ACL
    // -------------------------------------------------------------------------

    <T extends IdentifiableObject> T getNoAcl(Class<T> clazz, String uid);

    <T extends IdentifiableObject> void updateNoAcl(T object);

    <T extends IdentifiableObject> List<T> getAllNoAcl(Class<T> clazz);
}
