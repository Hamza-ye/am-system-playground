package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.Fingerprint;
import org.nmcpye.activitiesmanagement.repository.FingerprintRepository;
import org.nmcpye.activitiesmanagement.service.FingerprintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fingerprint}.
 */
@Service
@Transactional
public class FingerprintServiceImpl implements FingerprintService {

    private final Logger log = LoggerFactory.getLogger(FingerprintServiceImpl.class);

    private final FingerprintRepository fingerprintRepository;

    public FingerprintServiceImpl(FingerprintRepository fingerprintRepository) {
        this.fingerprintRepository = fingerprintRepository;
    }

    @Override
    public Fingerprint save(Fingerprint fingerprint) {
        log.debug("Request to save Fingerprint : {}", fingerprint);
        return fingerprintRepository.save(fingerprint);
    }

    @Override
    public Optional<Fingerprint> partialUpdate(Fingerprint fingerprint) {
        log.debug("Request to partially update Fingerprint : {}", fingerprint);

        return fingerprintRepository
            .findById(fingerprint.getId())
            .map(
                existingFingerprint -> {
                    if (fingerprint.getUid() != null) {
                        existingFingerprint.setUid(fingerprint.getUid());
                    }
                    if (fingerprint.getDescription() != null) {
                        existingFingerprint.setDescription(fingerprint.getDescription());
                    }
                    if (fingerprint.getCreated() != null) {
                        existingFingerprint.setCreated(fingerprint.getCreated());
                    }
                    if (fingerprint.getLastUpdated() != null) {
                        existingFingerprint.setLastUpdated(fingerprint.getLastUpdated());
                    }
                    if (fingerprint.getFingerprintUrl() != null) {
                        existingFingerprint.setFingerprintUrl(fingerprint.getFingerprintUrl());
                    }
                    if (fingerprint.getFingerprintOwner() != null) {
                        existingFingerprint.setFingerprintOwner(fingerprint.getFingerprintOwner());
                    }

                    return existingFingerprint;
                }
            )
            .map(fingerprintRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fingerprint> findAll() {
        log.debug("Request to get all Fingerprints");
        return fingerprintRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fingerprint> findOne(Long id) {
        log.debug("Request to get Fingerprint : {}", id);
        return fingerprintRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fingerprint : {}", id);
        fingerprintRepository.deleteById(id);
    }
}
