package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.repository.ChvRepository;
import org.nmcpye.activitiesmanagement.service.ChvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Chv}.
 */
@Service
@Transactional
public class ChvServiceImpl implements ChvService {

    private final Logger log = LoggerFactory.getLogger(ChvServiceImpl.class);

    private final ChvRepository cHVRepository;

    public ChvServiceImpl(ChvRepository cHVRepository) {
        this.cHVRepository = cHVRepository;
    }

    @Override
    public Chv save(Chv cHV) {
        log.debug("Request to save Chv : {}", cHV);
        return cHVRepository.save(cHV);
    }

    @Override
    public Optional<Chv> partialUpdate(Chv cHV) {
        log.debug("Request to partially update Chv : {}", cHV);

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
    public List<Chv> findAll() {
        log.debug("Request to get all CHVS");
        return cHVRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Chv> findOne(Long id) {
        log.debug("Request to get Chv : {}", id);
        return cHVRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chv : {}", id);
        cHVRepository.deleteById(id);
    }
}
