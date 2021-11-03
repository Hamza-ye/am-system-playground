package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.organisationunit.OrganisationUnit;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitRepository;
import org.nmcpye.activitiesmanagement.service.OrganisationUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganisationUnit}.
 */
@Service
@Transactional
public class OrganisationUnitServiceImpl implements OrganisationUnitService {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitServiceImpl.class);

    private final OrganisationUnitRepository organisationUnitRepository;

    public OrganisationUnitServiceImpl(OrganisationUnitRepository organisationUnitRepository) {
        this.organisationUnitRepository = organisationUnitRepository;
    }

    @Override
    public OrganisationUnit save(OrganisationUnit organisationUnit) {
        log.debug("Request to save OrganisationUnit : {}", organisationUnit);
        return organisationUnitRepository.save(organisationUnit);
    }

    @Override
    public Optional<OrganisationUnit> partialUpdate(OrganisationUnit organisationUnit) {
        log.debug("Request to partially update OrganisationUnit : {}", organisationUnit);

        return organisationUnitRepository
            .findById(organisationUnit.getId())
            .map(
                existingOrganisationUnit -> {
                    if (organisationUnit.getUid() != null) {
                        existingOrganisationUnit.setUid(organisationUnit.getUid());
                    }
                    if (organisationUnit.getCode() != null) {
                        existingOrganisationUnit.setCode(organisationUnit.getCode());
                    }
                    if (organisationUnit.getName() != null) {
                        existingOrganisationUnit.setName(organisationUnit.getName());
                    }
                    if (organisationUnit.getShortName() != null) {
                        existingOrganisationUnit.setShortName(organisationUnit.getShortName());
                    }
                    if (organisationUnit.getCreated() != null) {
                        existingOrganisationUnit.setCreated(organisationUnit.getCreated());
                    }
                    if (organisationUnit.getLastUpdated() != null) {
                        existingOrganisationUnit.setLastUpdated(organisationUnit.getLastUpdated());
                    }
                    if (organisationUnit.getPath() != null) {
                        existingOrganisationUnit.setPath(organisationUnit.getPath());
                    }
                    if (organisationUnit.getHierarchyLevel() != null) {
                        existingOrganisationUnit.setHierarchyLevel(organisationUnit.getHierarchyLevel());
                    }
                    if (organisationUnit.getOpeningDate() != null) {
                        existingOrganisationUnit.setOpeningDate(organisationUnit.getOpeningDate());
                    }
                    if (organisationUnit.getComment() != null) {
                        existingOrganisationUnit.setComment(organisationUnit.getComment());
                    }
                    if (organisationUnit.getClosedDate() != null) {
                        existingOrganisationUnit.setClosedDate(organisationUnit.getClosedDate());
                    }
                    if (organisationUnit.getUrl() != null) {
                        existingOrganisationUnit.setUrl(organisationUnit.getUrl());
                    }
                    if (organisationUnit.getContactPerson() != null) {
                        existingOrganisationUnit.setContactPerson(organisationUnit.getContactPerson());
                    }
                    if (organisationUnit.getAddress() != null) {
                        existingOrganisationUnit.setAddress(organisationUnit.getAddress());
                    }
                    if (organisationUnit.getEmail() != null) {
                        existingOrganisationUnit.setEmail(organisationUnit.getEmail());
                    }
                    if (organisationUnit.getPhoneNumber() != null) {
                        existingOrganisationUnit.setPhoneNumber(organisationUnit.getPhoneNumber());
                    }
                    if (organisationUnit.getOrganisationUnitType() != null) {
                        existingOrganisationUnit.setOrganisationUnitType(organisationUnit.getOrganisationUnitType());
                    }

                    return existingOrganisationUnit;
                }
            )
            .map(organisationUnitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnit> findAll() {
        log.debug("Request to get all OrganisationUnits");
        return organisationUnitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganisationUnit> findOne(Long id) {
        log.debug("Request to get OrganisationUnit : {}", id);
        return organisationUnitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganisationUnit : {}", id);
        organisationUnitRepository.deleteById(id);
    }
}
