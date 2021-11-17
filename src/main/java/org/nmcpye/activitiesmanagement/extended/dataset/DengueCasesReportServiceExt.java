package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface DengueCasesReportServiceExt {

    String ID = DengueCasesReportServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // DengueCasesReport
    // -------------------------------------------------------------------------

    /**
     * Adds a DengueCasesReport.
     *
     * @param dengueCasesReport The DengueCasesReport to add.
     * @return The generated unique identifier for this DengueCasesReport.
     */
    long addDengueCasesReport(DengueCasesReport dengueCasesReport);

    /**
     * Updates a DengueCasesReport.
     *
     * @param dengueCasesReport The DengueCasesReport to update.
     */
    void updateDengueCasesReport(DengueCasesReport dengueCasesReport);

    /**
     * Deletes a DengueCasesReport.
     *
     * @param dengueCasesReport The DengueCasesReport to delete.
     */
    void deleteDengueCasesReport(DengueCasesReport dengueCasesReport);

    /**
     * Get a DengueCasesReport
     *
     * @param id The unique identifier for the DengueCasesReport to get.
     * @return The DengueCasesReport with the given id or null if it does not exist.
     */
    DengueCasesReport getDengueCasesReport(long id);

    /**
     * Returns the DengueCasesReport with the given UID.
     *
     * @param uid the UID.
     * @return the DengueCasesReport with the given UID, or null if no match.
     */
    DengueCasesReport getDengueCasesReport(String uid);

    /**
     * Returns the DengueCasesReport with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the DengueCasesReport with the given UID, or null if no match.
     */
    DengueCasesReport getDengueCasesReportNoAcl(String uid);

    /**
     * Get all DengueCasesReports.
     *
     * @return A list containing all DengueCasesReports.
     */
    List<DengueCasesReport> getAllDengueCasesReports();

    /**
     * Gets all DengueCasesReports associated with the given PeriodType.
     *
     * @param periodType the PeriodType.
     * @return a list of DengueCasesReports.
     */
    List<DengueCasesReport> getDengueCasesReportsByPeriodType(PeriodType periodType);

    /**
     * Returns DengueCasesReports which current user have READ access. If the current
     * user has the ALL authority then all DengueCasesReports are returned.
     */
    List<DengueCasesReport> getAllDataRead();

    /**
     * Returns DengueCasesReports which given user have READ access. If the current
     * user has the ALL authority then all DengueCasesReports are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DengueCasesReports which the given user has data read access to.
     */
    List<DengueCasesReport> getUserDataRead(User user);

    /**
     * Returns DengueCasesReports which current user have WRITE access. If the
     * current user has the ALL authority then all DengueCasesReports are returned.
     */
    List<DengueCasesReport> getAllDataWrite();

    /**
     * Returns DengueCasesReports which current user have WRITE access. If the
     * current user has the ALL authority then all DengueCasesReports are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of DengueCasesReports which given user has data write access to.
     */
    List<DengueCasesReport> getUserDataWrite(User user);

}
