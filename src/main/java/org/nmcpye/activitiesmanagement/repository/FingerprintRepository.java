package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import org.nmcpye.activitiesmanagement.domain.Fingerprint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fingerprint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, Long> {
    @Query("select fingerprint from Fingerprint fingerprint where fingerprint.user.login = ?#{principal.username}")
    List<Fingerprint> findByUserIsCurrentUser();

    @Query("select fingerprint from Fingerprint fingerprint where fingerprint.lastUpdatedBy.login = ?#{principal.username}")
    List<Fingerprint> findByLastUpdatedByIsCurrentUser();
}
