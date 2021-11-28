package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ContentPage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentPageRepository extends JpaRepository<ContentPage, Long> {}
