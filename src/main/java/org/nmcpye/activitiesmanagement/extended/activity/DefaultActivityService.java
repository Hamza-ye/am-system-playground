package org.nmcpye.activitiesmanagement.extended.activity;

import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.extended.person.PersonQueryParams;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hamza on 16/11/2021.
 */
@Service("org.nmcpye.activitiesmanagement.extended.activity.ActivityServiceExtended")
public class DefaultActivityService implements ActivityServiceExtended {

    private final ActivityStore activityStore;

    public DefaultActivityService(ActivityStore activityStore) {
        this.activityStore = activityStore;
    }


    @Override
    public Long addActivity(Activity activity) {
        return null;
    }

    @Override
    public void updateActivity(Activity activity) {

    }

    @Override
    public void deleteActivity(Activity activity) {

    }

    @Override
    public Activity getActivity(Long id) {
        return null;
    }

    @Override
    public Activity getActivity(String uid) {
        return null;
    }

    @Override
    public Activity getActivityByActivityname(String name) {
        return null;
    }

    @Override
    public Activity getActivityByIdentifier(String id) {
        return null;
    }

    @Override
    public List<Activity> getActivities(Collection<String> uids) {
        return null;
    }

    @Override
    public List<Activity> getAllActivities() {
        return null;
    }

    @Override
    public List<Activity> getActivities(ActivityQueryParams params) {
        return null;
    }

    @Override
    public List<Activity> getActivitys(PersonQueryParams params, @Nullable List<String> orders) {
        return null;
    }

    @Override
    public int getActivityCount(PersonQueryParams params) {
        return 0;
    }

    @Override
    public int getActivityCount() {
        return 0;
    }

}
