package org.nmcpye.activitiesmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DataSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DataSet entity.
 */
@Repository
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
    @Query("select dataSet from DataSet dataSet where dataSet.createdBy.login = ?#{principal.username}")
    List<DataSet> findByUserIsCurrentUser();

    @Query("select dataSet from DataSet dataSet where dataSet.lastUpdatedBy.login = ?#{principal.username}")
    List<DataSet> findByLastUpdatedByIsCurrentUser();

    @Query(
        value = "select distinct dataSet from DataSet dataSet left join fetch dataSet.sources",
        countQuery = "select count(distinct dataSet) from DataSet dataSet"
    )
    Page<DataSet> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct dataSet from DataSet dataSet left join fetch dataSet.sources")
    List<DataSet> findAllWithEagerRelationships();

    @Query("select dataSet from DataSet dataSet left join fetch dataSet.sources where dataSet.id =:id")
    Optional<DataSet> findOneWithEagerRelationships(@Param("id") Long id);
}
