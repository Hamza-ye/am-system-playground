package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

import java.util.List;

/**
 * Created by Hamza on 16/11/2021.
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
    long addDemographicData(DemographicData demographicData);

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
    DemographicData getDemographicData(long id);

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
     * Get all DemographicDatas.
     *
     * @return A list containing all DemographicDatas.
     */
    List<DemographicData> getAllDemographicDatas();

    /**
     * Returns the DemographicDatas which current user have READ access. If the current
     * user has the ALL authority then all DemographicDatas are returned.
     */
    List<DemographicData> getAllDataRead();

    /**
     * Returns the DemographicDatas which given user have READ access. If the current
     * user has the ALL authority then all DemographicDatas are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DemographicDatas which the given user has data read access to.
     */
    List<DemographicData> getUserDataRead(User user);

    /**
     * Returns the DemographicDatas which current user have WRITE access. If the
     * current user has the ALL authority then all DemographicDatas are returned.
     */
    List<DemographicData> getAllDataWrite();

    /**
     * Returns the DemographicDatas which current user have WRITE access. If the
     * current user has the ALL authority then all DemographicDatas are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DemographicDatas which given user has data write access to.
     */
    List<DemographicData> getUserDataWrite(User user);

}
