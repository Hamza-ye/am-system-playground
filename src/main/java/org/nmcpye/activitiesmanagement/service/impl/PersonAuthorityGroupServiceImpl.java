package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.PersonAuthorityGroup;
import org.nmcpye.activitiesmanagement.repository.PersonAuthorityGroupRepository;
import org.nmcpye.activitiesmanagement.service.PersonAuthorityGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonAuthorityGroup}.
 */
@Service
@Transactional
public class PersonAuthorityGroupServiceImpl implements PersonAuthorityGroupService {

    private final Logger log = LoggerFactory.getLogger(PersonAuthorityGroupServiceImpl.class);

    private final PersonAuthorityGroupRepository personAuthorityGroupRepository;

    public PersonAuthorityGroupServiceImpl(PersonAuthorityGroupRepository personAuthorityGroupRepository) {
        this.personAuthorityGroupRepository = personAuthorityGroupRepository;
    }

    @Override
    public PersonAuthorityGroup save(PersonAuthorityGroup personAuthorityGroup) {
        log.debug("Request to save PersonAuthorityGroup : {}", personAuthorityGroup);
        return personAuthorityGroupRepository.save(personAuthorityGroup);
    }

    @Override
    public Optional<PersonAuthorityGroup> partialUpdate(PersonAuthorityGroup personAuthorityGroup) {
        log.debug("Request to partially update PersonAuthorityGroup : {}", personAuthorityGroup);

        return personAuthorityGroupRepository
            .findById(personAuthorityGroup.getId())
            .map(
                existingPersonAuthorityGroup -> {
                    if (personAuthorityGroup.getUid() != null) {
                        existingPersonAuthorityGroup.setUid(personAuthorityGroup.getUid());
                    }
                    if (personAuthorityGroup.getCode() != null) {
                        existingPersonAuthorityGroup.setCode(personAuthorityGroup.getCode());
                    }
                    if (personAuthorityGroup.getName() != null) {
                        existingPersonAuthorityGroup.setName(personAuthorityGroup.getName());
                    }
                    if (personAuthorityGroup.getDescription() != null) {
                        existingPersonAuthorityGroup.setDescription(personAuthorityGroup.getDescription());
                    }
                    if (personAuthorityGroup.getCreated() != null) {
                        existingPersonAuthorityGroup.setCreated(personAuthorityGroup.getCreated());
                    }
                    if (personAuthorityGroup.getLastUpdated() != null) {
                        existingPersonAuthorityGroup.setLastUpdated(personAuthorityGroup.getLastUpdated());
                    }

                    return existingPersonAuthorityGroup;
                }
            )
            .map(personAuthorityGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonAuthorityGroup> findAll() {
        log.debug("Request to get all PersonAuthorityGroups");
        return personAuthorityGroupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonAuthorityGroup> findOne(Long id) {
        log.debug("Request to get PersonAuthorityGroup : {}", id);
        return personAuthorityGroupRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PersonAuthorityGroup : {}", id);
        personAuthorityGroupRepository.deleteById(id);
    }
}
