package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.WHMovement;
import org.nmcpye.activitiesmanagement.repository.WHMovementRepository;
import org.nmcpye.activitiesmanagement.service.WHMovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WHMovement}.
 */
@Service
@Transactional
public class WHMovementServiceImpl implements WHMovementService {

    private final Logger log = LoggerFactory.getLogger(WHMovementServiceImpl.class);

    private final WHMovementRepository wHMovementRepository;

    public WHMovementServiceImpl(WHMovementRepository wHMovementRepository) {
        this.wHMovementRepository = wHMovementRepository;
    }

    @Override
    public WHMovement save(WHMovement wHMovement) {
        log.debug("Request to save WHMovement : {}", wHMovement);
        return wHMovementRepository.save(wHMovement);
    }

    @Override
    public Optional<WHMovement> partialUpdate(WHMovement wHMovement) {
        log.debug("Request to partially update WHMovement : {}", wHMovement);

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
    public Page<WHMovement> findAll(Pageable pageable) {
        log.debug("Request to get all WHMovements");
        return wHMovementRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WHMovement> findOne(Long id) {
        log.debug("Request to get WHMovement : {}", id);
        return wHMovementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WHMovement : {}", id);
        wHMovementRepository.deleteById(id);
    }
}
