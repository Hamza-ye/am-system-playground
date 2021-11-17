package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.domain.period.Period;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

import java.util.Date;
import java.util.List;

public interface DataSetService {
    String ID = DataSetService.class.getName();

    // -------------------------------------------------------------------------
    // DataSet
    // -------------------------------------------------------------------------

    /**
     * Adds a DataSet.
     *
     * @param dataSet The DataSet to add.
     * @return The generated unique identifier for this DataSet.
     */
    long addDataSet(DataSet dataSet);

    /**
     * Updates a DataSet.
     *
     * @param dataSet The DataSet to update.
     */
    void updateDataSet(DataSet dataSet);

    /**
     * Deletes a DataSet.
     *
     * @param dataSet The DataSet to delete.
     */
    void deleteDataSet(DataSet dataSet);

    /**
     * Get a DataSet
     *
     * @param id The unique identifier for the DataSet to get.
     * @return The DataSet with the given id or null if it does not exist.
     */
    DataSet getDataSet(long id);

    /**
     * Returns the DataSet with the given UID.
     *
     * @param uid the UID.
     * @return the DataSet with the given UID, or null if no match.
     */
    DataSet getDataSet(String uid);

    /**
     * Returns the DataSet with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the DataSet with the given UID, or null if no match.
     */
    DataSet getDataSetNoAcl(String uid);

    /**
     * Get all DataSets.
     *
     * @return A list containing all DataSets.
     */
    List<DataSet> getAllDataSets();

    /**
     * Gets all DataSets associated with the given PeriodType.
     *
     * @param periodType the PeriodType.
     * @return a list of DataSets.
     */
    List<DataSet> getDataSetsByPeriodType(PeriodType periodType);

    /**
     * Returns the data sets which current user have READ access. If the current
     * user has the ALL authority then all data sets are returned.
     */
    List<DataSet> getAllDataRead();

    /**
     * Returns the data sets which given user have READ access. If the current
     * user has the ALL authority then all data sets are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of data sets which the given user has data read access to.
     */
    List<DataSet> getUserDataRead(User user);

    /**
     * Returns the data sets which current user have WRITE access. If the
     * current user has the ALL authority then all data sets are returned.
     */
    List<DataSet> getAllDataWrite();

    /**
     * Returns the data sets which current user have WRITE access. If the
     * current user has the ALL authority then all data sets are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of data sets which given user has data write access to.
     */
    List<DataSet> getUserDataWrite(User user);

    /**
     * Checks whether the period is locked for data entry for the given input,
     * checking the dataset's expiryDays and lockExceptions.
     *
     * @param dataSet          the data set
     * @param period           the period.
     * @param organisationUnit the organisation unit.
     * @param now              the base date for deciding locked date, current date if null.
     * @return true or false indicating whether the system is locked or not.
     */
    boolean isLocked(User user, DataSet dataSet, Period period, OrganisationUnit organisationUnit, Date now);

    /**
     * Checks whether the system is locked for data entry for the given input,
     * checking expiryDays, lockExceptions and approvals.
     *
     * @param dataSet            the data set
     * @param period             the period.
     * @param organisationUnit   the organisation unit.
     * @param now                the base date for deciding locked date, current date if null.
     * @param useOrgUnitChildren whether to check children of the given org unit
     *                           or the org unit only.
     * @return true or false indicating whether the system is locked or not.
     */
    boolean isLocked(User user, DataSet dataSet, Period period, OrganisationUnit organisationUnit,
                     Date now, boolean useOrgUnitChildren);


//    /**
//     * Return a list of LockException with given filter list
//     *
//     * @param filters
//     * @return a list of LockException with given filter list
//     */
//    List<LockException> filterLockExceptions(List<String> filters);

    /**
     * Gets all data sets which are not assigned to any organisation units.
     */
    List<DataSet> getDataSetsNotAssignedToOrganisationUnits();
}
