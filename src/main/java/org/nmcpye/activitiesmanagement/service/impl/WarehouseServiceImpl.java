package org.nmcpye.activitiesmanagement.service.impl;

import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Warehouse;
import org.nmcpye.activitiesmanagement.repository.WarehouseRepository;
import org.nmcpye.activitiesmanagement.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Warehouse}.
 */
@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final Logger log = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        log.debug("Request to save Warehouse : {}", warehouse);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Optional<Warehouse> partialUpdate(Warehouse warehouse) {
        log.debug("Request to partially update Warehouse : {}", warehouse);

        return warehouseRepository
            .findById(warehouse.getId())
            .map(
                existingWarehouse -> {
                    if (warehouse.getUid() != null) {
                        existingWarehouse.setUid(warehouse.getUid());
                    }
                    if (warehouse.getCode() != null) {
                        existingWarehouse.setCode(warehouse.getCode());
                    }
                    if (warehouse.getName() != null) {
                        existingWarehouse.setName(warehouse.getName());
                    }
                    if (warehouse.getCreated() != null) {
                        existingWarehouse.setCreated(warehouse.getCreated());
                    }
                    if (warehouse.getLastUpdated() != null) {
                        existingWarehouse.setLastUpdated(warehouse.getLastUpdated());
                    }
                    if (warehouse.getWhNo() != null) {
                        existingWarehouse.setWhNo(warehouse.getWhNo());
                    }
                    if (warehouse.getInitialBalancePlan() != null) {
                        existingWarehouse.setInitialBalancePlan(warehouse.getInitialBalancePlan());
                    }
                    if (warehouse.getInitialBalanceActual() != null) {
                        existingWarehouse.setInitialBalanceActual(warehouse.getInitialBalanceActual());
                    }

                    return existingWarehouse;
                }
            )
            .map(warehouseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Warehouse> findAll(Pageable pageable) {
        log.debug("Request to get all Warehouses");
        return warehouseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Warehouse> findOne(Long id) {
        log.debug("Request to get Warehouse : {}", id);
        return warehouseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Warehouse : {}", id);
        warehouseRepository.deleteById(id);
    }
}
