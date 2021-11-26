package org.nmcpye.activitiesmanagement.service.impl;

import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.repository.ChvRepository;
import org.nmcpye.activitiesmanagement.service.ChvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Chv}.
 */
@Service
@Transactional
public class ChvServiceImpl implements ChvService {

    private final Logger log = LoggerFactory.getLogger(ChvServiceImpl.class);

    private final ChvRepository chvRepository;

    public ChvServiceImpl(ChvRepository chvRepository) {
        this.chvRepository = chvRepository;
    }

    @Override
    public Chv save(Chv chv) {
        log.debug("Request to save Chv : {}", chv);
        return chvRepository.save(chv);
    }

    @Override
    public Optional<Chv> partialUpdate(Chv chv) {
        log.debug("Request to partially update Chv : {}", chv);

        return chvRepository
            .findById(chv.getId())
            .map(
                existingChv -> {
                    if (chv.getUid() != null) {
                        existingChv.setUid(chv.getUid());
                    }
                    if (chv.getCode() != null) {
                        existingChv.setCode(chv.getCode());
                    }
                    if (chv.getDescription() != null) {
                        existingChv.setDescription(chv.getDescription());
                    }
                    if (chv.getCreated() != null) {
                        existingChv.setCreated(chv.getCreated());
                    }
                    if (chv.getLastUpdated() != null) {
                        existingChv.setLastUpdated(chv.getLastUpdated());
                    }
                    if (chv.getMobile() != null) {
                        existingChv.setMobile(chv.getMobile());
                    }

                    return existingChv;
                }
            )
            .map(chvRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Chv> findAll() {
        log.debug("Request to get all Chvs");
        return chvRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Chv> findOne(Long id) {
        log.debug("Request to get Chv : {}", id);
        return chvRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chv : {}", id);
        chvRepository.deleteById(id);
    }
}
