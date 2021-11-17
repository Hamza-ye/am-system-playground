package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;

import java.util.List;

/**
 * Created by Hamza on 16/11/2021.
 */
public interface CasesReportClassServiceExt {
    String ID = CasesReportClassServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // CasesReportClass
    // -------------------------------------------------------------------------

    /**
     * Adds a CasesReportClass.
     *
     * @param casesReportClass The CasesReportClass to add.
     * @return The generated unique identifier for this CasesReportClass.
     */
    long addCasesReportClass(CasesReportClass casesReportClass);

    /**
     * Updates a CasesReportClass.
     *
     * @param casesReportClass The CasesReportClass to update.
     */
    void updateCasesReportClass(CasesReportClass casesReportClass);

    /**
     * Deletes a CasesReportClass.
     *
     * @param casesReportClass The CasesReportClass to delete.
     */
    void deleteCasesReportClass(CasesReportClass casesReportClass);

    /**
     * Get a CasesReportClass
     *
     * @param id The unique identifier for the CasesReportClass to get.
     * @return The CasesReportClass with the given id or null if it does not exist.
     */
    CasesReportClass getCasesReportClass(long id);

    /**
     * Returns the CasesReportClass with the given UID.
     *
     * @param uid the UID.
     * @return the CasesReportClass with the given UID, or null if no match.
     */
    CasesReportClass getCasesReportClass(String uid);

    /**
     * Returns the CasesReportClass with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the CasesReportClass with the given UID, or null if no match.
     */
    CasesReportClass getCasesReportClassNoAcl(String uid);

    /**
     * Get all CasesReportClasss.
     *
     * @return A list containing all CasesReportClasss.
     */
    List<CasesReportClass> getAllCasesReportClasss();

    /**
     * Gets all CasesReportClasss associated with the given PeriodType.
     *
     * @param periodType the PeriodType.
     * @return a list of CasesReportClasss.
     */
    List<CasesReportClass> getCasesReportClasssByPeriodType(PeriodType periodType);

    /**
     * Returns CasesReportClasss which current user have READ access. If the current
     * user has the ALL authority then all CasesReportClasss are returned.
     */
    List<CasesReportClass> getAllDataRead();

    /**
     * Returns CasesReportClasss which given user have READ access. If the current
     * user has the ALL authority then all CasesReportClasss are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of CasesReportClasss which the given user has data read access to.
     */
    List<CasesReportClass> getUserDataRead(User user);

    /**
     * Returns CasesReportClasss which current user have WRITE access. If the
     * current user has the ALL authority then all CasesReportClasss are returned.
     */
    List<CasesReportClass> getAllDataWrite();

    /**
     * Returns CasesReportClasss which current user have WRITE access. If the
     * current user has the ALL authority then all CasesReportClasss are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of CasesReportClasss which given user has data write access to.
     */
    List<CasesReportClass> getUserDataWrite(User user);

}
