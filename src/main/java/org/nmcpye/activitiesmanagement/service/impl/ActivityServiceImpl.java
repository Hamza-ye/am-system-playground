package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Activity;
import org.nmcpye.activitiesmanagement.repository.ActivityRepository;
import org.nmcpye.activitiesmanagement.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Activity}.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;

    public ActivityServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public Activity save(Activity activity) {
        log.debug("Request to save Activity : {}", activity);
        return activityRepository.save(activity);
    }

    @Override
    public Optional<Activity> partialUpdate(Activity activity) {
        log.debug("Request to partially update Activity : {}", activity);

        return activityRepository
            .findById(activity.getId())
            .map(
                existingActivity -> {
                    if (activity.getUid() != null) {
                        existingActivity.setUid(activity.getUid());
                    }
                    if (activity.getCode() != null) {
                        existingActivity.setCode(activity.getCode());
                    }
                    if (activity.getName() != null) {
                        existingActivity.setName(activity.getName());
                    }
                    if (activity.getCreated() != null) {
                        existingActivity.setCreated(activity.getCreated());
                    }
                    if (activity.getLastUpdated() != null) {
                        existingActivity.setLastUpdated(activity.getLastUpdated());
                    }
                    if (activity.getStartDate() != null) {
                        existingActivity.setStartDate(activity.getStartDate());
                    }
                    if (activity.getEndDate() != null) {
                        existingActivity.setEndDate(activity.getEndDate());
                    }
                    if (activity.getNoOfDays() != null) {
                        existingActivity.setNoOfDays(activity.getNoOfDays());
                    }
                    if (activity.getActive() != null) {
                        existingActivity.setActive(activity.getActive());
                    }
                    if (activity.getIsDisplayed() != null) {
                        existingActivity.setIsDisplayed(activity.getIsDisplayed());
                    }

                    return existingActivity;
                }
            )
            .map(activityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Activity> findAll(Pageable pageable) {
        log.debug("Request to get all Activities");
        return activityRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Activity> findOne(Long id) {
        log.debug("Request to get Activity : {}", id);
        return activityRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activity : {}", id);
        activityRepository.deleteById(id);
    }
}
