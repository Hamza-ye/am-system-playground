package org.nmcpye.activitiesmanagement.extended.chv;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;

import java.util.List;

/**
 * Created by Hamza on 17-11-2021.
 */
public interface ChvServiceExt {

    String ID = ChvServiceExt.class.getName();

    // -------------------------------------------------------------------------
    // Chv
    // -------------------------------------------------------------------------

    /**
     * Adds a Chv.
     *
     * @param chv The Chv to add.
     * @return The generated unique identifier for this Chv.
     */
    Long addCHV(Chv chv);

    /**
     * Updates a Chv.
     *
     * @param chv The Chv to update.
     */
    void updateCHV(Chv chv);

    /**
     * Deletes a Chv.
     *
     * @param chv The Chv to delete.
     */
    void deleteCHV(Chv chv);

    /**
     * Get a Chv
     *
     * @param id The unique identifier for the Chv to get.
     * @return The Chv with the given id or null if it does not exist.
     */
    Chv getCHV(Long id);

    /**
     * Returns the Chv with the given UID.
     *
     * @param uid the UID.
     * @return the Chv with the given UID, or null if no match.
     */
    Chv getCHV(String uid);

    /**
     * Returns the Chv with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the Chv with the given UID, or null if no match.
     */
    Chv getCHVNoAcl(String uid);

    /**
     * Get all CHVs.
     *
     * @return A list containing all CHVs.
     */
    List<Chv> getAllCHVs();

    /**
     * Returns the Chvs which current user have READ access. If the current
     * user has the ALL authority then all Chvs are returned.
     */
    List<Chv> getAllDataRead();

    /**
     * Returns the Chvs which given user have READ access. If the current
     * user has the ALL authority then all Chvs are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of Chvs which the given user has data read access to.
     */
    List<Chv> getUserDataRead(User user);

    /**
     * Returns the Chvs which current user have WRITE access. If the
     * current user has the ALL authority then all Chvs are returned.
     */
    List<Chv> getAllDataWrite();

    /**
     * Returns the Chvs which current user have WRITE access. If the
     * current user has the ALL authority then all Chvs are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of Chvs which given user has data write access to.
     */
    List<Chv> getUserDataWrite(User user);

}
