package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.extended.common.annotation.Description;
import org.nmcpye.activitiesmanagement.extended.schema.PropertyType;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Gist.Include;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.Property;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyRange;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyTransformer;
import org.nmcpye.activitiesmanagement.extended.schema.transformer.UserPropertyTransformer;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

//@MappedSuperclass
@JsonRootName( value = "identifiableObject", namespace = DxfNamespaces.DXF_2_0 )
public class BaseIdentifiableObject
    extends BaseLinkableObject implements IdentifiableObject {

    /**
     * The database internal identifier for this Object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    protected Long id;

    /**
     * The Unique Identifier for this Object.
     */
    @Size(max = 11)
    @Column(name = "uid", nullable = false, unique = true)
    protected String uid;

    /**
     * The unique code for this Object.
     */
    @Column(name = "code", unique = true)
    protected String code;

    /**
     * The name of this Object. Required and unique.
     */
    @Column(name = "name")
    protected String name;

    /**
     * The date this object was created.
     */
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date created;

    /**
     * The date this object was last updated.
     */
    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdated;

    /**
     * Owner of this object.
     */
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected User createdBy;

    /**
     * The i18n variant of the name. Not persisted.
     */
    protected transient String displayName;

    /**
     * Last user updated this object.
     */
    @ManyToOne
    @JoinColumn(name = "last_updated_by")
    protected User lastUpdatedBy;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BaseIdentifiableObject() {}

    public BaseIdentifiableObject(Long id, String uid, String name) {
        this.id = id;
        this.uid = uid;
        this.name = name;
    }

    public BaseIdentifiableObject(String uid, String code, String name) {
        this.uid = uid;
        this.code = code;
        this.name = name;
    }

    public BaseIdentifiableObject(IdentifiableObject identifiableObject) {
        this.id = identifiableObject.getId();
        this.uid = identifiableObject.getUid();
        this.name = identifiableObject.getName();
        this.created = identifiableObject.getCreated();
        this.lastUpdated = identifiableObject.getLastUpdated();
    }

    // -------------------------------------------------------------------------
    // Comparable implementation
    // -------------------------------------------------------------------------

    /**
     * Compares objects based on display name. A null display name is ordered
     * after a non-null display name.
     */
    @Override
    public int compareTo(IdentifiableObject object) {
        if (this.getDisplayName() == null) {
            return object.getDisplayName() == null ? 0 : 1;
        }

        return object.getDisplayName() == null ? -1 : this.getDisplayName().compareToIgnoreCase(object.getDisplayName());
    }

    // -------------------------------------------------------------------------
    // Setters and getters
    // -------------------------------------------------------------------------

    @Override
//    @JsonIgnore
    @JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
//    @JsonProperty(value = "id")
    @JsonProperty(value = "uid")
    @Description( "The Unique Identifier for this Object." )
    @Property(value = PropertyType.IDENTIFIER, required = Property.Value.FALSE)
    @PropertyRange(min = 11, max = 11)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    @JsonProperty
    @Description( "The unique code for this Object." )
    @Property(PropertyType.IDENTIFIER)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    @JsonProperty
    @Description( "The name of this Object. Required." )
    @PropertyRange(min = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonProperty
    public String getDisplayName() {
        //        displayName = getTranslation( TranslationProperty.NAME, displayName );
        return displayName != null ? displayName : getName();
    }

    @JsonIgnore
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    @JsonProperty
    @Description("The date this object was created.")
    @Property(value = PropertyType.DATE, required = Property.Value.FALSE)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    @JsonProperty
    @JsonSerialize(using = UserPropertyTransformer.JacksonSerialize.class)
    @JsonDeserialize(using = UserPropertyTransformer.JacksonDeserialize.class)
    @PropertyTransformer(UserPropertyTransformer.class)
    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Override
    @JsonProperty
    @Description("The date this object was last updated.")
    @Property(value = PropertyType.DATE, required = Property.Value.FALSE)
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    @Gist( included = Include.FALSE )
    @JsonProperty
    @JsonSerialize( using = UserPropertyTransformer.JacksonSerialize.class )
    @JsonDeserialize( using = UserPropertyTransformer.JacksonDeserialize.class )
    @PropertyTransformer( UserPropertyTransformer.class )
    public User getCreatedBy()
    {
        return createdBy;
    }

    @Override
    @JsonProperty
    @JsonSerialize(using = UserPropertyTransformer.JacksonSerialize.class)
    @JsonDeserialize(using = UserPropertyTransformer.JacksonDeserialize.class)
    @PropertyTransformer(UserPropertyTransformer.class)
    public User getUser() {
        return createdBy;
    }

    @Override
    public void setCreatedBy( User createdBy )
    {
        this.createdBy = createdBy;
    }

    public void setUser(User user) {
        setCreatedBy( createdBy == null ? user : createdBy );
    }

    // -------------------------------------------------------------------------
    // hashCode and equals
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        int result = getUid() != null ? getUid().hashCode() : 0;
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);

        return result;
    }

    /**
     * Class check uses isAssignableFrom and get-methods to handle proxied objects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!getClass().isAssignableFrom(o.getClass())) {
            return false;
        }

        final BaseIdentifiableObject other = (BaseIdentifiableObject) o;

        if (getUid() != null ? !getUid().equals(other.getUid()) : other.getUid() != null) {
            return false;
        }

        if (getCode() != null ? !getCode().equals(other.getCode()) : other.getCode() != null) {
            return false;
        }

        if (getName() != null ? !getName().equals(other.getName()) : other.getName() != null) {
            return false;
        }

        return true;
    }

    /**
     * Equality check against typed identifiable object. This method is not
     * vulnerable to proxy issues, where an uninitialized object class type
     * fails comparison to a real class.
     *
     * @param other the identifiable object to compare this object against.
     * @return true if equal.
     */
    public boolean typedEquals(IdentifiableObject other) {
        if (other == null) {
            return false;
        }

        if (getUid() != null ? !getUid().equals(other.getUid()) : other.getUid() != null) {
            return false;
        }

        if (getCode() != null ? !getCode().equals(other.getCode()) : other.getCode() != null) {
            return false;
        }

        if (getName() != null ? !getName().equals(other.getName()) : other.getName() != null) {
            return false;
        }

        return true;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Set auto-generated fields on save or update
     */
    public void setAutoFields() {
        if (getUid() == null || getUid().length() == 0) {
            setUid(CodeGenerator.generateUid());
        }

        Date date = new Date();

        if (getCreated() == null) {
            setCreated(date);
        }

        setLastUpdated(date);
    }

    /**
     * Returns the value of the property referred to by the given IdScheme.
     *
     * @param idScheme the IdScheme.
     * @return the value of the property referred to by the IdScheme.
     */
    @Override
    public String getPropertyValue(IdScheme idScheme) {
        if (idScheme.isNull() || idScheme.is(IdentifiableProperty.UID)) {
            return getUid();
        } else if (idScheme.is(IdentifiableProperty.CODE)) {
            return getCode();
        } else if (idScheme.is(IdentifiableProperty.NAME)) {
            return getName();
        } else if (idScheme.is(IdentifiableProperty.ID)) {
            return getId() > 0 ? String.valueOf(getId()) : null;
        }

        return null;
    }

    @Override
    public String toString() {
        return (
            "{" +
            "\"class\":\"" +
            getClass() +
            "\", " +
            "\"id\":\"" +
            getId() +
            "\", " +
            "\"uid\":\"" +
            getUid() +
            "\", " +
            "\"code\":\"" +
            getCode() +
            "\", " +
            "\"name\":\"" +
            getName() +
            "\", " +
            "\"created\":\"" +
            getCreated() +
            "\", " +
            "\"lastUpdated\":\"" +
            getLastUpdated() +
            "\" " +
            "}"
        );
    }
}
