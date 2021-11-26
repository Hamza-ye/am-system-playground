package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Chv entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChvRepository extends JpaRepository<Chv, Long> {
    @Query("select cHV from Chv cHV where cHV.createdBy.login = ?#{principal.username}")
    List<Chv> findByUserIsCurrentUser();

    @Query("select cHV from Chv cHV where cHV.lastUpdatedBy.login = ?#{principal.username}")
    List<Chv> findByLastUpdatedByIsCurrentUser();
}
