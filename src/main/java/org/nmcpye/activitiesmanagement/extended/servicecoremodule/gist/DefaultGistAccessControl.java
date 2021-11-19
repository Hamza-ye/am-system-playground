package org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.BaseIdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.PrimaryKeyObject;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Transform;
import org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistQuery.Comparison;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistQuery.Field;
import org.nmcpye.activitiesmanagement.extended.servicecoremodule.gist.GistQuery.Filter;
import org.nmcpye.activitiesmanagement.extended.user.UserServiceExt;
import org.nmcpye.activitiesmanagement.security.AuthoritiesConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.*;

/**
 * Encapsulates all access control related logic of gist API request processing.
 * <p>
 * An instance is always related to the current {@link User} of the currently
 * processed gist API request.
 */
public class DefaultGistAccessControl implements GistAccessControl {

    private static final Set<String> PUBLIC_USER_PROPERTY_PATHS = unmodifiableSet(
        new HashSet<>(
            asList("id", "code", "displayName", "name", "surname", "firstName", "userCredentials.username")));

    private static final Set<String> PUBLIC_USER_CREDENTIALS_PROPERTY_PATHS = singleton("username");

    private static final Set<String> PUBLIC_PROPERTY_PATHS = unmodifiableSet(
        new HashSet<>(asList("sharing", "access", "translations")));

    private final User currentUser;

    private final AclService aclService;

    private final UserServiceExt userServiceExt;

    private final GistService gistService;

    public DefaultGistAccessControl(User currentUser, AclService aclService,
                                    UserServiceExt userServiceExt, GistService gistService) {
        this.currentUser = currentUser;
        this.aclService = aclService;
        this.userServiceExt = userServiceExt;
        this.gistService = gistService;
    }

    @Override
    public String getCurrentUserUid() {
        return currentUser.getUid();
    }

    @Override
    public boolean isSuperuser() {
        if (currentUser.getPerson() != null) {
            return currentUser.getPerson().isSuper();
        }
        return currentUser.getAuthorities().stream().anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN));
    }

    @Override
    public boolean canReadHQL() {
        if (currentUser.getPerson() != null) {
            return currentUser.getPerson().isAuthorized("F_METADATA_EXPORT");
        }
        return isSuperuser();
    }

    @Override
    public boolean canRead(Class<? extends PrimaryKeyObject> type) {
        if (!IdentifiableObject.class.isAssignableFrom(type)) {
            return true;
        }
        @SuppressWarnings("unchecked")
        Class<? extends IdentifiableObject> ioType = (Class<? extends IdentifiableObject>) type;
        return aclService.canRead(currentUser, ioType);
    }

    @Override
    public boolean canReadObject(Class<? extends PrimaryKeyObject> type, String uid) {
        if (!IdentifiableObject.class.isAssignableFrom(type)) {
            return true;
        }
        @SuppressWarnings("unchecked")
        Class<? extends IdentifiableObject> ioType = (Class<? extends IdentifiableObject>) type;
        if (!aclService.isClassShareable(ioType)) {
            return aclService.canRead(currentUser, ioType);
        }
        List<?> res = gistService.gist(GistQuery.builder()
            .elementType(ioType)
            .autoType(GistAutoType.M)
            .fields(singletonList(new Field("sharing", Transform.NONE)))
            .filters(singletonList(new Filter("id", Comparison.EQ, uid)))
            .build());
//        Sharing sharing = res.isEmpty() ? new Sharing() : (Sharing) res.get( 0 );
        BaseIdentifiableObject object = new BaseIdentifiableObject();
//        object.setSharing( sharing );
        return aclService.canRead(currentUser, object, ioType);
    }

    @Override
    public boolean canRead(Class<? extends PrimaryKeyObject> type, String path) {
        if (!IdentifiableObject.class.isAssignableFrom(type)) {
            return true;
        }
        boolean isUserField = type == User.class;
        boolean isUserCredentialsField = type == Person.class;
        if (isUserField && PUBLIC_USER_PROPERTY_PATHS.contains(path)) {
            return true;
        }
        if (isUserCredentialsField && PUBLIC_USER_CREDENTIALS_PROPERTY_PATHS.contains(path)) {
            return true;
        }
        @SuppressWarnings("unchecked")
        Class<? extends IdentifiableObject> ioType = (Class<? extends IdentifiableObject>) type;
        return PUBLIC_PROPERTY_PATHS.contains(path) || aclService.canRead(currentUser, ioType);
    }

    @Override
    public boolean canFilterByAccessOfUser(String userUid) {
        User user = getCurrentUserUid().equals(userUid) ? currentUser : userServiceExt.getUser(userUid);
        return user != null && aclService.canRead(currentUser, user);
    }

//    @Override
//    public Access asAccess( Class<? extends IdentifiableObject> type, Sharing value )
//    {
//        BaseIdentifiableObject object = new BaseIdentifiableObject();
//        object.setSharing( value );
//        return aclService.getAccess( object, currentUser, type );
//    }

//    @Override
//    public String createAccessFilterHQL( String tableName )
//    {
//        return JpaQueryUtils.generateHqlQueryForSharingCheck( tableName, currentUser, AclService.LIKE_READ_METADATA );
//    }
}
