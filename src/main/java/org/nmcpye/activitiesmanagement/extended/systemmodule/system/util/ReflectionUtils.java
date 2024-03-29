package org.nmcpye.activitiesmanagement.extended.systemmodule.system.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import javassist.util.proxy.ProxyFactory;
import org.hibernate.collection.spi.PersistentCollection;
import org.nmcpye.activitiesmanagement.extended.schema.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private final static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    public static final List<String> SHARING_PROPS = ImmutableList.of(
        "publicAccess", "externalAccess", "userGroupAccesses", "userAccesses", "sharing" );

    /**
     * Invokes method getId() for this object and returns the return value. An
     * int return type is expected. If the operation fails -1 is returned.
     *
     * @param object object to call getId() on.
     * @return The identifier.
     */
    public static long getId( Object object )
    {
        try
        {
            Method method = object.getClass().getMethod( "getId" );

            return (Long) method.invoke( object );
        }
        catch ( NoSuchMethodException ex )
        {
            return -1;
        }
        catch ( InvocationTargetException ex )
        {
            return -1;
        }
        catch ( IllegalAccessException ex )
        {
            return -1;
        }
    }

    /**
     * Fetch a property off the object. Returns null if the operation fails.
     *
     * @param object the object.
     * @param property name of the property to get.
     * @return the value of the property or null.
     */
    public static String getProperty( Object object, String property )
    {
        try
        {
            property = property.substring( 0, 1 ).toUpperCase() + property.substring( 1, property.length() );

            Method method = object.getClass().getMethod( "get" + property );

            return (String) method.invoke( object );
        }
        catch ( NoSuchMethodException ex )
        {
            return null;
        }
        catch ( InvocationTargetException ex )
        {
            return null;
        }
        catch ( IllegalAccessException ex )
        {
            return null;
        }
    }

    /**
     * Sets a property for the supplied object. Throws an
     * UnsupportedOperationException if the operation fails.
     *
     * @param object Object to modify
     * @param name Name of property to set
     * @param value Value the property will be set to
     */
    public static void setProperty( Object object, String name, String value )
    {
        Object[] arguments = new Object[] { value };

        Class<?>[] parameterTypes = new Class<?>[] { String.class };

        if ( name.length() > 0 )
        {
            name = "set" + name.substring( 0, 1 ).toUpperCase() + name.substring( 1, name.length() );

            try
            {
                Method concatMethod = object.getClass().getMethod( name, parameterTypes );

                concatMethod.invoke( object, arguments );
            }
            catch ( Exception ex )
            {
                throw new UnsupportedOperationException( "Failed to set property", ex );
            }
        }
    }

    /**
     * Sets a property for the supplied object. Throws an
     * UnsupportedOperationException if the operation fails.
     *
     * @param object Object to modify
     * @param namePrefix prefix of the property name to set
     * @param name Name of property to set
     * @param value Value the property will be set to
     */
    public static void setProperty( Object object, String namePrefix, String name, String value )
    {
        String prefixed = namePrefix + name.substring( 0, 1 ).toUpperCase() + name.substring( 1, name.length() );

        setProperty( object, prefixed, value );
    }

    /**
     * Returns the name of the class that the object is an instance of
     *
     * @param object object to determine className for.
     * @return String containing the class name.
     */
    public static String getClassName( Object object )
    {
        return object.getClass().getSimpleName();
    }

    /**
     * Test whether the object is an array or a Collection.
     *
     * @param value the object.
     * @return true if the object is an array or a Collection, false otherwise.
     */
    public static boolean isCollection( Object value )
    {
        if ( value != null )
        {
            if ( value.getClass().isArray() || value instanceof Collection<?> )
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isCollection( String fieldName, Object object, Class<?> type )
    {
        return isCollection( fieldName, object, type, null );
    }

    public static boolean isCollection( String fieldName, Object object, Class<?> type,
                                        Class<? extends Annotation> annotation )
    {
        Field field;

        field = _findField( object.getClass(), fieldName );

        if ( field == null )
        {
            return false;
        }

        if ( annotation != null )
        {
            if ( !field.isAnnotationPresent( annotation ) )
            {
                return false;
            }
        }

        try
        {
            if ( Collection.class.isAssignableFrom( field.getType() ) )
            {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                if ( actualTypeArguments.length > 0 )
                {
                    if ( type.isAssignableFrom( (Class<?>) actualTypeArguments[0] ) )
                    {
                        return true;
                    }
                }
            }

        }
        catch ( ClassCastException e )
        {
            return false;
        }

        return false;
    }

    public static Method findGetterMethod( String fieldName, Object target )
    {
        return findGetterMethod( fieldName, target.getClass() );
    }

    public static Method findGetterMethod( String fieldName, Class<?> clazz )
    {
        final String[] getterNames = new String[] {
            "get",
            "is",
            "has"
        };

        Field field = _findField( clazz, StringUtils.uncapitalize( fieldName ) );
        Method method;

        if ( field != null )
        {
            for ( String getterName : getterNames )
            {
                method = _findMethod( clazz, getterName + StringUtils.capitalize( field.getName() ) );

                if ( method != null )
                {
                    return method;
                }
            }
        }

        return null;

    }

    public static <T> T invokeGetterMethod( String fieldName, Object target )
    {
        Method method = findGetterMethod( fieldName, target );

        if ( method != null )
        {
            return invokeMethod( target, method );
        }

        return null;
    }

    public static Method findSetterMethod( String fieldName, Object target )
    {
        if ( target == null || !StringUtils.hasLength( fieldName ) )
        {
            return null;
        }

        final String[] setterNames = new String[] {
            "set"
        };

        Field field = _findField( target.getClass(), StringUtils.uncapitalize( fieldName ) );
        Method method;

        if ( field != null )
        {
            for ( String setterName : setterNames )
            {
                method = _findMethod( target.getClass(), setterName + StringUtils.capitalize( field.getName() ),
                    field.getType() );

                if ( method != null )
                {
                    return method;
                }
            }
        }

        return null;
    }

    public static <T> T invokeSetterMethod( String fieldName, Object target, Object... args )
    {
        Method method = findSetterMethod( fieldName, target );

        if ( method != null )
        {
            return invokeMethod( target, method, args );
        }

        return null;
    }

    public static boolean isType( Field field, Class<?> clazz )
    {
        Class<?> type = field.getType();
        return clazz.isAssignableFrom( type );
    }

    private static Field _findField( Class<?> clazz, String name )
    {
        return _findField( clazz, name, null );
    }

    private static Field _findField( Class<?> clazz, String name, Class<?> type )
    {
        Class<?> searchType = clazz;

        while ( !Object.class.equals( searchType ) && searchType != null )
        {
            Field[] fields = searchType.getDeclaredFields();

            for ( Field field : fields )
            {
                if ( (name == null || name.equals( field.getName() )) )
                {
                    return field;
                }
            }

            searchType = searchType.getSuperclass();
        }

        return null;
    }

    public static List<Field> getAllFields( Class<?> clazz )
    {
        Class<?> searchType = clazz;
        List<Field> fields = new ArrayList<>();

        while ( !Object.class.equals( searchType ) && searchType != null )
        {
            fields.addAll( Arrays.asList( searchType.getDeclaredFields() ) );
            searchType = searchType.getSuperclass();
        }

        return fields;
    }

    public static Set<String> getAllFieldNames( Class<?> klass )
    {
        List<Field> fields = getAllFields( klass );
        return fields.stream().map( Field::getName ).collect( Collectors.toSet() );
    }

    private static Method _findMethod( Class<?> clazz, String name )
    {
        return _findMethod( clazz, name, new Class[0] );
    }

    private static Method _findMethod( Class<?> clazz, String name, Class<?>... paramTypes )
    {
        Class<?> searchType = clazz;

        while ( searchType != null )
        {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());

            for ( Method method : methods )
            {
                if ( name.equals( method.getName() )
                    && (paramTypes == null || Arrays.equals( paramTypes, method.getParameterTypes() )) )
                {
                    return method;
                }
            }

            searchType = searchType.getSuperclass();
        }

        return null;
    }

    @SuppressWarnings( "unchecked" )
    public static <T> T invokeMethod( Object target, Method method, Object... args )
    {
        if ( target == null || method == null )
        {
            return null;
        }

        if ( Modifier.isProtected( method.getModifiers() ) || Modifier.isPrivate( method.getModifiers() ) )
        {
            return null;
        }

        try
        {
            return (T) method.invoke( target, args );
        }
        catch ( InvocationTargetException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
        catch ( ClassCastException e )
        {
            log.error( "fail, ClassCastException:" + e.getMessage(), e );
            throw e;
        }
        catch ( IllegalArgumentException e )
        {
            log.error( "fail, IllegalArgumentException:" + e.getMessage(), e );
            throw e;
        }
    }

    public static Collection<Field> collectFields( Class<?> clazz, Predicate<Field> predicate )
    {
        Class<?> type = clazz;
        Collection<Field> fields = new ArrayList<>();

        while ( !Object.class.equals( type ) && type != null )
        {
            Field[] declaredFields = type.getDeclaredFields();

            for ( Field field : declaredFields )
            {
                if ( Modifier.isStatic( field.getModifiers() ) || (predicate != null && !predicate.test( field )) )
                {
                    continue;
                }

                fields.add( field );
            }

            type = type.getSuperclass();
        }

        return fields;
    }

    public static <E> Collection<E> newCollectionInstance( Class<?> clazz )
    {
        if ( List.class.isAssignableFrom( clazz ) )
        {
            return new ArrayList<>();
        }
        else if ( Set.class.isAssignableFrom( clazz ) )
        {
            return new HashSet<>();
        }
        else
        {
            throw new RuntimeException( "Unknown Collection type." );
        }
    }

    /**
     * Get all uniquely declared methods on a given Class, if methods are
     * overriden only the topmost method is returned.
     *
     * @param klass Class
     * @return List of uniquely declared methods
     */
    public static List<Method> getMethods( Class<?> klass )
    {
        return Arrays.asList( org.springframework.util.ReflectionUtils.getUniqueDeclaredMethods( klass ) );
    }

    /**
     * Returns a multimap of the mapping method-name -> [methods]. Useful to
     * find overloaded methods in a class hierarchy.
     *
     * @param klass Class
     * @return Multimap of method-name -> [methods]
     */
    public static Multimap<String, Method> getMethodsMultimap( Class<?> klass )
    {
        Multimap<String, Method> methods = ArrayListMultimap.create();
        getMethods( klass ).forEach( method -> methods.put( method.getName(), method ) );
        return methods;
    }

    public static boolean isSharingProperty( Property property )
    {
        return SHARING_PROPS.contains( property.getName() ) || SHARING_PROPS.contains( property.getCollectionName() );
    }

    public static boolean isTranslationProperty( Property property )
    {
        return "translations".equals( property.getName() ) || "translations".equals( property.getCollectionName() );
    }

    public static Class<?> getRealClass( Class<?> klass )
    {
        if ( ProxyFactory.isProxyClass( klass ) )
        {
            klass = klass.getSuperclass();
        }

        while ( PersistentCollection.class.isAssignableFrom( klass ) )
        {
            klass = klass.getSuperclass();
        }

        return klass;
    }
}
