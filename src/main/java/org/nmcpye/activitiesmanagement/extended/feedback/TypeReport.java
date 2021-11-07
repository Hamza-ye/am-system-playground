package org.nmcpye.activitiesmanagement.extended.feedback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeReport
{
    private Class<?> klass;

    private Stats stats = new Stats();

    private Map<Integer, ObjectReport> objectReportMap = new HashMap<>();

    @JsonCreator
    public TypeReport( @JsonProperty( "klass" ) Class<?> klass )
    {
        this.klass = klass;
    }

    //-----------------------------------------------------------------------------------
    // Utility Methods
    //-----------------------------------------------------------------------------------

    public void merge( TypeReport typeReport )
    {
        stats.merge( typeReport.getStats() );

        typeReport.getObjectReportMap().forEach( ( index, objectReport ) -> {
            if ( !objectReportMap.containsKey( index ) )
            {
                objectReportMap.put( index, objectReport );
                return;
            }

            objectReportMap.get( index ).addErrorReports( objectReport.getErrorReports() );
        } );
    }

    public void addObjectReport( ObjectReport objectReport )
    {
        if ( !objectReportMap.containsKey( objectReport.getIndex() ) )
        {
            objectReportMap.put( objectReport.getIndex(), objectReport );
            return;
        }

        objectReportMap.get( objectReport.getIndex() ).merge( objectReport );
    }

    //-----------------------------------------------------------------------------------
    // Getters and Setters
    //-----------------------------------------------------------------------------------

    @JsonProperty
    public Class<?> getKlass()
    {
        return klass;
    }

    @JsonProperty
    public Stats getStats()
    {
        return stats;
    }

    @JsonProperty
    public List<ObjectReport> getObjectReports()
    {
        return new ArrayList<>( objectReportMap.values() );
    }
    
    @JsonProperty
    public void setObjectReports( List<ObjectReport> objectReports )
    {
        if ( objectReports != null )
        {
            objectReports.forEach( or -> objectReportMap.put( or.getIndex(), or ) );
        }
    }

    public List<ErrorReport> getErrorReports()
    {
        List<ErrorReport> errorReports = new ArrayList<>();
        objectReportMap.values().forEach( objectReport -> errorReports.addAll( objectReport.getErrorReports() ) );

        return errorReports;
    }

    public Map<Integer, ObjectReport> getObjectReportMap()
    {
        return objectReportMap;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper( this )
            .add( "klass", klass )
            .add( "stats", stats )
            .add( "objectReports", getObjectReports() )
            .toString();
    }
}
