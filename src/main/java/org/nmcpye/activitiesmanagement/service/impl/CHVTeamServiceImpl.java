package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CHVTeam;
import org.nmcpye.activitiesmanagement.repository.CHVTeamRepository;
import org.nmcpye.activitiesmanagement.service.CHVTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CHVTeam}.
 */
@Service
@Transactional
public class CHVTeamServiceImpl implements CHVTeamService {

    private final Logger log = LoggerFactory.getLogger(CHVTeamServiceImpl.class);

    private final CHVTeamRepository cHVTeamRepository;

    public CHVTeamServiceImpl(CHVTeamRepository cHVTeamRepository) {
        this.cHVTeamRepository = cHVTeamRepository;
    }

    @Override
    public CHVTeam save(CHVTeam cHVTeam) {
        log.debug("Request to save CHVTeam : {}", cHVTeam);
        return cHVTeamRepository.save(cHVTeam);
    }

    @Override
    public Optional<CHVTeam> partialUpdate(CHVTeam cHVTeam) {
        log.debug("Request to partially update CHVTeam : {}", cHVTeam);

        return cHVTeamRepository
            .findById(cHVTeam.getId())
            .map(
                existingCHVTeam -> {
                    if (cHVTeam.getUid() != null) {
                        existingCHVTeam.setUid(cHVTeam.getUid());
                    }
                    if (cHVTeam.getCode() != null) {
                        existingCHVTeam.setCode(cHVTeam.getCode());
                    }
                    if (cHVTeam.getName() != null) {
                        existingCHVTeam.setName(cHVTeam.getName());
                    }
                    if (cHVTeam.getDescription() != null) {
                        existingCHVTeam.setDescription(cHVTeam.getDescription());
                    }
                    if (cHVTeam.getCreated() != null) {
                        existingCHVTeam.setCreated(cHVTeam.getCreated());
                    }
                    if (cHVTeam.getLastUpdated() != null) {
                        existingCHVTeam.setLastUpdated(cHVTeam.getLastUpdated());
                    }
                    if (cHVTeam.getTeamNo() != null) {
                        existingCHVTeam.setTeamNo(cHVTeam.getTeamNo());
                    }
                    if (cHVTeam.getTeamType() != null) {
                        existingCHVTeam.setTeamType(cHVTeam.getTeamType());
                    }

                    return existingCHVTeam;
                }
            )
            .map(cHVTeamRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CHVTeam> findAll() {
        log.debug("Request to get all CHVTeams");
        return cHVTeamRepository.findAllWithEagerRelationships();
    }

    public Page<CHVTeam> findAllWithEagerRelationships(Pageable pageable) {
        return cHVTeamRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CHVTeam> findOne(Long id) {
        log.debug("Request to get CHVTeam : {}", id);
        return cHVTeamRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CHVTeam : {}", id);
        cHVTeamRepository.deleteById(id);
    }
}
