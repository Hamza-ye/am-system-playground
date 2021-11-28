package org.nmcpye.activitiesmanagement.extended.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.nmcpye.activitiesmanagement.extended.schema.annotation.PropertyRange;
import org.nmcpye.activitiesmanagement.extended.translation.Translatable;

import javax.persistence.Column;
import javax.validation.constraints.Size;

//@MappedSuperclass
@JsonRootName( value = "nameableObject", namespace = DxfNamespaces.DXF_2_0 )
public class BaseNameableObject extends BaseIdentifiableObject implements NameableObject {

    /**
     * An short name representing this Object. Optional but unique.
     */
    @Column(name = "short_name")
    @Size(max = 50)
    protected String shortName;

    /**
     * Description of this Object.
     */
    @Column(name = "description")
    protected String description;

    /**
     * The i18n variant of the short name. Should not be persisted.
     */
    protected transient String displayShortName;

    /**
     * The i18n variant of the description. Should not be persisted.
     */
    protected transient String displayDescription;

    protected transient String displayFormName;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public BaseNameableObject() {}

    public BaseNameableObject(String uid, String code, String name) {
        this.uid = uid;
        this.code = code;
        this.name = name;
    }

    public BaseNameableObject(long id, String uid, String name, String shortName, String code, String description) {
        super(id, uid, name);
        this.shortName = shortName;
        this.code = code;
        this.description = description;
    }

    public BaseNameableObject(NameableObject object) {
        super(object.getId(), object.getUid(), object.getName());
        this.shortName = object.getShortName();
        this.code = object.getCode();
        this.description = object.getDescription();
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Returns the display property indicated by the given display property. Falls
     * back to display name if display short name is null.
     *
     * @param displayProperty the display property.
     * @return the display property.
     */
    @Override
    @JsonIgnore
    public String getDisplayProperty(DisplayProperty displayProperty) {
        if (DisplayProperty.SHORTNAME.equals(displayProperty) && getDisplayShortName() != null) {
            return getDisplayShortName();
        } else {
            return getDisplayName();
        }
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getShortName() != null ? getShortName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
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

        if (!super.equals(o)) {
            return false;
        }

        final BaseNameableObject other = (BaseNameableObject) o;

        if (getShortName() != null ? !getShortName().equals(other.getShortName()) : other.getShortName() != null) {
            return false;
        }

        if (getDescription() != null ? !getDescription().equals(other.getDescription()) : other.getDescription() != null) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return (
            "{" +
            "\"class\":\"" +
            getClass() +
            "\", " +
            "\"hashCode\":\"" +
            hashCode() +
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
            "\"shortName\":\"" +
            getShortName() +
            "\", " +
            "\"description\":\"" +
            getDescription() +
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

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @Override
    @JsonProperty
    @PropertyRange(min = 1)
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    @JsonProperty
    @Translatable( propertyName = "shortName", key = "SHORT_NAME" )
    public String getDisplayShortName()
    {
        return getTranslation( "SHORT_NAME", getShortName() );
    }

    public void setDisplayShortName(String displayShortName) {
        this.displayShortName = displayShortName;
    }

    @Override
    @JsonProperty
    @PropertyRange(min = 1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    @JsonProperty
    @Translatable( propertyName = "description", key = "DESCRIPTION" )
    public String getDisplayDescription()
    {
        return getTranslation( "DESCRIPTION", getDescription() );
    }

    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
    }
}
