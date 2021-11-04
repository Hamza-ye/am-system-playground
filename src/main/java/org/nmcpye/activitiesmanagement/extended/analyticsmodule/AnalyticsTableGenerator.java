package org.nmcpye.activitiesmanagement.extended.analyticsmodule;

import org.nmcpye.activitiesmanagement.domain.JobConfiguration;

import javax.annotation.Nullable;

/**
 * Interface responsible for generating analytics tables. Will look for and
 * invoke implementations of interface {@link AnalyticsTableService}.
 *
 */
public interface AnalyticsTableGenerator
{
//    /**
//     * Generates analytics tables.
//     *
//     * @param params the {@link AnalyticsTableUpdateParams}.
//     */
//    void generateTables( AnalyticsTableUpdateParams params );

    /**
     * Drops all existing analytics tables.
     */
    void dropTables();

    /**
     * Generates all resource tables.
     *
     * @param jobId the job identifier, can be null.
     */
    void generateResourceTables( @Nullable JobConfiguration jobId );
}
