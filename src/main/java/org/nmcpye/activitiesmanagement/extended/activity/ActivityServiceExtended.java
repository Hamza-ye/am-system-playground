package org.nmcpye.activitiesmanagement.extended.activity;

import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.extended.person.PersonQueryParams;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface ActivityServiceExtended {
    String ID = ActivityServiceExtended.class.getName();

    // -------------------------------------------------------------------------
    // Activity
    // -------------------------------------------------------------------------

    /**
     * Adds a Activity.
     *
     * @param activity the Activity to add.
     * @return the generated identifier.
     */
    Long addActivity(Activity activity);

    /**
     * Updates a Activity.
     *
     * @param activity the Activity to update.
     */
    void updateActivity(Activity activity);

    /**
     * Retrieves the Activity with the given identifier.
     *
     * @param id the identifier of the Activity to retrieve.
     * @return the Activity.
     */
    Activity getActivity(Long id);

    /**
     * Retrieves the Activity with the given unique identifier.
     *
     * @param uid the identifier of the Activity to retrieve.
     * @return the Activity.
     */
    Activity getActivity(String uid);

    /**
     * Retrieves the Activity with the given username.
     *
     * @param name the name of the Activity to retrieve.
     * @return the Activity.
     */
    Activity getActivityByActivityname(String name);

    /**
     * Retrieves the Activity by attempting to look up by various identifiers
     * in the following order:
     *
     * <ul>
     * <li>UID</li>
     * <li>UUID</li>
     * <li>Activityname</li>
     * </ul>
     *
     * @param id the Activity identifier.
     * @return the Activity, or null if not found.
     */
    Activity getActivityByIdentifier(String id);

    /**
     * Retrieves a collection of Activity with the given unique identifiers.
     *
     * @param uids the identifiers of the collection of Activitys to retrieve.
     * @return the activities.
     */
    List<Activity> getActivities(Collection<String> uids);

    /**
     * Returns a List of all Activities.
     *
     * @return a Collection of Activities.
     */
    List<Activity> getAllActivities();

    /**
     * Deletes an Activity.
     *
     * @param activity the Activity to delete.
     */
    void deleteActivity(Activity activity);

    /**
     * Returns a list of users based on the given query parameters.
     * The default order of last name and first name will be applied.
     *
     * @param params the activity query parameters.
     * @return a List of activities.
     */
    List<Activity> getActivities(ActivityQueryParams params);

    /**
     * Returns a list of users based on the given query parameters.
     * If the specified list of orders are empty, default order of
     * last name and first name will be applied.
     *
     * @param params the activity query parameters.
     * @param orders the already validated order strings (e.g. email:asc).
     * @return a List of activities.
     */
    List<Activity> getActivitys(PersonQueryParams params, @Nullable List<String> orders);

    /**
     * Returns the number of activities based on the given query parameters.
     *
     * @param params the activity query parameters.
     * @return number of activities.
     */
    int getActivityCount(PersonQueryParams params);

    /**
     * Returns number of all activities
     *
     * @return number of activities
     */
    int getActivityCount();
}
