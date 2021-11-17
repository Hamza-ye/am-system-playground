package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface DemographicDataServiceExt {

    String ID = DemographicDataServiceExt.class.getName();
    // -------------------------------------------------------------------------
    // DemographicData
    // -------------------------------------------------------------------------

    /**
     * Adds a DemographicData.
     *
     * @param demographicData The DemographicData to add.
     * @return The generated unique identifier for this DemographicData.
     */
    Long addDemographicData(DemographicData demographicData);

    /**
     * Updates a DemographicData.
     *
     * @param demographicData The DemographicData to update.
     */
    void updateDemographicData(DemographicData demographicData);

    /**
     * Deletes a DemographicData.
     *
     * @param demographicData The DemographicData to delete.
     */
    void deleteDemographicData(DemographicData demographicData);

    /**
     * Get a DemographicData
     *
     * @param id The unique identifier for the DemographicData to get.
     * @return The DemographicData with the given id or null if it does not exist.
     */
    DemographicData getDemographicData(Long id);

    /**
     * Returns the DemographicData with the given UID.
     *
     * @param uid the UID.
     * @return the DemographicData with the given UID, or null if no match.
     */
    DemographicData getDemographicData(String uid);

    /**
     * Returns the DemographicData with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the DemographicData with the given UID, or null if no match.
     */
    DemographicData getDemographicDataNoAcl(String uid);

    /**
     * Get all DemographicData.
     *
     * @return A list containing all DemographicData.
     */
    List<DemographicData> getAllDemographicData();

    /**
     * Returns the DemographicData which current user have READ access. If the current
     * user has the ALL authority then all DemographicData are returned.
     */
    List<DemographicData> getAllDataRead();

    /**
     * Returns the DemographicData which given user have READ access. If the current
     * user has the ALL authority then all DemographicData are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DemographicData which the given user has data read access to.
     */
    List<DemographicData> getUserDataRead(User user);

    /**
     * Returns the DemographicData which current user have WRITE access. If the
     * current user has the ALL authority then all DemographicData are returned.
     */
    List<DemographicData> getAllDataWrite();

    /**
     * Returns the DemographicData which current user have WRITE access. If the
     * current user has the ALL authority then all DemographicData are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DemographicData which given user has data write access to.
     */
    List<DemographicData> getUserDataWrite(User user);

}
