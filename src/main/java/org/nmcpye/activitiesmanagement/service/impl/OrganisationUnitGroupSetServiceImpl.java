package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitGroupSet;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitGroupSetRepository;
import org.nmcpye.activitiesmanagement.service.OrganisationUnitGroupSetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganisationUnitGroupSet}.
 */
@Service
@Transactional
public class OrganisationUnitGroupSetServiceImpl implements OrganisationUnitGroupSetService {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitGroupSetServiceImpl.class);

    private final OrganisationUnitGroupSetRepository organisationUnitGroupSetRepository;

    public OrganisationUnitGroupSetServiceImpl(OrganisationUnitGroupSetRepository organisationUnitGroupSetRepository) {
        this.organisationUnitGroupSetRepository = organisationUnitGroupSetRepository;
    }

    @Override
    public OrganisationUnitGroupSet save(OrganisationUnitGroupSet organisationUnitGroupSet) {
        log.debug("Request to save OrganisationUnitGroupSet : {}", organisationUnitGroupSet);
        return organisationUnitGroupSetRepository.save(organisationUnitGroupSet);
    }

    @Override
    public Optional<OrganisationUnitGroupSet> partialUpdate(OrganisationUnitGroupSet organisationUnitGroupSet) {
        log.debug("Request to partially update OrganisationUnitGroupSet : {}", organisationUnitGroupSet);

        return organisationUnitGroupSetRepository
            .findById(organisationUnitGroupSet.getId())
            .map(
                existingOrganisationUnitGroupSet -> {
                    if (organisationUnitGroupSet.getUid() != null) {
                        existingOrganisationUnitGroupSet.setUid(organisationUnitGroupSet.getUid());
                    }
                    if (organisationUnitGroupSet.getCode() != null) {
                        existingOrganisationUnitGroupSet.setCode(organisationUnitGroupSet.getCode());
                    }
                    if (organisationUnitGroupSet.getName() != null) {
                        existingOrganisationUnitGroupSet.setName(organisationUnitGroupSet.getName());
                    }
                    if (organisationUnitGroupSet.getCreated() != null) {
                        existingOrganisationUnitGroupSet.setCreated(organisationUnitGroupSet.getCreated());
                    }
                    if (organisationUnitGroupSet.getLastUpdated() != null) {
                        existingOrganisationUnitGroupSet.setLastUpdated(organisationUnitGroupSet.getLastUpdated());
                    }
                    if (organisationUnitGroupSet.getCompulsory() != null) {
                        existingOrganisationUnitGroupSet.setCompulsory(organisationUnitGroupSet.getCompulsory());
                    }
                    if (organisationUnitGroupSet.getIncludeSubhierarchyInAnalytics() != null) {
                        existingOrganisationUnitGroupSet.setIncludeSubhierarchyInAnalytics(
                            organisationUnitGroupSet.getIncludeSubhierarchyInAnalytics()
                        );
                    }

                    return existingOrganisationUnitGroupSet;
                }
            )
            .map(organisationUnitGroupSetRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroupSet> findAll() {
        log.debug("Request to get all OrganisationUnitGroupSets");
        return organisationUnitGroupSetRepository.findAllWithEagerRelationships();
    }

    public Page<OrganisationUnitGroupSet> findAllWithEagerRelationships(Pageable pageable) {
        return organisationUnitGroupSetRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganisationUnitGroupSet> findOne(Long id) {
        log.debug("Request to get OrganisationUnitGroupSet : {}", id);
        return organisationUnitGroupSetRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganisationUnitGroupSet : {}", id);
        organisationUnitGroupSetRepository.deleteById(id);
    }
}
