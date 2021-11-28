package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.RelatedLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RelatedLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatedLinkRepository extends JpaRepository<RelatedLink, Long> {}
