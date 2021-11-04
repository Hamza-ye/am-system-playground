package org.nmcpye.activitiesmanagement.extended.systemmodule.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nmcpye.activitiesmanagement.extended.common.CodeGenerator;
import org.nmcpye.activitiesmanagement.extended.scheduling.JobType;

import java.util.Date;

public class Notification
{
    private String uid; // FIXME expose as "id" externally in next API version as "uid" is internal

    private NotificationLevel level;

    private JobType category;

    private Date time;

    private String message;

    private boolean completed;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Notification()
    {
        this.uid = CodeGenerator.generateUid();
    }

    public Notification( NotificationLevel level, JobType category, Date time, String message, boolean completed )
    {
        this.uid = CodeGenerator.generateUid();
        this.level = level;
        this.category = category;
        this.time = time;
        this.message = message;
        this.completed = completed;
    }

    // -------------------------------------------------------------------------
    // Get and set
    // -------------------------------------------------------------------------

    @JsonProperty
    public NotificationLevel getLevel()
    {
        return level;
    }

    public void setLevel( NotificationLevel level )
    {
        this.level = level;
    }

    @JsonProperty
    public String getId() // expose as ID also to be future proof, we should not expose UID fields
    {
        return uid;
    }

    @JsonProperty
    public String getUid()
    {
        return uid;
    }

    public void setUid( String uid )
    {
        this.uid = uid;
    }

    @JsonProperty
    public JobType getCategory()
    {
        return category;
    }

    public void setCategory( JobType category )
    {
        this.category = category;
    }

    @JsonProperty
    public Date getTime()
    {
        return time;
    }

    public void setTime( Date time )
    {
        this.time = time;
    }

    @JsonProperty
    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    @JsonProperty
    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted( boolean completed )
    {
        this.completed = completed;
    }

    // -------------------------------------------------------------------------
    // equals, hashCode, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return uid.hashCode();
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final Notification other = (Notification) object;

        return uid.equals( other.uid );
    }

    @Override
    public String toString()
    {
        return "[Level: " + level + ", category: " + category + ", time: " + time + ", message: " + message + "]";
    }
}
