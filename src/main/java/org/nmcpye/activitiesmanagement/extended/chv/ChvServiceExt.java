package org.nmcpye.activitiesmanagement.extended.chv;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.chv.CHV;

import java.util.List;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface ChvServiceExt {

    String ID = ChvServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // CHV
    // -------------------------------------------------------------------------

    /**
     * Adds a CHV.
     *
     * @param chv The CHV to add.
     * @return The generated unique identifier for this CHV.
     */
    long addCHV(CHV chv);

    /**
     * Updates a CHV.
     *
     * @param chv The CHV to update.
     */
    void updateCHV(CHV chv);

    /**
     * Deletes a CHV.
     *
     * @param chv The CHV to delete.
     */
    void deleteCHV(CHV chv);

    /**
     * Get a CHV
     *
     * @param id The unique identifier for the CHV to get.
     * @return The CHV with the given id or null if it does not exist.
     */
    CHV getCHV(long id);

    /**
     * Returns the CHV with the given UID.
     *
     * @param uid the UID.
     * @return the CHV with the given UID, or null if no match.
     */
    CHV getCHV(String uid);

    /**
     * Returns the CHV with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the CHV with the given UID, or null if no match.
     */
    CHV getCHVNoAcl(String uid);

    /**
     * Get all CHVs.
     *
     * @return A list containing all CHVs.
     */
    List<CHV> getAllCHVs();

    /**
     * Returns the Chvs which current user have READ access. If the current
     * user has the ALL authority then all Chvs are returned.
     */
    List<CHV> getAllDataRead();

    /**
     * Returns the Chvs which given user have READ access. If the current
     * user has the ALL authority then all Chvs are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of Chvs which the given user has data read access to.
     */
    List<CHV> getUserDataRead(User user);

    /**
     * Returns the Chvs which current user have WRITE access. If the
     * current user has the ALL authority then all Chvs are returned.
     */
    List<CHV> getAllDataWrite();

    /**
     * Returns the Chvs which current user have WRITE access. If the
     * current user has the ALL authority then all Chvs are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of Chvs which given user has data write access to.
     */
    List<CHV> getUserDataWrite(User user);

}
