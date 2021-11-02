package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.CHVMalariaReportVersion1;
import org.nmcpye.activitiesmanagement.repository.CHVMalariaReportVersion1Repository;
import org.nmcpye.activitiesmanagement.service.CHVMalariaReportVersion1Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CHVMalariaReportVersion1}.
 */
@Service
@Transactional
public class CHVMalariaReportVersion1ServiceImpl implements CHVMalariaReportVersion1Service {

    private final Logger log = LoggerFactory.getLogger(CHVMalariaReportVersion1ServiceImpl.class);

    private final CHVMalariaReportVersion1Repository cHVMalariaReportVersion1Repository;

    public CHVMalariaReportVersion1ServiceImpl(CHVMalariaReportVersion1Repository cHVMalariaReportVersion1Repository) {
        this.cHVMalariaReportVersion1Repository = cHVMalariaReportVersion1Repository;
    }

    @Override
    public CHVMalariaReportVersion1 save(CHVMalariaReportVersion1 cHVMalariaReportVersion1) {
        log.debug("Request to save CHVMalariaReportVersion1 : {}", cHVMalariaReportVersion1);
        return cHVMalariaReportVersion1Repository.save(cHVMalariaReportVersion1);
    }

    @Override
    public Optional<CHVMalariaReportVersion1> partialUpdate(CHVMalariaReportVersion1 cHVMalariaReportVersion1) {
        log.debug("Request to partially update CHVMalariaReportVersion1 : {}", cHVMalariaReportVersion1);

        return cHVMalariaReportVersion1Repository
            .findById(cHVMalariaReportVersion1.getId())
            .map(
                existingCHVMalariaReportVersion1 -> {
                    if (cHVMalariaReportVersion1.getUid() != null) {
                        existingCHVMalariaReportVersion1.setUid(cHVMalariaReportVersion1.getUid());
                    }
                    if (cHVMalariaReportVersion1.getCreated() != null) {
                        existingCHVMalariaReportVersion1.setCreated(cHVMalariaReportVersion1.getCreated());
                    }
                    if (cHVMalariaReportVersion1.getLastUpdated() != null) {
                        existingCHVMalariaReportVersion1.setLastUpdated(cHVMalariaReportVersion1.getLastUpdated());
                    }
                    if (cHVMalariaReportVersion1.getTested() != null) {
                        existingCHVMalariaReportVersion1.setTested(cHVMalariaReportVersion1.getTested());
                    }
                    if (cHVMalariaReportVersion1.getPositive() != null) {
                        existingCHVMalariaReportVersion1.setPositive(cHVMalariaReportVersion1.getPositive());
                    }
                    if (cHVMalariaReportVersion1.getDrugsGiven() != null) {
                        existingCHVMalariaReportVersion1.setDrugsGiven(cHVMalariaReportVersion1.getDrugsGiven());
                    }
                    if (cHVMalariaReportVersion1.getSuppsGiven() != null) {
                        existingCHVMalariaReportVersion1.setSuppsGiven(cHVMalariaReportVersion1.getSuppsGiven());
                    }
                    if (cHVMalariaReportVersion1.getRdtBalance() != null) {
                        existingCHVMalariaReportVersion1.setRdtBalance(cHVMalariaReportVersion1.getRdtBalance());
                    }
                    if (cHVMalariaReportVersion1.getRdtReceived() != null) {
                        existingCHVMalariaReportVersion1.setRdtReceived(cHVMalariaReportVersion1.getRdtReceived());
                    }
                    if (cHVMalariaReportVersion1.getRdtUsed() != null) {
                        existingCHVMalariaReportVersion1.setRdtUsed(cHVMalariaReportVersion1.getRdtUsed());
                    }
                    if (cHVMalariaReportVersion1.getRdtDamagedLost() != null) {
                        existingCHVMalariaReportVersion1.setRdtDamagedLost(cHVMalariaReportVersion1.getRdtDamagedLost());
                    }
                    if (cHVMalariaReportVersion1.getDrugsBalance() != null) {
                        existingCHVMalariaReportVersion1.setDrugsBalance(cHVMalariaReportVersion1.getDrugsBalance());
                    }
                    if (cHVMalariaReportVersion1.getDrugsReceived() != null) {
                        existingCHVMalariaReportVersion1.setDrugsReceived(cHVMalariaReportVersion1.getDrugsReceived());
                    }
                    if (cHVMalariaReportVersion1.getDrugsUsed() != null) {
                        existingCHVMalariaReportVersion1.setDrugsUsed(cHVMalariaReportVersion1.getDrugsUsed());
                    }
                    if (cHVMalariaReportVersion1.getDrugsDamagedLost() != null) {
                        existingCHVMalariaReportVersion1.setDrugsDamagedLost(cHVMalariaReportVersion1.getDrugsDamagedLost());
                    }
                    if (cHVMalariaReportVersion1.getSuppsBalance() != null) {
                        existingCHVMalariaReportVersion1.setSuppsBalance(cHVMalariaReportVersion1.getSuppsBalance());
                    }
                    if (cHVMalariaReportVersion1.getSuppsReceived() != null) {
                        existingCHVMalariaReportVersion1.setSuppsReceived(cHVMalariaReportVersion1.getSuppsReceived());
                    }
                    if (cHVMalariaReportVersion1.getSuppsUsed() != null) {
                        existingCHVMalariaReportVersion1.setSuppsUsed(cHVMalariaReportVersion1.getSuppsUsed());
                    }
                    if (cHVMalariaReportVersion1.getSuppsDamagedLost() != null) {
                        existingCHVMalariaReportVersion1.setSuppsDamagedLost(cHVMalariaReportVersion1.getSuppsDamagedLost());
                    }
                    if (cHVMalariaReportVersion1.getComment() != null) {
                        existingCHVMalariaReportVersion1.setComment(cHVMalariaReportVersion1.getComment());
                    }

                    return existingCHVMalariaReportVersion1;
                }
            )
            .map(cHVMalariaReportVersion1Repository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CHVMalariaReportVersion1> findAll() {
        log.debug("Request to get all CHVMalariaReportVersion1s");
        return cHVMalariaReportVersion1Repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CHVMalariaReportVersion1> findOne(Long id) {
        log.debug("Request to get CHVMalariaReportVersion1 : {}", id);
        return cHVMalariaReportVersion1Repository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CHVMalariaReportVersion1 : {}", id);
        cHVMalariaReportVersion1Repository.deleteById(id);
    }
}
