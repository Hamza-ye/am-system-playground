package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.DataProvider;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataProvider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataProviderRepository extends JpaRepository<DataProvider, Long> {
    @Query("select dataProvider from DataProvider dataProvider where dataProvider.user.login = ?#{principal.username}")
    List<DataProvider> findByUserIsCurrentUser();

    @Query("select dataProvider from DataProvider dataProvider where dataProvider.lastUpdatedBy.login = ?#{principal.username}")
    List<DataProvider> findByLastUpdatedByIsCurrentUser();
}
