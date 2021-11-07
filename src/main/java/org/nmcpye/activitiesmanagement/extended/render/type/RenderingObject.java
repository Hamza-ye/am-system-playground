package org.nmcpye.activitiesmanagement.extended.render.type;

public interface RenderingObject<T extends Enum<?>>
{
    String _TYPE = "type";

    T getType();

    void setType(T type);

    Class<T> getRenderTypeClass();
}
