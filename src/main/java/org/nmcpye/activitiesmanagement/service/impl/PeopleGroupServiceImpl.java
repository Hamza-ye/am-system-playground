package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.PeopleGroup;
import org.nmcpye.activitiesmanagement.repository.PeopleGroupRepository;
import org.nmcpye.activitiesmanagement.service.PeopleGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PeopleGroup}.
 */
@Service
@Transactional
public class PeopleGroupServiceImpl implements PeopleGroupService {

    private final Logger log = LoggerFactory.getLogger(PeopleGroupServiceImpl.class);

    private final PeopleGroupRepository peopleGroupRepository;

    public PeopleGroupServiceImpl(PeopleGroupRepository peopleGroupRepository) {
        this.peopleGroupRepository = peopleGroupRepository;
    }

    @Override
    public PeopleGroup save(PeopleGroup peopleGroup) {
        log.debug("Request to save PeopleGroup : {}", peopleGroup);
        return peopleGroupRepository.save(peopleGroup);
    }

    @Override
    public Optional<PeopleGroup> partialUpdate(PeopleGroup peopleGroup) {
        log.debug("Request to partially update PeopleGroup : {}", peopleGroup);

        return peopleGroupRepository
            .findById(peopleGroup.getId())
            .map(
                existingPeopleGroup -> {
                    if (peopleGroup.getUid() != null) {
                        existingPeopleGroup.setUid(peopleGroup.getUid());
                    }
                    if (peopleGroup.getCode() != null) {
                        existingPeopleGroup.setCode(peopleGroup.getCode());
                    }
                    if (peopleGroup.getName() != null) {
                        existingPeopleGroup.setName(peopleGroup.getName());
                    }
                    if (peopleGroup.getCreated() != null) {
                        existingPeopleGroup.setCreated(peopleGroup.getCreated());
                    }
                    if (peopleGroup.getLastUpdated() != null) {
                        existingPeopleGroup.setLastUpdated(peopleGroup.getLastUpdated());
                    }
                    if (peopleGroup.getUuid() != null) {
                        existingPeopleGroup.setUuid(peopleGroup.getUuid());
                    }

                    return existingPeopleGroup;
                }
            )
            .map(peopleGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeopleGroup> findAll() {
        log.debug("Request to get all PeopleGroups");
        return peopleGroupRepository.findAllWithEagerRelationships();
    }

    public Page<PeopleGroup> findAllWithEagerRelationships(Pageable pageable) {
        return peopleGroupRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PeopleGroup> findOne(Long id) {
        log.debug("Request to get PeopleGroup : {}", id);
        return peopleGroupRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PeopleGroup : {}", id);
        peopleGroupRepository.deleteById(id);
    }
}
