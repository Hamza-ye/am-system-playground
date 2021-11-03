package org.nmcpye.activitiesmanagement.extended.common;

public interface NameableObject extends IdentifiableObject {
    String getShortName();

    String getDisplayShortName();

    String getDescription();

    String getDisplayDescription();

    String getDisplayProperty(DisplayProperty property);
}
