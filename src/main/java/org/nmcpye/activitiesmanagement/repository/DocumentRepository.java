package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("select document from Document document where document.createdBy.login = ?#{principal.username}")
    List<Document> findByCreatedByIsCurrentUser();

    @Query("select document from Document document where document.lastUpdatedBy.login = ?#{principal.username}")
    List<Document> findByLastUpdatedByIsCurrentUser();
}
