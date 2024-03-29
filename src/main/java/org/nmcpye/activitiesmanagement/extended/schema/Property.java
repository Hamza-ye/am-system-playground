package org.nmcpye.activitiesmanagement.extended.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.nmcpye.activitiesmanagement.extended.common.EmbeddedObject;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject;
import org.nmcpye.activitiesmanagement.extended.common.NameableObject;
import org.nmcpye.activitiesmanagement.extended.hibernate.HibernateProxyUtils;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class Property implements Ordered, Klass {

    /**
     * Class for property.
     */
    private Class<?> klass;

    /**
     * Normalized type of this property
     */
    private PropertyType propertyType;

    /**
     * If this property is a collection, this is the class of the items inside
     * the collection.
     */
    private Class<?> itemKlass;

    /**
     * If this property is a collection, this is the normalized type of the
     * items inside the collection.
     */
    private PropertyType itemPropertyType;

    /**
     * Direct link to getter for this property.
     */
    private Method getterMethod;

    /**
     * Direct link to setter for this property.
     */
    private Method setterMethod;

    /**
     * Name for this property, if this class is a collection, it is the name of
     * the items -inside- the collection and not the collection wrapper itself.
     */
    private String name;

    /**
     * Name for actual field, used to persistence operations and getting
     * setter/getter.
     */
    private String fieldName;

    /**
     * Is this property persisted somewhere. This property will be used to
     * create criteria queries on demand (default: false)
     */
    private boolean persisted;

    /**
     * Name of collection wrapper.
     */
    private String collectionName;

    /**
     * If this Property is a collection, should it be wrapped with
     * collectionName?
     */
    private Boolean collectionWrapping;

    /**
     * Description if provided, will be fetched from @Description annotation.
     *
     * @see org.nmcpye.activitiesmanagement.extended.common.annotation.Description
     */
    private String description;

    /**
     * Namespace used for this property.
     */
    private String namespace;

    /**
     * Usually only used for XML. Is this property considered an attribute.
     */
    private boolean attribute;

    /**
     * This property is true if the type pointed to does not export any
     * properties itself, it is then assumed to be a primitive type. If
     * collection is true, this this check is done on the generic type of the
     * collection, e.g. List<String> would set simple to be true, but
     * List<DataElement> would set it to false.
     */
    private boolean simple;

    /**
     * This property is true if the type of this property is a sub-class of
     * Collection.
     *
     * @see java.util.Collection
     */
    private boolean collection;

    /**
     * This property is true if collection=true and klass points to a
     * implementation with a stable order (i.e. List).
     */
    private boolean ordered;

    /**
     * If this property is a complex object or a collection, is this property
     * considered the owner of that relationship (important for imports etc).
     */
    private boolean owner;

    /**
     * Is this class a sub-class of IdentifiableObject
     *
     * @see org.nmcpye.activitiesmanagement.extended.common.IdentifiableObject
     */
    private boolean identifiableObject;

    /**
     * Is this class a sub-class of NameableObject
     *
     * @see org.nmcpye.activitiesmanagement.extended.common.NameableObject
     */
    private boolean nameableObject;

    /**
     * Does this class implement {@link EmbeddedObject} ?
     */
    private boolean embeddedObject;

    /**
     * Does this class implement {@link EmbeddedObject} ?
     */
    private boolean analyticalObject;

    /**
     * Can this property be read.
     */
    private boolean readable;

    /**
     * Can this property be written to.
     */
    private boolean writable;

    /**
     * Are the values for this property required to be unique?
     */
    private boolean unique;

    /**
     * Nullability of this property.
     */
    private boolean required;

    /**
     * Maximum length/size/value of this property.
     */
    private Integer length;

    /**
     * Minimum size/length of this property.
     */
    private Double max;

    /**
     * Minimum size/length of this property.
     */
    private Double min;

    /**
     * Cascading used when doing CRUD operations.
     */
    private String cascade;

    /**
     * Is property many-to-many.
     */
    private boolean manyToMany;

    /**
     * Is property one-to-one.
     */
    private boolean oneToOne;

    /**
     * Is property many-to-one.
     */
    private boolean manyToOne;

    /**
     * Is property one-to-many.
     */
    private boolean oneToMany;

    /**
     * The hibernate role of the owning side.
     */
    private String owningRole;

    /**
     * The hibernate role of the inverse side (if many-to-many).
     */
    private String inverseRole;

    /**
     * If property type is enum, this is the list of valid options.
     */
    private List<String> constants;

    /**
     * Used by LinkService to link to the Schema describing this type (if
     * reference).
     */
    private String href;

    /**
     * Points to relative Web-API endpoint (if exposed).
     */
    private String relativeApiEndpoint;

    /**
     * Used by LinkService to link to the API endpoint containing this type.
     */
    private String apiEndpoint;

    /**
     * PropertyTransformer to apply to this property before and field filtering
     * is applied.
     */
    private Class<? extends PropertyTransformer> propertyTransformer;

    /**
     * Default value of the Property
     */
    private Object defaultValue;

    private boolean translatable;

    private String translationKey;

    /**
     * The translation key use for retrieving I18n translation of this
     * property's name. The key follows snake_case naming convention.
     */
    private String i18nTranslationKey;

    private GistPreferences gistPreferences = GistPreferences.DEFAULT;

    public Property()
    {
    }

    public Property( Class<?> klass )
    {
        setKlass( klass );
    }

    public Property( Class<?> klass, Method getter, Method setter )
    {
        this( klass );
        this.getterMethod = getter;
        this.setterMethod = setter;
    }

    @Override
    @JsonProperty
    public Class<?> getKlass()
    {
        return klass;
    }

    public void setKlass( Class<?> klass )
    {
        this.identifiableObject = IdentifiableObject.class.isAssignableFrom( klass );
        this.nameableObject = NameableObject.class.isAssignableFrom( klass );
        this.embeddedObject = EmbeddedObject.class.isAssignableFrom( klass );
        this.klass = klass;
    }

    @JsonProperty
    public PropertyType getPropertyType()
    {
        return propertyType;
    }

    public void setPropertyType( PropertyType propertyType )
    {
        this.propertyType = propertyType;
    }

    @JsonProperty
    public Class<?> getItemKlass()
    {
        return itemKlass;
    }

    public void setItemKlass( Class<?> itemKlass )
    {
        this.itemKlass = itemKlass;
    }

    @JsonProperty
    public PropertyType getItemPropertyType()
    {
        return itemPropertyType;
    }

    public void setItemPropertyType( PropertyType itemPropertyType )
    {
        this.itemPropertyType = itemPropertyType;
    }

    public Method getGetterMethod()
    {
        return getterMethod;
    }

    public void setGetterMethod( Method getterMethod )
    {
        this.getterMethod = getterMethod;
    }

    public Method getSetterMethod()
    {
        return setterMethod;
    }

    public void setSetterMethod( Method setterMethod )
    {
        this.setterMethod = setterMethod;
    }

    @JsonProperty
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @JsonProperty
    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName( String fieldName )
    {
        this.fieldName = fieldName;
    }

    @JsonProperty
    public boolean isPersisted()
    {
        return persisted;
    }

    public void setPersisted( boolean persisted )
    {
        this.persisted = persisted;
    }

    @JsonProperty
    public String getCollectionName()
    {
        return collectionName != null ? collectionName : (isCollection() ? name : null);
    }

    public void setCollectionName( String collectionName )
    {
        this.collectionName = collectionName;
    }

    @JsonProperty
    public Boolean isCollectionWrapping()
    {
        return collectionWrapping;
    }

    public void setCollectionWrapping( Boolean collectionWrapping )
    {
        this.collectionWrapping = collectionWrapping;
    }

    @JsonProperty
    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    @JsonProperty
    public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace( String namespace )
    {
        this.namespace = namespace;
    }

    @JsonProperty
    public boolean isAttribute()
    {
        return attribute;
    }

    public void setAttribute( boolean attribute )
    {
        this.attribute = attribute;
    }

    @JsonProperty
    public boolean isSimple()
    {
        return simple;
    }

    public void setSimple( boolean simple )
    {
        this.simple = simple;
    }

    @JsonProperty
    public boolean isCollection()
    {
        return collection;
    }

    public void setCollection( boolean collection )
    {
        this.collection = collection;
    }

    @JsonProperty
    public boolean isOrdered()
    {
        return ordered;
    }

    public void setOrdered( boolean ordered )
    {
        this.ordered = ordered;
    }

    @JsonProperty
    public boolean isOwner()
    {
        return owner;
    }

    public void setOwner( boolean owner )
    {
        this.owner = owner;
    }

    @JsonProperty
    public boolean isIdentifiableObject()
    {
        return identifiableObject;
    }

    public void setIdentifiableObject( boolean identifiableObject )
    {
        this.identifiableObject = identifiableObject;
    }

    @JsonProperty
    public boolean isNameableObject()
    {
        return nameableObject;
    }

    public void setNameableObject( boolean nameableObject )
    {
        this.nameableObject = nameableObject;
    }

    @JsonProperty
    public boolean isEmbeddedObject()
    {
        return embeddedObject;
    }

    public void setEmbeddedObject( boolean embeddedObject )
    {
        this.embeddedObject = embeddedObject;
    }

    @JsonProperty
    public boolean isAnalyticalObject()
    {
        return analyticalObject;
    }

    public void setAnalyticalObject( boolean analyticalObject )
    {
        this.analyticalObject = analyticalObject;
    }

    @JsonProperty
    public boolean isReadable()
    {
        return readable;
    }

    public void setReadable( boolean readable )
    {
        this.readable = readable;
    }

    @JsonProperty
    public boolean isWritable()
    {
        return writable;
    }

    public void setWritable( boolean writable )
    {
        this.writable = writable;
    }

    @JsonProperty
    public boolean isUnique()
    {
        return unique;
    }

    public void setUnique( boolean unique )
    {
        this.unique = unique;
    }

    @JsonProperty
    public boolean isRequired()
    {
        return required;
    }

    public void setRequired( boolean required )
    {
        this.required = required;
    }

    @JsonProperty
    public Integer getLength()
    {
        return length;
    }

    public void setLength( Integer length )
    {
        this.length = length;
    }

    @JsonProperty
    public Double getMax()
    {
        return max;
    }

    public void setMax( Double max )
    {
        this.max = max;
    }

    @JsonProperty
    public Double getMin()
    {
        return min;
    }

    public void setMin( Double min )
    {
        this.min = min;
    }

    @JsonProperty
    public String getCascade()
    {
        return cascade;
    }

    public void setCascade( String cascade )
    {
        this.cascade = cascade;
    }

    @JsonProperty
    public boolean isManyToMany()
    {
        return manyToMany;
    }

    public void setManyToMany( boolean manyToMany )
    {
        this.manyToMany = manyToMany;
    }

    @JsonProperty
    public boolean isOneToOne()
    {
        return oneToOne;
    }

    public void setOneToOne( boolean oneToOne )
    {
        this.oneToOne = oneToOne;
    }

    @JsonProperty
    public boolean isManyToOne()
    {
        return manyToOne;
    }

    public void setManyToOne( boolean manyToOne )
    {
        this.manyToOne = manyToOne;
    }

    @JsonProperty
    public boolean isOneToMany()
    {
        return oneToMany;
    }

    public void setOneToMany( boolean oneToMany )
    {
        this.oneToMany = oneToMany;
    }

    @JsonProperty
    public String getOwningRole()
    {
        return owningRole;
    }

    public void setOwningRole( String owningRole )
    {
        this.owningRole = owningRole;
    }

    @JsonProperty
    public String getInverseRole()
    {
        return inverseRole;
    }

    public void setInverseRole( String inverseRole )
    {
        this.inverseRole = inverseRole;
    }

    @JsonProperty
    public String getTranslationKey()
    {
        return this.translationKey;
    }

    public void setTranslationKey( String translationKey )
    {
        this.translationKey = translationKey;
    }

    @JsonProperty
    public String getI18nTranslationKey()
    {
        return i18nTranslationKey;
    }

    public void setI18nTranslationKey( String i18nTranslationKey )
    {
        this.i18nTranslationKey = i18nTranslationKey;
    }

    @JsonProperty
    public List<String> getConstants()
    {
        return constants;
    }

    public void setConstants( List<String> constants )
    {
        this.constants = constants;
    }

    @JsonProperty
    public String getHref()
    {
        return href;
    }

    public void setHref( String href )
    {
        this.href = href;
    }

    @JsonProperty
    public String getRelativeApiEndpoint()
    {
        return relativeApiEndpoint;
    }

    public void setRelativeApiEndpoint( String relativeApiEndpoint )
    {
        this.relativeApiEndpoint = relativeApiEndpoint;
    }

    @JsonProperty
    public String getApiEndpoint()
    {
        return apiEndpoint;
    }

    public void setApiEndpoint( String apiEndpoint )
    {
        this.apiEndpoint = apiEndpoint;
    }

    @JsonProperty( "propertyTransformer" )
    public boolean hasPropertyTransformer()
    {
        return propertyTransformer != null;
    }

    public Class<? extends PropertyTransformer> getPropertyTransformer()
    {
        return propertyTransformer;
    }

    public void setPropertyTransformer( Class<? extends PropertyTransformer> propertyTransformer )
    {
        this.propertyTransformer = propertyTransformer;
    }

    @JsonProperty
    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue( Object defaultValue )
    {
        if ( defaultValue != null && klass.isAssignableFrom( HibernateProxyUtils.getRealClass( defaultValue ) ) )
        {
            this.defaultValue = defaultValue;
        }
        else
        {
            this.defaultValue = null;
        }
    }

    @JsonProperty
    public boolean isTranslatable()
    {
        return this.translatable;
    }

    public void setTranslatable( boolean translatable )
    {
        this.translatable = translatable;
    }

    @JsonProperty
    public GistPreferences getGistPreferences()
    {
        return gistPreferences;
    }

    public void setGistPreferences( GistPreferences gistPreferences )
    {
        this.gistPreferences = gistPreferences == null ? GistPreferences.DEFAULT : gistPreferences;
    }

    public String key()
    {
        return isCollection() ? collectionName : name;
    }

    public boolean is( PropertyType propertyType )
    {
        return propertyType != null && propertyType.equals( this.propertyType );
    }

    public boolean is( PropertyType... anyOf )
    {
        for ( PropertyType type : anyOf )
        {
            if ( is( type ) )
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder()
    {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( klass, propertyType, itemKlass, itemPropertyType, getterMethod, setterMethod, name,
            fieldName, persisted, collectionName,
            collectionWrapping, description, namespace, attribute, simple, collection, owner, identifiableObject,
            nameableObject, readable, writable,
            unique, required, length, max, min, cascade, manyToMany, oneToOne, manyToOne, owningRole, inverseRole,
            constants, defaultValue );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }

        final Property other = (Property) obj;

        return Objects.equals( this.klass, other.klass )
            && Objects.equals( this.propertyType, other.propertyType )
            && Objects.equals( this.itemKlass, other.itemKlass )
            && Objects.equals( this.itemPropertyType, other.itemPropertyType )
            && Objects.equals( this.getterMethod, other.getterMethod )
            && Objects.equals( this.setterMethod, other.setterMethod )
            && Objects.equals( this.name, other.name )
            && Objects.equals( this.fieldName, other.fieldName )
            && Objects.equals( this.persisted, other.persisted )
            && Objects.equals( this.collectionName, other.collectionName )
            && Objects.equals( this.collectionWrapping, other.collectionWrapping )
            && Objects.equals( this.description, other.description )
            && Objects.equals( this.namespace, other.namespace )
            && Objects.equals( this.attribute, other.attribute )
            && Objects.equals( this.simple, other.simple )
            && Objects.equals( this.collection, other.collection )
            && Objects.equals( this.owner, other.owner )
            && Objects.equals( this.identifiableObject, other.identifiableObject )
            && Objects.equals( this.nameableObject, other.nameableObject )
            && Objects.equals( this.readable, other.readable )
            && Objects.equals( this.writable, other.writable )
            && Objects.equals( this.unique, other.unique )
            && Objects.equals( this.required, other.required )
            && Objects.equals( this.length, other.length )
            && Objects.equals( this.max, other.max )
            && Objects.equals( this.min, other.min )
            && Objects.equals( this.cascade, other.cascade )
            && Objects.equals( this.manyToMany, other.manyToMany )
            && Objects.equals( this.oneToOne, other.oneToOne )
            && Objects.equals( this.manyToOne, other.manyToOne )
            && Objects.equals( this.owningRole, other.owningRole )
            && Objects.equals( this.inverseRole, other.inverseRole )
            && Objects.equals( this.constants, other.constants )
            && Objects.equals( this.defaultValue, other.defaultValue );
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "klass", klass )
            .add( "propertyType", propertyType )
            .add( "itemKlass", itemKlass )
            .add( "itemPropertyType", itemPropertyType )
            .add( "getterMethod", getterMethod )
            .add( "setterMethod", setterMethod )
            .add( "name", name )
            .add( "fieldName", fieldName )
            .add( "persisted", persisted )
            .add( "collectionName", collectionName )
            .add( "collectionWrapping", collectionWrapping )
            .add( "description", description )
            .add( "namespace", namespace )
            .add( "attribute", attribute )
            .add( "simple", simple )
            .add( "collection", collection )
            .add( "owner", owner )
            .add( "identifiableObject", identifiableObject )
            .add( "nameableObject", nameableObject )
            .add( "readable", readable )
            .add( "writable", writable )
            .add( "unique", unique )
            .add( "required", required )
            .add( "length", length )
            .add( "max", max )
            .add( "min", min )
            .add( "cascade", cascade )
            .add( "manyToMany", manyToMany )
            .add( "oneToOne", oneToOne )
            .add( "manyToOne", manyToOne )
            .add( "owningRole", owningRole )
            .add( "inverseRole", inverseRole )
            .add( "constants", constants )
            .add( "defaultValue", defaultValue )
            .toString();
    }
}
