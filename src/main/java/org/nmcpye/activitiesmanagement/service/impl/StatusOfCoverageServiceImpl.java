package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.StatusOfCoverage;
import org.nmcpye.activitiesmanagement.repository.StatusOfCoverageRepository;
import org.nmcpye.activitiesmanagement.service.StatusOfCoverageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StatusOfCoverage}.
 */
@Service
@Transactional
public class StatusOfCoverageServiceImpl implements StatusOfCoverageService {

    private final Logger log = LoggerFactory.getLogger(StatusOfCoverageServiceImpl.class);

    private final StatusOfCoverageRepository statusOfCoverageRepository;

    public StatusOfCoverageServiceImpl(StatusOfCoverageRepository statusOfCoverageRepository) {
        this.statusOfCoverageRepository = statusOfCoverageRepository;
    }

    @Override
    public StatusOfCoverage save(StatusOfCoverage statusOfCoverage) {
        log.debug("Request to save StatusOfCoverage : {}", statusOfCoverage);
        return statusOfCoverageRepository.save(statusOfCoverage);
    }

    @Override
    public Optional<StatusOfCoverage> partialUpdate(StatusOfCoverage statusOfCoverage) {
        log.debug("Request to partially update StatusOfCoverage : {}", statusOfCoverage);

        return statusOfCoverageRepository
            .findById(statusOfCoverage.getId())
            .map(
                existingStatusOfCoverage -> {
                    if (statusOfCoverage.getCode() != null) {
                        existingStatusOfCoverage.setCode(statusOfCoverage.getCode());
                    }
                    if (statusOfCoverage.getStatus() != null) {
                        existingStatusOfCoverage.setStatus(statusOfCoverage.getStatus());
                    }

                    return existingStatusOfCoverage;
                }
            )
            .map(statusOfCoverageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusOfCoverage> findAll() {
        log.debug("Request to get all StatusOfCoverages");
        return statusOfCoverageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusOfCoverage> findOne(Long id) {
        log.debug("Request to get StatusOfCoverage : {}", id);
        return statusOfCoverageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StatusOfCoverage : {}", id);
        statusOfCoverageRepository.deleteById(id);
    }
}
