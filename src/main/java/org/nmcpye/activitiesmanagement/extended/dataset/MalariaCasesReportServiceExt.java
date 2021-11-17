package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface MalariaCasesReportServiceExt {

    String ID = MalariaCasesReportServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // MalariaCasesReport
    // -------------------------------------------------------------------------

    /**
     * Adds a MalariaCasesReport.
     *
     * @param malariaCasesReport The MalariaCasesReport to add.
     * @return The generated unique identifier for this MalariaCasesReport.
     */
    Long addMalariaCasesReport(MalariaCasesReport malariaCasesReport);

    /**
     * Updates a MalariaCasesReport.
     *
     * @param malariaCasesReport The MalariaCasesReport to update.
     */
    void updateMalariaCasesReport(MalariaCasesReport malariaCasesReport);

    /**
     * Deletes a MalariaCasesReport.
     *
     * @param malariaCasesReport The MalariaCasesReport to delete.
     */
    void deleteMalariaCasesReport(MalariaCasesReport malariaCasesReport);

    /**
     * Get a MalariaCasesReport
     *
     * @param id The unique identifier for the MalariaCasesReport to get.
     * @return The MalariaCasesReport with the given id or null if it does not exist.
     */
    MalariaCasesReport getMalariaCasesReport(Long id);

    /**
     * Returns the MalariaCasesReport with the given UID.
     *
     * @param uid the UID.
     * @return the MalariaCasesReport with the given UID, or null if no match.
     */
    MalariaCasesReport getMalariaCasesReport(String uid);

    /**
     * Returns the MalariaCasesReport with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the MalariaCasesReport with the given UID, or null if no match.
     */
    MalariaCasesReport getMalariaCasesReportNoAcl(String uid);

    /**
     * Get all MalariaCasesReports.
     *
     * @return A list containing all MalariaCasesReports.
     */
    List<MalariaCasesReport> getAllMalariaCasesReports();

    /**
     * Gets all MalariaCasesReports associated with the given PeriodType.
     *
     * @param periodType the PeriodType.
     * @return a list of MalariaCasesReports.
     */
    List<MalariaCasesReport> getMalariaCasesReportsByPeriodType(PeriodType periodType);

    /**
     * Returns MalariaCasesReports which current user have READ access. If the current
     * user has the ALL authority then all MalariaCasesReports are returned.
     */
    List<MalariaCasesReport> getAllDataRead();

    /**
     * Returns MalariaCasesReports which given user have READ access. If the current
     * user has the ALL authority then all MalariaCasesReports are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of MalariaCasesReports which the given user has data read access to.
     */
    List<MalariaCasesReport> getUserDataRead(User user);

    /**
     * Returns MalariaCasesReports which current user have WRITE access. If the
     * current user has the ALL authority then all MalariaCasesReports are returned.
     */
    List<MalariaCasesReport> getAllDataWrite();

    /**
     * Returns MalariaCasesReports which current user have WRITE access. If the
     * current user has the ALL authority then all MalariaCasesReports are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of MalariaCasesReports which given user has data write access to.
     */
    List<MalariaCasesReport> getUserDataWrite(User user);

}
