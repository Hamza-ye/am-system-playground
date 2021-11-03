package org.nmcpye.activitiesmanagement.extended.service;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.nmcpye.activitiesmanagement.extended.common.ReflectionUtils.getRealClass;

import com.google.common.collect.ImmutableMap;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.*;
import org.nmcpye.activitiesmanagement.extended.common.exception.InvalidIdentifierReferenceException;
import org.nmcpye.activitiesmanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Note that it is required for nameable object stores to have concrete implementation
 * classes, not rely on the HibernateIdentifiableObjectStore class, in order to
 * be injected as nameable object stores.
 *
 */
@Component("org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectManager")
public class DefaultIdentifiableObjectManager implements IdentifiableObjectManager {

    private final Logger log = LoggerFactory.getLogger(DefaultIdentifiableObjectManager.class);

    private final Set<IdentifiableObjectStore<? extends IdentifiableObject>> identifiableObjectStores;

    private final Set<GenericDimensionalObjectStore<? extends DimensionalObject>> dimensionalObjectStores;

    private final UserService userService;

    private Map<Class<? extends IdentifiableObject>, IdentifiableObjectStore<? extends IdentifiableObject>> identifiableObjectStoreMap;

    private Map<Class<? extends DimensionalObject>, GenericDimensionalObjectStore<? extends DimensionalObject>> dimensionalObjectStoreMap;

    @PersistenceContext
    EntityManager entityManager;

    public DefaultIdentifiableObjectManager(
        Set<IdentifiableObjectStore<? extends IdentifiableObject>> identifiableObjectStores,
        Set<GenericDimensionalObjectStore<? extends DimensionalObject>> dimensionalObjectStores,
        UserService userService
    ) {
        checkNotNull(identifiableObjectStores);
        checkNotNull(dimensionalObjectStores);
        checkNotNull(userService);

        this.dimensionalObjectStores = dimensionalObjectStores;
        this.identifiableObjectStores = identifiableObjectStores;
        this.userService = userService;
    }

    //--------------------------------------------------------------------------
    // IdentifiableObjectManager implementation
    //--------------------------------------------------------------------------

    @Override
    @Transactional
    public void save(IdentifiableObject object) {
        save(object, true);
    }

    @Override
    @Transactional
    public void save(IdentifiableObject object, boolean clearSharing) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(object.getClass());

        if (store != null) {
            store.save(object, clearSharing);
        }
    }

    @Override
    @Transactional
    public void save(List<IdentifiableObject> objects) {
        objects.forEach(o -> save(o, true));
    }

    @Override
    @Transactional
    public void update(IdentifiableObject object) {
        update(object, userService.getUserWithAuthorities().orElse(null));
    }

    @Override
    @Transactional
    public void update(IdentifiableObject object, User user) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(object.getClass());

        if (store != null) {
            store.update(object, user);
        }
    }

    @Override
    @Transactional
    public void update(List<IdentifiableObject> objects) {
        update(objects, userService.getUserWithAuthorities().orElse(null));
    }

    @Override
    @Transactional
    public void update(List<IdentifiableObject> objects, User user) {
        if (objects == null || objects.isEmpty()) {
            return;
        }

        for (IdentifiableObject object : objects) {
            update(object, user);
        }
    }

    //    @Override
    //    @Transactional
    //    public void updateTranslations( IdentifiableObject persistedObject, Set<Translation> translations )
    //    {
    //        Session session = sessionFactory.getCurrentSession();
    //        persistedObject.getTranslations().clear();
    //        session.flush();
    //
    //        translations.forEach( translation ->
    //        {
    //            if ( StringUtils.isNotEmpty( translation.getValue() ) )
    //            {
    //                persistedObject.getTranslations().add( translation );
    //            }
    //        } );
    //
    //        BaseIdentifiableObject translatedObject = (BaseIdentifiableObject) persistedObject;
    //        translatedObject.setLastUpdated( new Date() );
    //        translatedObject.setLastUpdatedBy( userService.getCurrentUser() );
    //
    //        session.update( translatedObject );
    //    }

    @Override
    @Transactional
    public void delete(IdentifiableObject object) {
        delete(object, userService.getUserWithAuthorities().orElse(null));
    }

    @Override
    @Transactional
    public void delete(IdentifiableObject object, User user) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(object.getClass());

        if (store != null) {
            store.delete(object, user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T get(String uid) {
        for (IdentifiableObjectStore<? extends IdentifiableObject> store : identifiableObjectStores) {
            T object = (T) store.getByUid(uid);

            if (object != null) {
                return object;
            }
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T get(Class<T> clazz, long id) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (T) store.get(id);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T get(Class<T> clazz, String uid) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (T) store.getByUid(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> boolean exists(Class<T> clazz, String uid) {
        return get(clazz, uid) != null;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T get(Collection<Class<? extends IdentifiableObject>> classes, String uid) {
        for (Class<? extends IdentifiableObject> clazz : classes) {
            T object = (T) get(clazz, uid);

            if (object != null) {
                return object;
            }
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T get(
        Collection<Class<? extends IdentifiableObject>> classes,
        IdScheme idScheme,
        String identifier
    ) {
        for (Class<? extends IdentifiableObject> clazz : classes) {
            T object = (T) getObject(clazz, idScheme, identifier);

            if (object != null) {
                return object;
            }
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> get(Class<T> clazz, Collection<String> uids) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (List<T>) store.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getNoAcl(Class<T> clazz, Collection<String> uids) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (List<T>) store.getByUidNoAcl(uids);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T getByCode(Class<T> clazz, String code) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (T) store.getByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T getByName(Class<T> clazz, String name) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (T) store.getByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> T search(Class<T> clazz, String query) {
        T object = get(clazz, query);

        if (object == null) {
            object = getByCode(clazz, query);
        }

        if (object == null) {
            object = getByName(clazz, query);
        }

        return object;
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> List<T> filter(Class<T> clazz, String query) {
        Set<T> uniqueObjects = new HashSet<>();

        T uidObject = get(clazz, query);

        if (uidObject != null) {
            uniqueObjects.add(uidObject);
        }

        T codeObject = getByCode(clazz, query);

        if (codeObject != null) {
            uniqueObjects.add(codeObject);
        }

        uniqueObjects.addAll(getLikeName(clazz, query, false));

        List<T> objects = new ArrayList<>(uniqueObjects);

        Collections.sort(objects);

        return objects;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getAll(Class<T> clazz) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getDataWriteAll(Class<T> clazz) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getDataWriteAll();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getDataReadAll(Class<T> clazz) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getDataReadAll();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getAllSorted(Class<T> clazz) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getAllOrderedName();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getByUid(Class<T> clazz, Collection<String> uids) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getByUid(uids);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getById(Class<T> clazz, Collection<Long> ids) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (List<T>) store.getById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getByCode(Class<T> clazz, Collection<String> codes) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getByCode(codes);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getByUidOrdered(Class<T> clazz, List<String> uids) {
        IdentifiableObjectStore<T> store = (IdentifiableObjectStore<T>) getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        List<T> list = new ArrayList<>();

        if (uids != null) {
            for (String uid : uids) {
                T object = store.getByUid(uid);

                if (object != null) {
                    list.add(object);
                }
            }
        }

        return list;
    }

    @Override
    @Transactional
    public <T extends IdentifiableObject> int getCount(Class<T> clazz) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store != null) {
            return store.getCount();
        }

        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> int getCountByCreated(Class<T> clazz, Date created) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store != null) {
            return store.getCountGeCreated(created);
        }

        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> int getCountByLastUpdated(Class<T> clazz, Date lastUpdated) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store != null) {
            return store.getCountGeLastUpdated(lastUpdated);
        }

        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getLikeName(Class<T> clazz, String name) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getAllLikeName(name);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getLikeName(Class<T> clazz, String name, boolean caseSensitive) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getAllLikeName(name, caseSensitive);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getBetweenSorted(Class<T> clazz, int first, int max) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getAllOrderedName(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getBetweenLikeName(Class<T> clazz, Set<String> words, int first, int max) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getAllLikeName(words, first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> Date getLastUpdated(Class<T> clazz) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return store.getLastUpdated();
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> Map<String, T> getIdMap(Class<T> clazz, IdentifiableProperty property) {
        return getIdMap(clazz, IdScheme.from(property));
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> Map<String, T> getIdMap(Class<T> clazz, IdScheme idScheme) {
        IdentifiableObjectStore<T> store = (IdentifiableObjectStore<T>) getIdentifiableObjectStore(clazz);

        Map<String, T> map = new HashMap<>();

        if (store == null) {
            return map;
        }

        List<T> objects = store.getAll();

        return IdentifiableObjectUtils.getIdMap(objects, idScheme);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> Map<String, T> getIdMapNoAcl(Class<T> clazz, IdentifiableProperty property) {
        return getIdMapNoAcl(clazz, IdScheme.from(property));
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> Map<String, T> getIdMapNoAcl(Class<T> clazz, IdScheme idScheme) {
        IdentifiableObjectStore<T> store = (IdentifiableObjectStore<T>) getIdentifiableObjectStore(clazz);

        Map<String, T> map = new HashMap<>();

        if (store == null) {
            return map;
        }

        List<T> objects = store.getAllNoAcl();

        return IdentifiableObjectUtils.getIdMap(objects, idScheme);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getObjects(
        Class<T> clazz,
        IdentifiableProperty property,
        Collection<String> identifiers
    ) {
        IdentifiableObjectStore<T> store = (IdentifiableObjectStore<T>) getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        if (identifiers != null && !identifiers.isEmpty()) {
            if (property == null || IdentifiableProperty.UID.equals(property)) {
                return store.getByUid(identifiers);
            } else if (IdentifiableProperty.CODE.equals(property)) {
                return store.getByCode(identifiers);
            } else if (IdentifiableProperty.NAME.equals(property)) {
                return store.getByName(identifiers);
            }

            throw new InvalidIdentifierReferenceException("Invalid identifiable property / class combination: " + property);
        }

        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getObjects(Class<T> clazz, Collection<Long> identifiers) {
        IdentifiableObjectStore<T> store = (IdentifiableObjectStore<T>) getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return store.getById(identifiers);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends IdentifiableObject> T getObject(Class<T> clazz, IdentifiableProperty property, String value) {
        return getObject(clazz, IdScheme.from(property), value);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T getObject(Class<T> clazz, IdScheme idScheme, String value) {
        IdentifiableObjectStore<T> store = (IdentifiableObjectStore<T>) getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        if (!StringUtils.isEmpty(value)) {
            if (idScheme.isNull() || idScheme.is(IdentifiableProperty.UID)) {
                return store.getByUid(value);
            } else if (idScheme.is(IdentifiableProperty.CODE)) {
                return store.getByCode(value);
            } else if (idScheme.is(IdentifiableProperty.NAME)) {
                return store.getByName(value);
            } else if (idScheme.is(IdentifiableProperty.ID)) {
                if (Integer.valueOf(value) > 0) {
                    return store.get(Long.valueOf(value));
                }
            }

            throw new InvalidIdentifierReferenceException("Invalid identifiable property / class combination: " + idScheme);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public IdentifiableObject getObject(String uid, String simpleClassName) {
        for (IdentifiableObjectStore<? extends IdentifiableObject> objectStore : identifiableObjectStores) {
            if (simpleClassName.equals(objectStore.getClazz().getSimpleName())) {
                return objectStore.getByUid(uid);
            }
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public IdentifiableObject getObject(long id, String simpleClassName) {
        for (IdentifiableObjectStore<? extends IdentifiableObject> objectStore : identifiableObjectStores) {
            if (simpleClassName.equals(objectStore.getClazz().getSimpleName())) {
                return objectStore.get(id);
            }
        }

        return null;
    }

    @Override
    @Transactional
    public void refresh(Object object) {
        entityManager.unwrap(Session.class).refresh(object);
    }

    @Override
    @Transactional
    public void flush() {
        entityManager.unwrap(Session.class).flush();
    }

    @Override
    @Transactional
    public void evict(Object object) {
        entityManager.unwrap(Session.class).evict(object);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> T getNoAcl(Class<T> clazz, String uid) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return null;
        }

        return (T) store.getByUidNoAcl(uid);
    }

    @Override
    @Transactional
    public <T extends IdentifiableObject> void updateNoAcl(T object) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(object.getClass());

        if (store != null) {
            store.updateNoAcl(object);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> List<T> getAllNoAcl(Class<T> clazz) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getAllNoAcl();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends DimensionalObject> List<T> getDataDimensions(Class<T> clazz) {
        GenericDimensionalObjectStore<DimensionalObject> store = getDimensionalObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getByDataDimension(true);
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends DimensionalObject> List<T> getDataDimensionsNoAcl(Class<T> clazz) {
        GenericDimensionalObjectStore<DimensionalObject> store = getDimensionalObjectStore(clazz);

        if (store == null) {
            return new ArrayList<>();
        }

        return (List<T>) store.getByDataDimensionNoAcl(true);
    }

    @Override
    public Map<Class<? extends IdentifiableObject>, IdentifiableObject> getDefaults() {
        return new ImmutableMap.Builder<Class<? extends IdentifiableObject>, IdentifiableObject>()
            //            .put( Category.class, DEFAULT_OBJECT_CACHE.get( Category.class.getName(), key -> HibernateUtils.initializeProxy( getByName( Category.class, "default" ) ) ).orElse( null ) )
            //            .put( CategoryCombo.class, DEFAULT_OBJECT_CACHE.get( CategoryCombo.class.getName(), key -> HibernateUtils.initializeProxy( getByName( CategoryCombo.class, "default" ) ) ).orElse( null ) )
            //            .put( CategoryOption.class, DEFAULT_OBJECT_CACHE.get( CategoryOption.class.getName(), key -> HibernateUtils.initializeProxy( getByName( CategoryOption.class, "default" ) ) ).orElse( null ) )
            //            .put( CategoryOptionCombo.class, DEFAULT_OBJECT_CACHE.get( CategoryOptionCombo.class.getName(), key -> HibernateUtils.initializeProxy( getByName( CategoryOptionCombo.class, "default" ) ) ).orElse( null ) )
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getUidsCreatedBefore(Class<? extends IdentifiableObject> klass, Date date) {
        IdentifiableObjectStore<IdentifiableObject> store = getIdentifiableObjectStore(klass);

        if (store == null) {
            return new ArrayList<>();
        }

        return store.getUidsCreatedBefore(date);
    }

    //--------------------------------------------------------------------------
    // Supportive methods
    //--------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(IdentifiableObject object) {
        Map<Class<? extends IdentifiableObject>, IdentifiableObject> defaults = getDefaults();

        if (object == null) {
            return false;
        }

        Class<?> realClass = getRealClass(object.getClass());
        if (!defaults.containsKey(realClass)) {
            return false;
        }

        IdentifiableObject defaultObject = defaults.get(realClass);

        return defaultObject != null && defaultObject.getUid().equals(object.getUid());
    }

    @SuppressWarnings("unchecked")
    private <T extends IdentifiableObject> IdentifiableObjectStore<IdentifiableObject> getIdentifiableObjectStore(Class<T> clazz) {
        initMaps();

        IdentifiableObjectStore<? extends IdentifiableObject> store = identifiableObjectStoreMap.get(clazz);

        if (store == null) {
            store = identifiableObjectStoreMap.get(clazz.getSuperclass());

            if (store == null && !Person.class.isAssignableFrom(clazz)) {
                log.debug("No IdentifiableObjectStore found for class: " + clazz);
            }
            if (store == null) {
                log.debug("No IdentifiableObjectStore found for class: " + clazz);
            }
        }

        return (IdentifiableObjectStore<IdentifiableObject>) store;
    }

    @SuppressWarnings("unchecked")
    private <T extends DimensionalObject> GenericDimensionalObjectStore<DimensionalObject> getDimensionalObjectStore(Class<T> clazz) {
        initMaps();

        GenericDimensionalObjectStore<? extends DimensionalObject> store = dimensionalObjectStoreMap.get(clazz);

        if (store == null) {
            store = dimensionalObjectStoreMap.get(clazz.getSuperclass());

            if (store == null) {
                log.debug("No DimensionalObjectStore found for class: " + clazz);
            }
        }

        return (GenericDimensionalObjectStore<DimensionalObject>) store;
    }

    private void initMaps() {
        if (identifiableObjectStoreMap != null) {
            return; // Already initialized
        }

        identifiableObjectStoreMap = new HashMap<>();

        for (IdentifiableObjectStore<? extends IdentifiableObject> store : identifiableObjectStores) {
            identifiableObjectStoreMap.put(store.getClazz(), store);
        }
        dimensionalObjectStoreMap = new HashMap<>();

        for (GenericDimensionalObjectStore<? extends DimensionalObject> store : dimensionalObjectStores) {
            dimensionalObjectStoreMap.put(store.getClazz(), store);
        }
    }
}
