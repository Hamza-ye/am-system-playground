package org.nmcpye.activitiesmanagement.extended.activity;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.activity.Activity;

import java.util.List;

/**
 * Created by Hamza on 20/10/2021.
 */
public interface ActivityServiceExtended {
    String ID = ActivityServiceExtended.class.getName();

    // -------------------------------------------------------------------------
    // Activity
    // -------------------------------------------------------------------------

    /**
     * Adds a Activity.
     *
     * @param activity The Activity to add.
     * @return The generated unique identifier for this Activity.
     */
    long addActivity(Activity activity);

    /**
     * Updates a Activity.
     *
     * @param activity The Activity to update.
     */
    void updateActivity(Activity activity);

    /**
     * Deletes a Activity.
     *
     * @param activity The Activity to delete.
     */
    void deleteActivity(Activity activity);

    /**
     * Get a Activity
     *
     * @param id The unique identifier for the Activity to get.
     * @return The Activity with the given id or null if it does not exist.
     */
    Activity getActivity(long id);

    /**
     * Returns the Activity with the given UID.
     *
     * @param uid the UID.
     * @return the Activity with the given UID, or null if no match.
     */
    Activity getActivity(String uid);

    /**
     * Returns the Activity with the given UID. Bypasses the ACL system.
     *
     * @param uid the UID.
     * @return the Activity with the given UID, or null if no match.
     */
    Activity getActivityNoAcl(String uid);

    /**
     * Get all Activitys.
     *
     * @return A list containing all Activitys.
     */
    List<Activity> getAllActivitys();

    /**
     * Returns the Chvs which current user have READ access. If the current
     * user has the ALL authority then all Chvs are returned.
     */
    List<Activity> getAllDataRead();

    /**
     * Returns the Chvs which given user have READ access. If the current
     * user has the ALL authority then all Chvs are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of Chvs which the given user has data read access to.
     */
    List<Activity> getUserDataRead(User user);

    /**
     * Returns the Chvs which current user have WRITE access. If the
     * current user has the ALL authority then all Chvs are returned.
     */
    List<Activity> getAllDataWrite();

    /**
     * Returns the Chvs which current user have WRITE access. If the
     * current user has the ALL authority then all Chvs are returned.
     *
     * @param user the user to query for data set list.
     * @return a list of Chvs which given user has data write access to.
     */
    List<Activity> getUserDataWrite(User user);
}
