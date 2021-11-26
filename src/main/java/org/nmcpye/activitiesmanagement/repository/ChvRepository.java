package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Chv entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChvRepository extends JpaRepository<Chv, Long> {
    @Query("select chv from Chv chv where chv.createdBy.login = ?#{principal.username}")
    List<Chv> findByCreatedByIsCurrentUser();

    @Query("select chv from Chv chv where chv.lastUpdatedBy.login = ?#{principal.username}")
    List<Chv> findByLastUpdatedByIsCurrentUser();
}
