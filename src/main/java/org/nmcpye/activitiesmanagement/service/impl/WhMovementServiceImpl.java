package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.WhMovement;
import org.nmcpye.activitiesmanagement.repository.WhMovementRepository;
import org.nmcpye.activitiesmanagement.service.WhMovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WhMovement}.
 */
@Service
@Transactional
public class WhMovementServiceImpl implements WhMovementService {

    private final Logger log = LoggerFactory.getLogger(WhMovementServiceImpl.class);

    private final WhMovementRepository wHMovementRepository;

    public WhMovementServiceImpl(WhMovementRepository wHMovementRepository) {
        this.wHMovementRepository = wHMovementRepository;
    }

    @Override
    public WhMovement save(WhMovement wHMovement) {
        log.debug("Request to save WhMovement : {}", wHMovement);
        return wHMovementRepository.save(wHMovement);
    }

    @Override
    public Optional<WhMovement> partialUpdate(WhMovement wHMovement) {
        log.debug("Request to partially update WhMovement : {}", wHMovement);

        return wHMovementRepository
            .findById(wHMovement.getId())
            .map(
                existingWHMovement -> {
                    if (wHMovement.getMovementType() != null) {
                        existingWHMovement.setMovementType(wHMovement.getMovementType());
                    }
                    if (wHMovement.getQuantity() != null) {
                        existingWHMovement.setQuantity(wHMovement.getQuantity());
                    }
                    if (wHMovement.getReconciliationSource() != null) {
                        existingWHMovement.setReconciliationSource(wHMovement.getReconciliationSource());
                    }
                    if (wHMovement.getReconciliationDestination() != null) {
                        existingWHMovement.setReconciliationDestination(wHMovement.getReconciliationDestination());
                    }
                    if (wHMovement.getConfirmedByOtherSide() != null) {
                        existingWHMovement.setConfirmedByOtherSide(wHMovement.getConfirmedByOtherSide());
                    }
                    if (wHMovement.getComment() != null) {
                        existingWHMovement.setComment(wHMovement.getComment());
                    }

                    return existingWHMovement;
                }
            )
            .map(wHMovementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WhMovement> findAll(Pageable pageable) {
        log.debug("Request to get all WHMovements");
        return wHMovementRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WhMovement> findOne(Long id) {
        log.debug("Request to get WhMovement : {}", id);
        return wHMovementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WhMovement : {}", id);
        wHMovementRepository.deleteById(id);
    }
}
