package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.ChvTeam;
import org.nmcpye.activitiesmanagement.repository.ChvTeamRepository;
import org.nmcpye.activitiesmanagement.service.ChvTeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChvTeam}.
 */
@Service
@Transactional
public class ChvTeamServiceImpl implements ChvTeamService {

    private final Logger log = LoggerFactory.getLogger(ChvTeamServiceImpl.class);

    private final ChvTeamRepository cHVTeamRepository;

    public ChvTeamServiceImpl(ChvTeamRepository cHVTeamRepository) {
        this.cHVTeamRepository = cHVTeamRepository;
    }

    @Override
    public ChvTeam save(ChvTeam cHVTeam) {
        log.debug("Request to save ChvTeam : {}", cHVTeam);
        return cHVTeamRepository.save(cHVTeam);
    }

    @Override
    public Optional<ChvTeam> partialUpdate(ChvTeam cHVTeam) {
        log.debug("Request to partially update ChvTeam : {}", cHVTeam);

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
    public List<ChvTeam> findAll() {
        log.debug("Request to get all CHVTeams");
        return cHVTeamRepository.findAllWithEagerRelationships();
    }

    public Page<ChvTeam> findAllWithEagerRelationships(Pageable pageable) {
        return cHVTeamRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChvTeam> findOne(Long id) {
        log.debug("Request to get ChvTeam : {}", id);
        return cHVTeamRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChvTeam : {}", id);
        cHVTeamRepository.deleteById(id);
    }
}
