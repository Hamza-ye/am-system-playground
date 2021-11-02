package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.OrganisationUnitLevel;
import org.nmcpye.activitiesmanagement.repository.OrganisationUnitLevelRepository;
import org.nmcpye.activitiesmanagement.service.OrganisationUnitLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganisationUnitLevel}.
 */
@Service
@Transactional
public class OrganisationUnitLevelServiceImpl implements OrganisationUnitLevelService {

    private final Logger log = LoggerFactory.getLogger(OrganisationUnitLevelServiceImpl.class);

    private final OrganisationUnitLevelRepository organisationUnitLevelRepository;

    public OrganisationUnitLevelServiceImpl(OrganisationUnitLevelRepository organisationUnitLevelRepository) {
        this.organisationUnitLevelRepository = organisationUnitLevelRepository;
    }

    @Override
    public OrganisationUnitLevel save(OrganisationUnitLevel organisationUnitLevel) {
        log.debug("Request to save OrganisationUnitLevel : {}", organisationUnitLevel);
        return organisationUnitLevelRepository.save(organisationUnitLevel);
    }

    @Override
    public Optional<OrganisationUnitLevel> partialUpdate(OrganisationUnitLevel organisationUnitLevel) {
        log.debug("Request to partially update OrganisationUnitLevel : {}", organisationUnitLevel);

        return organisationUnitLevelRepository
            .findById(organisationUnitLevel.getId())
            .map(
                existingOrganisationUnitLevel -> {
                    if (organisationUnitLevel.getUid() != null) {
                        existingOrganisationUnitLevel.setUid(organisationUnitLevel.getUid());
                    }
                    if (organisationUnitLevel.getCode() != null) {
                        existingOrganisationUnitLevel.setCode(organisationUnitLevel.getCode());
                    }
                    if (organisationUnitLevel.getName() != null) {
                        existingOrganisationUnitLevel.setName(organisationUnitLevel.getName());
                    }
                    if (organisationUnitLevel.getCreated() != null) {
                        existingOrganisationUnitLevel.setCreated(organisationUnitLevel.getCreated());
                    }
                    if (organisationUnitLevel.getLastUpdated() != null) {
                        existingOrganisationUnitLevel.setLastUpdated(organisationUnitLevel.getLastUpdated());
                    }
                    if (organisationUnitLevel.getLevel() != null) {
                        existingOrganisationUnitLevel.setLevel(organisationUnitLevel.getLevel());
                    }
                    if (organisationUnitLevel.getOfflineLevels() != null) {
                        existingOrganisationUnitLevel.setOfflineLevels(organisationUnitLevel.getOfflineLevels());
                    }

                    return existingOrganisationUnitLevel;
                }
            )
            .map(organisationUnitLevelRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganisationUnitLevel> findAll() {
        log.debug("Request to get all OrganisationUnitLevels");
        return organisationUnitLevelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganisationUnitLevel> findOne(Long id) {
        log.debug("Request to get OrganisationUnitLevel : {}", id);
        return organisationUnitLevelRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrganisationUnitLevel : {}", id);
        organisationUnitLevelRepository.deleteById(id);
    }
}
