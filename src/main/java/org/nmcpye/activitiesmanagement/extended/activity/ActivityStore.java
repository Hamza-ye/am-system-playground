package org.nmcpye.activitiesmanagement.extended.activity;

import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Hamza on 20/10/2021.
 */
public interface ActivityStore extends IdentifiableObjectStore<Activity> {
    String ID = ActivityStore.class.getName();

    /**
     * Returns Activity with given personId.
     *
     * @param activityId ActivityId
     * @return Activity with given activityId
     */
    Activity getActivity(Long activityId);

    /**
     * Returns a list of activities based on the given query parameters.
     *
     * @param params the activity query parameters.
     * @return a List of activities.
     */
    List<Activity> getActivities(ActivityQueryParams params);

    /**
     * Returns a list of Activities based on the given query parameters.
     * If the specified list of orders are empty, default order of
     * last name and first name will be applied.
     *
     * @param params the Activity query parameters.
     * @param orders the already validated order strings .
     * @return a List of Activities.
     */
    List<Activity> getActivities(ActivityQueryParams params, @Nullable List<String> orders);

    /**
     * Returns number of all Activities
     *
     * @return number of Activities
     */
    int getActivityCount();

    /**
     * Returns the number of activities based on the given query parameters.
     *
     * @param params the activity query parameters.
     * @return number of activities.
     */
    int getActivityCount(ActivityQueryParams params);
}
