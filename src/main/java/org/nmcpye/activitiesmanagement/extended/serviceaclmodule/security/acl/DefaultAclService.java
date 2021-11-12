package org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.PeopleGroup;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.feedback.ErrorReport;
import org.nmcpye.activitiesmanagement.extended.hibernate.HibernateProxyUtils;
import org.nmcpye.activitiesmanagement.extended.schemamodule.Schema;
import org.nmcpye.activitiesmanagement.extended.schemamodule.SchemaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default ACL implementation that uses SchemaDescriptors to get authorities /
 * sharing flags.
 */
@Service("org.nmcpye.activitiesmanagement.extended.serviceaclmodule.security.acl.AclService")
public class DefaultAclService implements AclService {
    public static final String INPUT_OBJECT_CAN_T_BE_OF_TYPE_CLASS = "Input object can't be of type Class!";

    private final SchemaService schemaService;

    public DefaultAclService(SchemaService schemaService) {
        checkNotNull(schemaService);

        this.schemaService = schemaService;
    }

    @Override
    public boolean isSupported(String type) {
        return schemaService.getSchemaBySingularName(type) != null;
    }

    @Override
    public boolean isSupported(IdentifiableObject object) {
        return schemaService.getSchema(HibernateProxyUtils.getRealClass(object)) != null;
    }

    @Override
    public boolean isShareable(String type) {
        Schema schema = schemaService.getSchemaBySingularName(type);
        return schema != null && schema.isShareable();
    }

    @Override
    public boolean isShareable(IdentifiableObject object) {
        Schema schema = schemaService.getSchema(HibernateProxyUtils.getRealClass(object));
        return schema != null && schema.isShareable();
    }

    @Override
    public <T extends IdentifiableObject> boolean isClassShareable(Class<T> klass) {
        Schema schema = schemaService.getSchema(klass);
        return schema != null && schema.isShareable();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isDataShareable(IdentifiableObject object) {
        return isDataClassShareable(HibernateProxyUtils.getRealClass(object));
    }

    @Override
    public <T extends IdentifiableObject> boolean isDataClassShareable(Class<T> klass) {
        Schema schema = schemaService.getSchema(klass);
        return schema != null && schema.isDataShareable();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canRead(User user, IdentifiableObject object) {
        return object == null || canRead(user, object, HibernateProxyUtils.getRealClass(object));
    }

    @Override
    public <T extends IdentifiableObject> boolean canRead(User user, T object, Class<? extends T> objType) {
        return true;
//        if ( readWriteCommonCheck( user, objType ) )
////        {
////            return true;
////        }
////
////        Schema schema = schemaService.getSchema( objType );
////
////        if ( canAccess( user, schema.getAuthorityByType( AuthorityType.READ ) ) )
////        {
////            if ( object instanceof CategoryOptionCombo )
////            {
////                return checkOptionComboSharingPermission( user, object, Permission.READ );
////            }
////
////            if ( !schema.isShareable() || object.getPublicAccess() == null || checkUser( user, object )
////                || checkSharingPermission( user, object, Permission.READ ) )
////            {
////                return true;
////            }
////        }
////        else
////        {
////            return false;
////        }
////
////        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canDataRead(User user, IdentifiableObject object) {
        return object == null || canDataRead(user, object, HibernateProxyUtils.getRealClass(object));
    }

    private <T extends IdentifiableObject> boolean canDataRead(User user, T object, Class<? extends T> objType) {
        return true;
//        if ( readWriteCommonCheck( user, objType ) )
//        {
//            return true;
//        }
//
//        Schema schema = schemaService.getSchema( objType );
//
//        if ( canAccess( user, schema.getAuthorityByType( AuthorityType.DATA_READ ) ) )
//        {
//            if ( object instanceof CategoryOptionCombo )
//            {
//                return checkOptionComboSharingPermission( user, object, Permission.DATA_READ )
//                    || checkOptionComboSharingPermission( user, object, Permission.DATA_WRITE );
//            }
//
//            if ( schema.isDataShareable() &&
//                (checkSharingPermission( user, object, Permission.DATA_READ )
//                    || checkSharingPermission( user, object, Permission.DATA_WRITE )) )
//            {
//                return true;
//            }
//        }
//
//        return false;
    }

    @Override
    public boolean canDataOrMetadataRead(User user, IdentifiableObject object) {
        Schema schema = schemaService.getSchema(HibernateProxyUtils.getRealClass(object));

        return schema.isDataShareable() ? canDataRead(user, object) : canRead(user, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canWrite(User user, IdentifiableObject object) {
        return object == null || canWrite(user, object, HibernateProxyUtils.getRealClass(object));
    }

    private <T extends IdentifiableObject> boolean canWrite(User user, T object, Class<? extends T> objType) {
        return true;
//        if ( readWriteCommonCheck( user, objType ) )
//        {
//            return true;
//        }
//
//        Schema schema = schemaService.getSchema( objType );
//
//        List<String> anyAuthorities = new ArrayList<>( schema.getAuthorityByType( AuthorityType.CREATE ) );
//
//        if ( anyAuthorities.isEmpty() )
//        {
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE_PRIVATE ) );
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE_PUBLIC ) );
//        }
//
//        if ( canAccess( user, anyAuthorities ) )
//        {
//            if ( object instanceof CategoryOptionCombo )
//            {
//                return checkOptionComboSharingPermission( user, object, Permission.WRITE );
//            }
//
//            return writeCommonCheck( schema, user, object, objType );
//        }
//        else if ( schema.isImplicitPrivateAuthority() && checkSharingAccess( user, object, objType ) )
//        {
//            return true;
//        }
//
//        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canDataWrite(User user, IdentifiableObject object) {
        return object == null || canDataWrite(user, object, HibernateProxyUtils.getRealClass(object));
    }

    private <T extends IdentifiableObject> boolean canDataWrite(User user, T object, Class<? extends T> objType) {
        return true;
//        if ( readWriteCommonCheck( user, objType ) )
//        {
//            return true;
//        }
//
//        Schema schema = schemaService.getSchema( objType );
//
//        // returned unmodifiable list does not need to be cloned since it is not
//        // modified
//        List<String> anyAuthorities = schema.getAuthorityByType( AuthorityType.DATA_CREATE );
//
//        if ( canAccess( user, anyAuthorities ) )
//        {
//            if ( object instanceof CategoryOptionCombo )
//            {
//                return checkOptionComboSharingPermission( user, object, Permission.DATA_WRITE );
//            }
//
//            if ( schema.isDataShareable() && checkSharingPermission( user, object, Permission.DATA_WRITE ) )
//            {
//                return true;
//            }
//        }
//
//        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canUpdate(User user, IdentifiableObject object) {
        return object == null || canUpdate(user, object, HibernateProxyUtils.getRealClass(object));
    }

    private <T extends IdentifiableObject> boolean canUpdate(User user, T object, Class<? extends T> objType) {
        return true;
//        if ( readWriteCommonCheck( user, objType ) )
//        {
//            return true;
//        }
//
//        Schema schema = schemaService.getSchema( objType );
//
//        List<String> anyAuthorities = new ArrayList<>( schema.getAuthorityByType( AuthorityType.UPDATE ) );
//
//        if ( anyAuthorities.isEmpty() )
//        {
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE ) );
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE_PRIVATE ) );
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE_PUBLIC ) );
//        }
//
//        if ( canAccess( user, anyAuthorities ) )
//        {
//            return writeCommonCheck( schema, user, object, objType );
//        }
//        else if ( schema.isImplicitPrivateAuthority() && checkSharingAccess( user, object, objType )
//            && (checkUser( user, object ) || checkSharingPermission( user, object, Permission.WRITE )) )
//        {
//            return true;
//        }
//
//        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canDelete(User user, IdentifiableObject object) {
        return object == null || canDelete(user, object, HibernateProxyUtils.getRealClass(object));
    }

    private <T extends IdentifiableObject> boolean canDelete(User user, T object, Class<? extends T> objType) {
        return true;
//        if ( readWriteCommonCheck( user, objType ) )
//        {
//            return true;
//        }
//
//        Schema schema = schemaService.getSchema( objType );
//
//        List<String> anyAuthorities = new ArrayList<>( schema.getAuthorityByType( AuthorityType.DELETE ) );
//
//        if ( anyAuthorities.isEmpty() )
//        {
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE ) );
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE_PRIVATE ) );
//            anyAuthorities.addAll( schema.getAuthorityByType( AuthorityType.CREATE_PUBLIC ) );
//        }
//
//        if ( canAccess( user, anyAuthorities ) )
//        {
//            if ( !schema.isShareable() || object.getPublicAccess() == null )
//            {
//                return true;
//            }
//
//            if ( checkSharingAccess( user, object, objType ) &&
//                (checkUser( user, object ) || checkSharingPermission( user, object, Permission.WRITE )) )
//            {
//                return true;
//            }
//        }
//        else if ( schema.isImplicitPrivateAuthority()
//            && (checkUser( user, object ) || checkSharingPermission( user, object, Permission.WRITE )) )
//        {
//            return true;
//        }
//
//        return false;
    }

    @Override
    public boolean canManage(User user, IdentifiableObject object) {
        return canUpdate(user, object);
    }

    private <T extends IdentifiableObject> boolean canManage(User user, T object, Class<? extends T> objType) {
        return canUpdate(user, object, objType);
    }

    @Override
    public <T extends IdentifiableObject> boolean canRead(User user, Class<T> klass) {
        return true;
//        Schema schema = schemaService.getSchema( klass );
//
//        return schema == null || schema.getAuthorityByType( AuthorityType.READ ) == null
//            || canAccess( user, schema.getAuthorityByType( AuthorityType.READ ) );
    }

    @Override
    public <T extends IdentifiableObject> boolean canCreate(User user, Class<T> klass) {
        return true;
//        Schema schema = schemaService.getSchema( klass );
//
//        if ( schema == null )
//        {
//            return false;
//        }
//
//        if ( !schema.isShareable() )
//        {
//            return canAccess( user, schema.getAuthorityByType( AuthorityType.CREATE ) );
//        }
//
//        return canMakeClassPublic( user, klass ) || canMakeClassPrivate( user, klass );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> boolean canMakePublic(User user, T object) {
        return canMakeClassPublic(user, HibernateProxyUtils.getRealClass(object));
    }

    @Override
    public <T extends IdentifiableObject> boolean canMakeClassPublic(User user, Class<T> klass) {
        return true;
//        Schema schema = schemaService.getSchema( klass );
//        return !(schema == null || !schema.isShareable())
//            && canAccess( user, schema.getAuthorityByType( AuthorityType.CREATE_PUBLIC ) );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> boolean canMakePrivate(User user, T object) {
        return canMakeClassPrivate(user, HibernateProxyUtils.getRealClass(object));
    }

    @Override
    public <T extends IdentifiableObject> boolean canMakeClassPrivate(User user, Class<T> klass) {
        return true;
//        Schema schema = schemaService.getSchema( klass );
//        return !(schema == null || !schema.isShareable())
//            && canAccess( user, schema.getAuthorityByType( AuthorityType.CREATE_PRIVATE ) );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdentifiableObject> boolean canMakeExternal(User user, T object) {
        return canMakeClassExternal(user, HibernateProxyUtils.getRealClass(object));
    }

    @Override
    public <T extends IdentifiableObject> boolean canMakeClassExternal(User user, Class<T> klass) {
        return true;
//        Schema schema = schemaService.getSchema( klass );
//        return !(schema == null || !schema.isShareable())
//            && ((!schema.getAuthorityByType( AuthorityType.EXTERNALIZE ).isEmpty() && haveOverrideAuthority( user ))
//                || haveAuthority( user, schema.getAuthorityByType( AuthorityType.EXTERNALIZE ) ));
    }

    @Override
    public <T extends IdentifiableObject> boolean defaultPrivate(Class<T> klass) {
        Schema schema = schemaService.getSchema(klass);
        return schema != null && schema.isDefaultPrivate();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T extends IdentifiableObject> boolean defaultPublic(T object) {
        return !defaultPrivate(HibernateProxyUtils.getRealClass(object));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends IdentifiableObject> classForType(String type) {
        Schema schema = schemaService.getSchemaBySingularName(type);

        if (schema != null && schema.isIdentifiableObject()) {
            return (Class<? extends IdentifiableObject>) schema.getKlass();
        }

        return null;
    }

    @Override
    public <T extends IdentifiableObject> void resetSharing(T object, User user) {
//        if ( object == null || !isShareable( object ) || user == null )
//        {
//            return;
//        }
//
//        BaseIdentifiableObject baseIdentifiableObject = (BaseIdentifiableObject) object;
//        baseIdentifiableObject.setPublicAccess( AccessStringHelper.DEFAULT );
//        baseIdentifiableObject.setExternalAccess( false );
//
//        if ( object.getSharing().getOwner() == null )
//        {
//            baseIdentifiableObject.setOwner( user.getUid() );
//        }
//
//        if ( canMakePublic( user, object ) && defaultPublic( object ) )
//        {
//            baseIdentifiableObject.setPublicAccess( AccessStringHelper.READ_WRITE );
//        }
//
//        SharingUtils.resetAccessCollections( baseIdentifiableObject );
    }

    @Override
    public <T extends IdentifiableObject> void clearSharing(T object, User user) {
//        if ( object == null || !isShareable( object ) || user == null )
//        {
//            return;
//        }
//
//        BaseIdentifiableObject baseIdentifiableObject = (BaseIdentifiableObject) object;
//        baseIdentifiableObject.setUser( user );
//        baseIdentifiableObject.setPublicAccess( AccessStringHelper.DEFAULT );
//        baseIdentifiableObject.setExternalAccess( false );
//        SharingUtils.resetAccessCollections( baseIdentifiableObject );
    }

    @Override
    public <T extends IdentifiableObject> List<ErrorReport> verifySharing(T object, User user) {
        List<ErrorReport> errorReports = new ArrayList<>();

//        if ( object == null || haveOverrideAuthority( user ) || !isShareable( object ) )
//        {
//            return errorReports;
//        }
//
//        if ( !AccessStringHelper.isValid( object.getSharing().getPublicAccess() ) )
//        {
//            errorReports.add( new ErrorReport( object.getClass(), ErrorCode.E3010, object.getPublicAccess() ) );
//            return errorReports;
//        }
//
//        Schema schema = schemaService.getSchema( HibernateProxyUtils.getRealClass( object ) );
//
//        if ( !schema.isDataShareable() )
//        {
//            ErrorReport errorReport = null;
//
//            if ( object.getSharing().getPublicAccess() != null &&
//                AccessStringHelper.hasDataSharing( object.getSharing().getPublicAccess() ) )
//            {
//                errorReport = new ErrorReport( object.getClass(), ErrorCode.E3011, object.getClass() );
//            }
//            else
//            {
//                for ( UserAccess userAccess : object.getSharing().getUsers().values() )
//                {
//                    if ( AccessStringHelper.hasDataSharing( userAccess.getAccess() ) )
//                    {
//                        errorReport = new ErrorReport( object.getClass(), ErrorCode.E3011, object.getClass() );
//                        break;
//                    }
//                }
//
//                for ( UserGroupAccess userGroupAccess : object.getSharing().getUserGroups().values() )
//                {
//                    if ( AccessStringHelper.hasDataSharing( userGroupAccess.getAccess() ) )
//                    {
//                        errorReport = new ErrorReport( object.getClass(), ErrorCode.E3011, object.getClass() );
//                        break;
//                    }
//                }
//            }
//
//            if ( errorReport != null )
//            {
//                errorReports.add( errorReport );
//            }
//        }
//
//        boolean canMakePublic = canMakePublic( user, object );
//        boolean canMakePrivate = canMakePrivate( user, object );
//        boolean canMakeExternal = canMakeExternal( user, object );
//
//        if ( object.getExternalAccess() )
//        {
//            if ( !canMakeExternal )
//            {
//                errorReports.add(
//                    new ErrorReport( object.getClass(), ErrorCode.E3006, user.getUsername(), object.getClass() ) );
//            }
//        }
//
//        errorReports.addAll( verifyImplicitSharing( user, object ) );
//
//        if ( AccessStringHelper.DEFAULT.equals( object.getPublicAccess() ) )
//        {
//            if ( canMakePublic || canMakePrivate )
//            {
//                return errorReports;
//            }
//
//            errorReports
//                .add( new ErrorReport( object.getClass(), ErrorCode.E3009, user.getUsername(), object.getClass() ) );
//        }
//        else
//        {
//            if ( canMakePublic )
//            {
//                return errorReports;
//            }
//
//            errorReports
//                .add( new ErrorReport( object.getClass(), ErrorCode.E3008, user.getUsername(), object.getClass() ) );
//        }

        return errorReports;
    }

    private <T extends IdentifiableObject> Collection<? extends ErrorReport> verifyImplicitSharing(User user,
                                                                                                   T object) {
        List<ErrorReport> errorReports = new ArrayList<>();
//        Schema schema = schemaService.getSchema( HibernateProxyUtils.getRealClass( object ) );
//
//        if ( !schema.isImplicitPrivateAuthority() || checkUser( user, object )
//            || checkSharingPermission( user, object, Permission.WRITE ) )
//        {
//            return errorReports;
//        }
//
//        if ( AccessStringHelper.DEFAULT.equals( object.getSharing().getPublicAccess() ) )
//        {
//            errorReports
//                .add( new ErrorReport( object.getClass(), ErrorCode.E3001, user.getUsername(), object.getClass() ) );
//        }

        return errorReports;
    }

    private boolean haveOverrideAuthority(User user) {
        return true;
//        return user == null || user.isSuper();
    }

    private boolean canAccess(User user, Collection<String> anyAuthorities) {
        return haveOverrideAuthority(user) || anyAuthorities.isEmpty() || haveAuthority(user, anyAuthorities);
    }

    private boolean haveAuthority(User user, Collection<String> anyAuthorities) {
        return true;
//        return containsAny( user.getUserCredentials().getAllAuthorities(), anyAuthorities );
    }

    /**
     * Should user be allowed access to this object.
     *
     * @param user   User to check against
     * @param object Object to check against
     * @return true/false depending on if access should be allowed
     */
    private boolean checkUser(User user, IdentifiableObject object) {
        return true;
//        return user == null || object.getSharing().getOwner() == null ||
//            user.getUid().equals( object.getSharing().getOwner() );
    }

    /**
     * Is the current user allowed to create/update the object given based on
     * its sharing settings.
     *
     * @param user   User to check against
     * @param object Object to check against
     * @return true/false depending on if sharing settings are allowed for given
     * user
     */
    private <T extends IdentifiableObject> boolean checkSharingAccess(User user, IdentifiableObject object,
                                                                      Class<T> objType) {
        return true;
//        boolean canMakePublic = canMakeClassPublic( user, objType );
//        boolean canMakePrivate = canMakeClassPrivate( user, objType );
//        boolean canMakeExternal = canMakeClassExternal( user, objType );
//
//        if ( AccessStringHelper.DEFAULT.equals( object.getSharing().getPublicAccess() ) )
//        {
//            if ( !(canMakePublic || canMakePrivate) )
//            {
//                return false;
//            }
//        }
//        else
//        {
//            if ( !canMakePublic )
//            {
//                return false;
//            }
//        }
//
//        if ( object.getExternalAccess() && !canMakeExternal )
//        {
//            return false;
//        }
//
//        return true;
    }

//    /**
//     * If the given user allowed to access the given object using the
//     * permissions given.
//     *
//     * @param user User to check against
//     * @param object Object to check against
//     * @param permission Permission to check against
//     * @return true if user can access object, false otherwise
//     */
//    private boolean checkSharingPermission( User user, IdentifiableObject object, Permission permission )
//    {
//        return true;
//        Sharing sharing = object.getSharing();
//        if ( AccessStringHelper.isEnabled( sharing.getPublicAccess(), permission ) )
//        {
//            return true;
//        }
//
//        if ( sharing.getUserGroups() != null && !CollectionUtils.isEmpty( user.getGroups() ) )
//        {
//            for ( UserGroupAccess userGroupAccess : sharing.getUserGroups().values() )
//            {
//                // Check if user is allowed to read this object through group
//                // access
//                if ( AccessStringHelper.isEnabled( userGroupAccess.getAccess(), permission )
//                    && hasUserGroupAccess( user.getGroups(), userGroupAccess.getId() ) )
//                {
//                    return true;
//                }
//            }
//        }
//
//        if ( sharing.getUsers() != null )
//        {
//            for ( UserAccess userAccess : sharing.getUsers().values() )
//            {
//                // Check if user is allowed to read to this object through user
//                // access
//
//                if ( AccessStringHelper.isEnabled( userAccess.getAccess(), permission )
//                    && user.getUid().equals( userAccess.getId() ) )
//                {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

    private boolean readWriteCommonCheck(User user, Class<?> objType) {
        if (haveOverrideAuthority(user)) {
            return true;
        }

        return schemaService.getSchema(objType) == null;
    }

    private <T extends IdentifiableObject> boolean writeCommonCheck(Schema schema, User user, T object,
                                                                    Class<? extends T> objType) {
        return true;
//        if ( !schema.isShareable() )
//        {
//            return true;
//        }
//
//        return checkSharingAccess( user, object, objType ) &&
//            (checkUser( user, object ) || checkSharingPermission( user, object, Permission.WRITE ));
    }

    private boolean hasUserGroupAccess(Set<PeopleGroup> userGroups, String userGroupUid) {
        return true;
//        for ( UserGroup group : userGroups )
//        {
//            if ( group.getUid().equals( userGroupUid ) )
//            {
//                return true;
//            }
//        }
//
//        return false;
    }
}