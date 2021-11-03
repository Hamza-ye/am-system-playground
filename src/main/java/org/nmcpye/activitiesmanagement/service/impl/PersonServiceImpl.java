package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.repository.PersonRepository;
import org.nmcpye.activitiesmanagement.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Person}.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person person) {
        log.debug("Request to save Person : {}", person);
        return personRepository.save(person);
    }

    @Override
    public Optional<Person> partialUpdate(Person person) {
        log.debug("Request to partially update Person : {}", person);

        return personRepository
            .findById(person.getId())
            .map(
                existingPerson -> {
                    if (person.getUid() != null) {
                        existingPerson.setUid(person.getUid());
                    }
                    if (person.getCode() != null) {
                        existingPerson.setCode(person.getCode());
                    }
                    if (person.getName() != null) {
                        existingPerson.setName(person.getName());
                    }
                    if (person.getCreated() != null) {
                        existingPerson.setCreated(person.getCreated());
                    }
                    if (person.getLastUpdated() != null) {
                        existingPerson.setLastUpdated(person.getLastUpdated());
                    }
                    if (person.getUuid() != null) {
                        existingPerson.setUuid(person.getUuid());
                    }
                    if (person.getGender() != null) {
                        existingPerson.setGender(person.getGender());
                    }
                    if (person.getMobile() != null) {
                        existingPerson.setMobile(person.getMobile());
                    }
                    if (person.getLastLogin() != null) {
                        existingPerson.setLastLogin(person.getLastLogin());
                    }
                    if (person.getLogin() != null) {
                        existingPerson.setLogin(person.getLogin());
                    }

                    existingPerson.setSelfRegistered(person.isSelfRegistered());

                    existingPerson.setDisabled(person.isDisabled());

                    return existingPerson;
                }
            )
            .map(personRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable);
    }

    public Page<Person> findAllWithEagerRelationships(Pageable pageable) {
        return personRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.deleteById(id);
    }
}
