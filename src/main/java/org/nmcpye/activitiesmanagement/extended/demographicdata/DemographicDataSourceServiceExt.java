package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface DemographicDataSourceServiceExt {

    String ID = DemographicDataSourceServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // DemographicDataSource
    // -------------------------------------------------------------------------

    /**
     * Adds a DemographicDataSource.
     *
     * @param demographicDataSource The DemographicDataSource to add.
     * @return The generated unique identifier for this DemographicDataSource.
     */
    long addDemographicDataSource(DemographicDataSource demographicDataSource);

    /**
     * Updates a DemographicDataSource.
     *
     * @param demographicDataSource The DemographicDataSource to update.
     */
    void updateDemographicDataSource(DemographicDataSource demographicDataSource);

    /**
     * Deletes a DemographicDataSource.
     *
     * @param demographicDataSource The DemographicDataSource to delete.
     */
    void deleteDemographicDataSource(DemographicDataSource demographicDataSource);

    /**
     * Get a DemographicDataSource
     *
     * @param id The unique identifier for the DemographicDataSource to get.
     * @return The DemographicDataSource with the given id or null if it does not exist.
     */
    DemographicDataSource getDemographicDataSource(long id);

    /**
     * Returns the DemographicDataSource with the given UID.
     *
     * @param uid the UID.
     * @return the DemographicDataSource with the given UID, or null if no match.
     */
    DemographicDataSource getDemographicDataSource(String uid);

    /**
     * Returns the DemographicDataSource with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the DemographicDataSource with the given UID, or null if no match.
     */
    DemographicDataSource getDemographicDataSourceNoAcl(String uid);

    /**
     * Get all DemographicDataSources.
     *
     * @return A list containing all DemographicDataSources.
     */
    List<DemographicDataSource> getAllDemographicDataSources();

    /**
     * Returns the DemographicDataSources which current user have READ access. If the current
     * user has the ALL authority then all DemographicDataSources are returned.
     */
    List<DemographicDataSource> getAllDataRead();

    /**
     * Returns the DemographicDataSources which given user have READ access. If the current
     * user has the ALL authority then all DemographicDataSources are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DemographicDataSources which the given user has data read access to.
     */
    List<DemographicDataSource> getUserDataRead(User user);

    /**
     * Returns the DemographicDataSources which current user have WRITE access. If the
     * current user has the ALL authority then all DemographicDataSources are returned.
     */
    List<DemographicDataSource> getAllDataWrite();

    /**
     * Returns the DemographicDataSources which current user have WRITE access. If the
     * current user has the ALL authority then all DemographicDataSources are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DemographicDataSources which given user has data write access to.
     */
    List<DemographicDataSource> getUserDataWrite(User user);

}
