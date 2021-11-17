package org.nmcpye.activitiesmanagement.extended.activity;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.extended.activity.pagingrepository.ActivityPagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 16/11/2021.
 */
@Service
public class DefaultActivityService implements ActivityServiceExtended {

    private final ActivityPagingRepository activityPagingRepository;

    public DefaultActivityService(ActivityPagingRepository activityPagingRepository) {
        this.activityPagingRepository = activityPagingRepository;
    }


    @Override
    public long addActivity(Activity casesReportClass) {
        activityPagingRepository.saveObject(casesReportClass);
        return casesReportClass.getId();
    }

    @Override
    public void updateActivity(Activity casesReportClass) {
        activityPagingRepository.update(casesReportClass);
    }

    @Override
    public void deleteActivity(Activity casesReportClass) {
        activityPagingRepository.delete(casesReportClass);
    }

    @Override
    public Activity getActivity(long id) {
        return activityPagingRepository.get(id);
    }

    @Override
    public Activity getActivity(String uid) {
        return activityPagingRepository.getByUid(uid);
    }

    @Override
    public Activity getActivityNoAcl(String uid) {
        return activityPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<Activity> getAllActivitys() {
        return activityPagingRepository.getAll();
    }

    @Override
    public List<Activity> getAllDataRead() {
        return activityPagingRepository.getDataReadAll();
    }

    @Override
    public List<Activity> getUserDataRead(User user) {
        return activityPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<Activity> getAllDataWrite() {
        return activityPagingRepository.getDataWriteAll();
    }

    @Override
    public List<Activity> getUserDataWrite(User user) {
        return activityPagingRepository.getDataWriteAll(user);
    }
}
