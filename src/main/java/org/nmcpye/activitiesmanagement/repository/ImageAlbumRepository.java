package org.nmcpye.activitiesmanagement.repository;

import org.nmcpye.activitiesmanagement.domain.ImageAlbum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the ImageAlbum entity.
 */
@Repository
public interface ImageAlbumRepository extends JpaRepository<ImageAlbum, Long> {
    @Query(
        value = "select distinct imageAlbum from ImageAlbum imageAlbum left join fetch imageAlbum.images",
        countQuery = "select count(distinct imageAlbum) from ImageAlbum imageAlbum"
    )
    Page<ImageAlbum> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct imageAlbum from ImageAlbum imageAlbum left join fetch imageAlbum.images")
    List<ImageAlbum> findAllWithEagerRelationships();

    @Query("select imageAlbum from ImageAlbum imageAlbum left join fetch imageAlbum.images where imageAlbum.id =:id")
    Optional<ImageAlbum> findOneWithEagerRelationships(@Param("id") Long id);
}
