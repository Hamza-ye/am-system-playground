package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.Warehouse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Warehouse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("select warehouse from Warehouse warehouse where warehouse.createdBy.login = ?#{principal.username}")
    List<Warehouse> findByUserIsCurrentUser();

    @Query("select warehouse from Warehouse warehouse where warehouse.lastUpdatedBy.login = ?#{principal.username}")
    List<Warehouse> findByLastUpdatedByIsCurrentUser();
}
