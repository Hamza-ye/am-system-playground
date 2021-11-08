package org.nmcpye.activitiesmanagement.extended.systemmodule.system.notification;

public enum NotificationLevel
{
    OFF,
    DEBUG,
    INFO,
    WARN,
    ERROR;
    
    public boolean isOff()
    {
        return this == OFF;
    }
}
