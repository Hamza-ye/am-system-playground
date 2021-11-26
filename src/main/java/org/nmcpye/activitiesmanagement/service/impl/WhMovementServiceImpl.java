package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.WhMovement;
import org.nmcpye.activitiesmanagement.repository.WhMovementRepository;
import org.nmcpye.activitiesmanagement.service.WhMovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link WhMovement}.
 */
@Service
@Transactional
public class WhMovementServiceImpl implements WhMovementService {

    private final Logger log = LoggerFactory.getLogger(WhMovementServiceImpl.class);

    private final WhMovementRepository whMovementRepository;

    public WhMovementServiceImpl(WhMovementRepository whMovementRepository) {
        this.whMovementRepository = whMovementRepository;
    }

    @Override
    public WhMovement save(WhMovement whMovement) {
        log.debug("Request to save WhMovement : {}", whMovement);
        return whMovementRepository.save(whMovement);
    }

    @Override
    public Optional<WhMovement> partialUpdate(WhMovement whMovement) {
        log.debug("Request to partially update WhMovement : {}", whMovement);

        return whMovementRepository
            .findById(whMovement.getId())
            .map(
                existingWhMovement -> {
                    if (whMovement.getMovementType() != null) {
                        existingWhMovement.setMovementType(whMovement.getMovementType());
                    }
                    if (whMovement.getQuantity() != null) {
                        existingWhMovement.setQuantity(whMovement.getQuantity());
                    }
                    if (whMovement.getReconciliationSource() != null) {
                        existingWhMovement.setReconciliationSource(whMovement.getReconciliationSource());
                    }
                    if (whMovement.getReconciliationDestination() != null) {
                        existingWhMovement.setReconciliationDestination(whMovement.getReconciliationDestination());
                    }
                    if (whMovement.getConfirmedByOtherSide() != null) {
                        existingWhMovement.setConfirmedByOtherSide(whMovement.getConfirmedByOtherSide());
                    }
                    if (whMovement.getComment() != null) {
                        existingWhMovement.setComment(whMovement.getComment());
                    }

                    return existingWhMovement;
                }
            )
            .map(whMovementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WhMovement> findAll(Pageable pageable) {
        log.debug("Request to get all WhMovements");
        return whMovementRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WhMovement> findOne(Long id) {
        log.debug("Request to get WhMovement : {}", id);
        return whMovementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WhMovement : {}", id);
        whMovementRepository.deleteById(id);
    }
}
