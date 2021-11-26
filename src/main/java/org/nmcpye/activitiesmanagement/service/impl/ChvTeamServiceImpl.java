package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.nmcpye.activitiesmanagement.repository.ChvTeamRepository;
import org.nmcpye.activitiesmanagement.service.ChvTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ChvTeam}.
 */
@Service
@Transactional
public class ChvTeamServiceImpl implements ChvTeamService {

    private final Logger log = LoggerFactory.getLogger(ChvTeamServiceImpl.class);

    private final ChvTeamRepository chvTeamRepository;

    public ChvTeamServiceImpl(ChvTeamRepository chvTeamRepository) {
        this.chvTeamRepository = chvTeamRepository;
    }

    @Override
    public ChvTeam save(ChvTeam chvTeam) {
        log.debug("Request to save ChvTeam : {}", chvTeam);
        return chvTeamRepository.save(chvTeam);
    }

    @Override
    public Optional<ChvTeam> partialUpdate(ChvTeam chvTeam) {
        log.debug("Request to partially update ChvTeam : {}", chvTeam);

        return chvTeamRepository
            .findById(chvTeam.getId())
            .map(
                existingChvTeam -> {
                    if (chvTeam.getUid() != null) {
                        existingChvTeam.setUid(chvTeam.getUid());
                    }
                    if (chvTeam.getCode() != null) {
                        existingChvTeam.setCode(chvTeam.getCode());
                    }
                    if (chvTeam.getName() != null) {
                        existingChvTeam.setName(chvTeam.getName());
                    }
                    if (chvTeam.getDescription() != null) {
                        existingChvTeam.setDescription(chvTeam.getDescription());
                    }
                    if (chvTeam.getCreated() != null) {
                        existingChvTeam.setCreated(chvTeam.getCreated());
                    }
                    if (chvTeam.getLastUpdated() != null) {
                        existingChvTeam.setLastUpdated(chvTeam.getLastUpdated());
                    }
                    if (chvTeam.getTeamNo() != null) {
                        existingChvTeam.setTeamNo(chvTeam.getTeamNo());
                    }
                    if (chvTeam.getTeamType() != null) {
                        existingChvTeam.setTeamType(chvTeam.getTeamType());
                    }

                    return existingChvTeam;
                }
            )
            .map(chvTeamRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChvTeam> findAll() {
        log.debug("Request to get all ChvTeams");
        return chvTeamRepository.findAllWithEagerRelationships();
    }

    public Page<ChvTeam> findAllWithEagerRelationships(Pageable pageable) {
        return chvTeamRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChvTeam> findOne(Long id) {
        log.debug("Request to get ChvTeam : {}", id);
        return chvTeamRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChvTeam : {}", id);
        chvTeamRepository.deleteById(id);
    }
}
