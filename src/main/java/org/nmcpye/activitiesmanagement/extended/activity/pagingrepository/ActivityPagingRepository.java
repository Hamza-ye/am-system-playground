package org.nmcpye.activitiesmanagement.extended.activity.pagingrepository;

import org.nmcpye.activitiesmanagement.domain.activity.Activity;
import org.nmcpye.activitiesmanagement.extended.activity.ActivityStore;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Activity entity.
 * created by Hamza Assada 12-11-2021
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityPagingRepository
    extends ActivityStore, PagingAndSortingRepository<Activity, Long> {

}
