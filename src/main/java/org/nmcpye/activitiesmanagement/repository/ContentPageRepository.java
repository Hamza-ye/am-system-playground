package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.ContentPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the ContentPage entity.
 */
@Repository
public interface ContentPageRepository extends JpaRepository<ContentPage, Long> {
    @Query("select contentPage from ContentPage contentPage where contentPage.createdBy.login = ?#{principal.username}")
    List<ContentPage> findByCreatedByIsCurrentUser();

    @Query("select contentPage from ContentPage contentPage where contentPage.lastUpdatedBy.login = ?#{principal.username}")
    List<ContentPage> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct contentPage from ContentPage contentPage left join fetch contentPage.relatedLinks",
        countQuery = "select count(distinct contentPage) from ContentPage contentPage"
    )
    Page<ContentPage> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct contentPage from ContentPage contentPage left join fetch contentPage.relatedLinks")
    List<ContentPage> findAllWithEagerRelationships();

    @Query("select contentPage from ContentPage contentPage left join fetch contentPage.relatedLinks where contentPage.id =:id")
    Optional<ContentPage> findOneWithEagerRelationships(@Param("id") Long id);
}
