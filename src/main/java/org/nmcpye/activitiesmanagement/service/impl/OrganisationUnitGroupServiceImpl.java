package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitGroup;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitGroupRepository;
import org.nmcpye.activitiesmanagement.service.OrganisationUnitGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganisationUnitGroup}.
 */
@Service
@Transactional
public class OrganisationUnitGroupServiceImpl implements OrganisationUnitGroupService {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitGroupServiceImpl.class);

    private final OrganisationUnitGroupRepository organisationUnitGroupRepository;

    public OrganisationUnitGroupServiceImpl(OrganisationUnitGroupRepository organisationUnitGroupRepository) {
        this.organisationUnitGroupRepository = organisationUnitGroupRepository;
    }

    @Override
    public OrganisationUnitGroup save(OrganisationUnitGroup organisationUnitGroup) {
        log.debug("Request to save OrganisationUnitGroup : {}", organisationUnitGroup);
        return organisationUnitGroupRepository.save(organisationUnitGroup);
    }

    @Override
    public Optional<OrganisationUnitGroup> partialUpdate(OrganisationUnitGroup organisationUnitGroup) {
        log.debug("Request to partially update OrganisationUnitGroup : {}", organisationUnitGroup);

        return organisationUnitGroupRepository
            .findById(organisationUnitGroup.getId())
            .map(
                existingOrganisationUnitGroup -> {
                    if (organisationUnitGroup.getUid() != null) {
                        existingOrganisationUnitGroup.setUid(organisationUnitGroup.getUid());
                    }
                    if (organisationUnitGroup.getCode() != null) {
                        existingOrganisationUnitGroup.setCode(organisationUnitGroup.getCode());
                    }
                    if (organisationUnitGroup.getName() != null) {
                        existingOrganisationUnitGroup.setName(organisationUnitGroup.getName());
                    }
                    if (organisationUnitGroup.getShortName() != null) {
                        existingOrganisationUnitGroup.setShortName(organisationUnitGroup.getShortName());
                    }
                    if (organisationUnitGroup.getCreated() != null) {
                        existingOrganisationUnitGroup.setCreated(organisationUnitGroup.getCreated());
                    }
                    if (organisationUnitGroup.getLastUpdated() != null) {
                        existingOrganisationUnitGroup.setLastUpdated(organisationUnitGroup.getLastUpdated());
                    }
                    if (organisationUnitGroup.getSymbol() != null) {
                        existingOrganisationUnitGroup.setSymbol(organisationUnitGroup.getSymbol());
                    }
                    if (organisationUnitGroup.getColor() != null) {
                        existingOrganisationUnitGroup.setColor(organisationUnitGroup.getColor());
                    }

                    return existingOrganisationUnitGroup;
                }
            )
            .map(organisationUnitGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitGroup> findAll() {
        log.debug("Request to get all OrganisationUnitGroups");
        return organisationUnitGroupRepository.findAllWithEagerRelationships();
    }

    public Page<OrganisationUnitGroup> findAllWithEagerRelationships(Pageable pageable) {
        return organisationUnitGroupRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganisationUnitGroup> findOne(Long id) {
        log.debug("Request to get OrganisationUnitGroup : {}", id);
        return organisationUnitGroupRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganisationUnitGroup : {}", id);
        organisationUnitGroupRepository.deleteById(id);
    }
}
