package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CHV;
import org.nmcpye.activitiesmanagement.repository.CHVRepository;
import org.nmcpye.activitiesmanagement.service.CHVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CHV}.
 */
@Service
@Transactional
public class CHVServiceImpl implements CHVService {

    private final Logger log = LoggerFactory.getLogger(CHVServiceImpl.class);

    private final CHVRepository cHVRepository;

    public CHVServiceImpl(CHVRepository cHVRepository) {
        this.cHVRepository = cHVRepository;
    }

    @Override
    public CHV save(CHV cHV) {
        log.debug("Request to save CHV : {}", cHV);
        return cHVRepository.save(cHV);
    }

    @Override
    public Optional<CHV> partialUpdate(CHV cHV) {
        log.debug("Request to partially update CHV : {}", cHV);

        return cHVRepository
            .findById(cHV.getId())
            .map(
                existingCHV -> {
                    if (cHV.getUid() != null) {
                        existingCHV.setUid(cHV.getUid());
                    }
                    if (cHV.getCode() != null) {
                        existingCHV.setCode(cHV.getCode());
                    }
                    if (cHV.getDescription() != null) {
                        existingCHV.setDescription(cHV.getDescription());
                    }
                    if (cHV.getCreated() != null) {
                        existingCHV.setCreated(cHV.getCreated());
                    }
                    if (cHV.getLastUpdated() != null) {
                        existingCHV.setLastUpdated(cHV.getLastUpdated());
                    }
                    if (cHV.getMobile() != null) {
                        existingCHV.setMobile(cHV.getMobile());
                    }

                    return existingCHV;
                }
            )
            .map(cHVRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CHV> findAll() {
        log.debug("Request to get all CHVS");
        return cHVRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CHV> findOne(Long id) {
        log.debug("Request to get CHV : {}", id);
        return cHVRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CHV : {}", id);
        cHVRepository.deleteById(id);
    }
}
