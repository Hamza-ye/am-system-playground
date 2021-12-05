package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.ChvMalariaReportVersion1;
import org.nmcpye.activitiesmanagement.repository.ChvMalariaReportVersion1Repository;
import org.nmcpye.activitiesmanagement.service.ChvMalariaReportVersion1Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChvMalariaReportVersion1}.
 */
@Service
@Transactional
public class ChvMalariaReportVersion1ServiceImpl implements ChvMalariaReportVersion1Service {

    private final Logger log = LoggerFactory.getLogger(ChvMalariaReportVersion1ServiceImpl.class);

    private final ChvMalariaReportVersion1Repository chvMalariaReportVersion1Repository;

    public ChvMalariaReportVersion1ServiceImpl(ChvMalariaReportVersion1Repository chvMalariaReportVersion1Repository) {
        this.chvMalariaReportVersion1Repository = chvMalariaReportVersion1Repository;
    }

    @Override
    public ChvMalariaReportVersion1 save(ChvMalariaReportVersion1 chvMalariaReportVersion1) {
        log.debug("Request to save ChvMalariaReportVersion1 : {}", chvMalariaReportVersion1);
        return chvMalariaReportVersion1Repository.save(chvMalariaReportVersion1);
    }

    @Override
    public Optional<ChvMalariaReportVersion1> partialUpdate(ChvMalariaReportVersion1 chvMalariaReportVersion1) {
        log.debug("Request to partially update ChvMalariaReportVersion1 : {}", chvMalariaReportVersion1);

        return chvMalariaReportVersion1Repository
            .findById(chvMalariaReportVersion1.getId())
            .map(
                existingChvMalariaReportVersion1 -> {
                    if (chvMalariaReportVersion1.getUid() != null) {
                        existingChvMalariaReportVersion1.setUid(chvMalariaReportVersion1.getUid());
                    }
                    if (chvMalariaReportVersion1.getCreated() != null) {
                        existingChvMalariaReportVersion1.setCreated(chvMalariaReportVersion1.getCreated());
                    }
                    if (chvMalariaReportVersion1.getLastUpdated() != null) {
                        existingChvMalariaReportVersion1.setLastUpdated(chvMalariaReportVersion1.getLastUpdated());
                    }
                    if (chvMalariaReportVersion1.getTested() != null) {
                        existingChvMalariaReportVersion1.setTested(chvMalariaReportVersion1.getTested());
                    }
                    if (chvMalariaReportVersion1.getPositive() != null) {
                        existingChvMalariaReportVersion1.setPositive(chvMalariaReportVersion1.getPositive());
                    }
                    if (chvMalariaReportVersion1.getDrugsGiven() != null) {
                        existingChvMalariaReportVersion1.setDrugsGiven(chvMalariaReportVersion1.getDrugsGiven());
                    }
                    if (chvMalariaReportVersion1.getSuppsGiven() != null) {
                        existingChvMalariaReportVersion1.setSuppsGiven(chvMalariaReportVersion1.getSuppsGiven());
                    }
                    if (chvMalariaReportVersion1.getRdtBalance() != null) {
                        existingChvMalariaReportVersion1.setRdtBalance(chvMalariaReportVersion1.getRdtBalance());
                    }
                    if (chvMalariaReportVersion1.getRdtReceived() != null) {
                        existingChvMalariaReportVersion1.setRdtReceived(chvMalariaReportVersion1.getRdtReceived());
                    }
                    if (chvMalariaReportVersion1.getRdtUsed() != null) {
                        existingChvMalariaReportVersion1.setRdtUsed(chvMalariaReportVersion1.getRdtUsed());
                    }
                    if (chvMalariaReportVersion1.getRdtDamagedLost() != null) {
                        existingChvMalariaReportVersion1.setRdtDamagedLost(chvMalariaReportVersion1.getRdtDamagedLost());
                    }
                    if (chvMalariaReportVersion1.getDrugsBalance() != null) {
                        existingChvMalariaReportVersion1.setDrugsBalance(chvMalariaReportVersion1.getDrugsBalance());
                    }
                    if (chvMalariaReportVersion1.getDrugsReceived() != null) {
                        existingChvMalariaReportVersion1.setDrugsReceived(chvMalariaReportVersion1.getDrugsReceived());
                    }
                    if (chvMalariaReportVersion1.getDrugsUsed() != null) {
                        existingChvMalariaReportVersion1.setDrugsUsed(chvMalariaReportVersion1.getDrugsUsed());
                    }
                    if (chvMalariaReportVersion1.getDrugsDamagedLost() != null) {
                        existingChvMalariaReportVersion1.setDrugsDamagedLost(chvMalariaReportVersion1.getDrugsDamagedLost());
                    }
                    if (chvMalariaReportVersion1.getSuppsBalance() != null) {
                        existingChvMalariaReportVersion1.setSuppsBalance(chvMalariaReportVersion1.getSuppsBalance());
                    }
                    if (chvMalariaReportVersion1.getSuppsReceived() != null) {
                        existingChvMalariaReportVersion1.setSuppsReceived(chvMalariaReportVersion1.getSuppsReceived());
                    }
                    if (chvMalariaReportVersion1.getSuppsUsed() != null) {
                        existingChvMalariaReportVersion1.setSuppsUsed(chvMalariaReportVersion1.getSuppsUsed());
                    }
                    if (chvMalariaReportVersion1.getSuppsDamagedLost() != null) {
                        existingChvMalariaReportVersion1.setSuppsDamagedLost(chvMalariaReportVersion1.getSuppsDamagedLost());
                    }
                    if (chvMalariaReportVersion1.getComment() != null) {
                        existingChvMalariaReportVersion1.setComment(chvMalariaReportVersion1.getComment());
                    }

                    return existingChvMalariaReportVersion1;
                }
            )
            .map(chvMalariaReportVersion1Repository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChvMalariaReportVersion1> findAll(Pageable pageable) {
        log.debug("Request to get all ChvMalariaReportVersion1s");
        return chvMalariaReportVersion1Repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChvMalariaReportVersion1> findOne(Long id) {
        log.debug("Request to get ChvMalariaReportVersion1 : {}", id);
        return chvMalariaReportVersion1Repository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChvMalariaReportVersion1 : {}", id);
        chvMalariaReportVersion1Repository.deleteById(id);
    }
}
