package org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;

import java.util.List;

public interface AclService {
    String LIKE_READ_METADATA = "r%"; // TODO use r_______ ?

    String LIKE_WRITE_METADATA = "_w%"; // TODO use r_______ ?

    String LIKE_READ_DATA = "__r_____";

    String LIKE_WRITE_DATA = "___w____";

    /**
     * Is type supported for acl?
     *
     * @param type Type to check
     * @return true if type is supported
     */
    boolean isSupported(String type);

    /**
     * Is class supported for acl?
     *
     * @param object Object to check
     * @return true if type is supported
     */
    boolean isSupported(IdentifiableObject object);

    <T extends IdentifiableObject> boolean isClassShareable(Class<T> klass);

    /**
     * Is class supported for data acl?
     *
     * @param object Object to check
     * @return true if type is supported
     */
    boolean isDataShareable(IdentifiableObject object);

    <T extends IdentifiableObject> boolean isDataClassShareable(Class<T> klass);

    /**
     * Is type supported for sharing?
     *
     * @param type Type to check
     * @return true if type is supported
     */
    boolean isShareable(String type);

    /**
     * Is class supported for sharing?
     *
     * @param o Object to check
     * @return true if type is supported
     */
    boolean isShareable(IdentifiableObject o);

    /**
     * Can user read this object
     * <p/>
     * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority? 2. Is the user for
     * the object null? 3. Is the user of the object equal to current user? 4.
     * Is the object public read? 5. Does any of the userGroupAccesses contain
     * public read and the current user is in that group
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    boolean canRead(User user, IdentifiableObject object);

    /**
     * Same as {@link #canRead(User, IdentifiableObject)} except that it allows
     * to pass superclasses as object that can be validated as if they are of a
     * certain object type.
     *
     * @param user    User to check against
     * @param object  Object to check, could be
     *                {@link org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject}
     * @param objType the type as which to regard the provided object
     * @param <T>     type of the passed object
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean canRead(User user, T object, Class<? extends T> objType);

    /**
     * Can user read data this object.
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    boolean canDataRead(User user, IdentifiableObject object);

    /**
     * Check if the given user has data or metadata permission over the given
     * object
     * <p>
     * Data-read permission is only considered if the given object's schema is
     * 'DataShareable'. If not 'DataShareable', only metadata-read ACL is
     * considered
     *
     * @param user   User to check against
     * @param object Object to check permission
     * @return true, if use can access object
     */
    boolean canDataOrMetadataRead(User user, IdentifiableObject object);

    /**
     * Can user write to this object (create)
     * <p/>
     * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority? 2. Is the user for
     * the object null? 3. Is the user of the object equal to current user? 4.
     * Is the object public write? 5. Does any of the userGroupAccesses contain
     * public write and the current user is in that group
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    boolean canWrite(User user, IdentifiableObject object);

    /**
     * Can user write data to this object (create)
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    boolean canDataWrite(User user, IdentifiableObject object);

    /**
     * Can user update this object
     * <p/>
     * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority? 2. Can user write
     * to this object?
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    boolean canUpdate(User user, IdentifiableObject object);

    /**
     * Can user delete this object
     * <p/>
     * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority? 2. Can user write
     * to this object?
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    boolean canDelete(User user, IdentifiableObject object);

    /**
     * Can user manage (make public) this object
     * <p/>
     * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority? 2. Can user write
     * to this object?
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    boolean canManage(User user, IdentifiableObject object);

    /**
     * Can read an objects of this type.
     *
     * @param user  User to User to check against
     * @param klass Type to check against
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean canRead(User user, Class<T> klass);

    /**
     * Can create an object of this type.
     *
     * @param user  User to User to check against
     * @param klass Type to check against
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean canCreate(User user, Class<T> klass);

    /**
     * Checks if a user can create a public instance of a certain object.
     * <p/>
     * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority? 2. Does user have
     * the authority to create public instances of that object
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean canMakePublic(User user, T object);

    <T extends IdentifiableObject> boolean canMakeClassPublic(User user, Class<T> klass);

    /**
     * Checks if a user can create a private instance of a certain object.
     * <p/>
     * 1. Does user have ACL_OVERRIDE_AUTHORITIES authority? 2. Does user have
     * the authority to create private instances of that object
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean canMakePrivate(User user, T object);

    <T extends IdentifiableObject> boolean canMakeClassPrivate(User user, Class<T> klass);

    /**
     * Can user make this object external? (read with no login)
     *
     * @param user   User to check against
     * @param object Object to check
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean canMakeExternal(User user, T object);

    <T extends IdentifiableObject> boolean canMakeClassExternal(User user, Class<T> klass);

    /**
     * Is the default for this type to be private?
     *
     * @param klass Type to check
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean defaultPrivate(Class<T> klass);

    /**
     * Is the default for this type to be public?
     *
     * @param object Object to check
     * @return Result of test
     */
    <T extends IdentifiableObject> boolean defaultPublic(T object);

    /**
     * Returns the class type corresponding to the given class type.
     *
     * @param type the singular class type name.
     * @return the class.
     */
    Class<? extends IdentifiableObject> classForType(String type);

    /**
     * Sets default sharing props on object, disregarding what is already there.
     *
     * @param object Object to update
     * @param user   User to base ACL on
     */
    <T extends IdentifiableObject> void resetSharing(T object, User user);

    /**
     * Clears all sharing information on the given object, and sets the owner to
     * the given user.
     *
     * @param object the object.
     * @param user   the user.
     */
    <T extends IdentifiableObject> void clearSharing(T object, User user);

    /**
     * Verify that sharing props are correctly set according to user.
     *
     * @param object Object to update
     * @param user   User to base ACL on
     * @return List of error reports (if any)
     */
    <T extends IdentifiableObject> List<ErrorReport> verifySharing(T object, User user);
}
