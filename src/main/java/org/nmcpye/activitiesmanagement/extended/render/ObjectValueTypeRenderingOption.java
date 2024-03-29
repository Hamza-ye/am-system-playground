package org.nmcpye.activitiesmanagement.extended.render;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.common.ValueType;
import org.nmcpye.activitiesmanagement.extended.render.type.ValueTypeRenderingType;

import java.util.Objects;
import java.util.Set;

/**
 * This class represents how a class (DataElement or TrackedEntityAttribute), a ValueType or OptionSet can be rendered
 */
public class ObjectValueTypeRenderingOption
{
    /**
     * The class that should be rendered
     */
    private Class<?> clazz;

    /**
     * The ValueType of the class to be rendered
     */
    private ValueType valueType;

    /**
     * Does the object repreent an option set?
     */
    private boolean hasOptionSet;

    /**
     * A set of renderingTypes available for the combination of clazz valueType and hasOptionSet
     */
    private Set<ValueTypeRenderingType> renderingTypes;

    public ObjectValueTypeRenderingOption( Class<?> clazz, ValueType valueType, boolean hasOptionSet,
        Set<ValueTypeRenderingType> renderingTypes )
    {
        this.clazz = clazz;
        this.valueType = valueType;
        this.hasOptionSet = hasOptionSet;
        this.renderingTypes = renderingTypes;
    }

    @JsonProperty
    public Set<ValueTypeRenderingType> getRenderingTypes()
    {
        return renderingTypes;
    }

    public void setRenderingTypes( Set<ValueTypeRenderingType> renderingTypes )
    {
        this.renderingTypes = renderingTypes;
    }

    @JsonProperty
    public boolean isHasOptionSet()
    {
        return hasOptionSet;
    }

    public void setHasOptionSet( boolean hasOptionSet )
    {
        this.hasOptionSet = hasOptionSet;
    }

    @JsonProperty
    public ValueType getValueType()
    {
        return valueType;
    }

    public void setValueType( ValueType valueType )
    {
        this.valueType = valueType;
    }

    @JsonProperty
    public Class<?> getClazz()
    {
        return clazz;
    }

    public void setClazz( Class<?> clazz )
    {
        this.clazz = clazz;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        ObjectValueTypeRenderingOption that = (ObjectValueTypeRenderingOption) o;
        return hasOptionSet == that.hasOptionSet &&
            Objects.equals( clazz, that.clazz ) &&
            valueType == that.valueType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( clazz, valueType, hasOptionSet );
    }
}
