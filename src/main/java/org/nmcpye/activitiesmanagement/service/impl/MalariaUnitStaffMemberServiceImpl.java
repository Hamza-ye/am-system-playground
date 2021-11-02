package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.MalariaUnitStaffMember;
import org.nmcpye.activitiesmanagement.repository.MalariaUnitStaffMemberRepository;
import org.nmcpye.activitiesmanagement.service.MalariaUnitStaffMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MalariaUnitStaffMember}.
 */
@Service
@Transactional
public class MalariaUnitStaffMemberServiceImpl implements MalariaUnitStaffMemberService {

    private final Logger log = LoggerFactory.getLogger(MalariaUnitStaffMemberServiceImpl.class);

    private final MalariaUnitStaffMemberRepository malariaUnitStaffMemberRepository;

    public MalariaUnitStaffMemberServiceImpl(MalariaUnitStaffMemberRepository malariaUnitStaffMemberRepository) {
        this.malariaUnitStaffMemberRepository = malariaUnitStaffMemberRepository;
    }

    @Override
    public MalariaUnitStaffMember save(MalariaUnitStaffMember malariaUnitStaffMember) {
        log.debug("Request to save MalariaUnitStaffMember : {}", malariaUnitStaffMember);
        return malariaUnitStaffMemberRepository.save(malariaUnitStaffMember);
    }

    @Override
    public Optional<MalariaUnitStaffMember> partialUpdate(MalariaUnitStaffMember malariaUnitStaffMember) {
        log.debug("Request to partially update MalariaUnitStaffMember : {}", malariaUnitStaffMember);

        return malariaUnitStaffMemberRepository
            .findById(malariaUnitStaffMember.getId())
            .map(
                existingMalariaUnitStaffMember -> {
                    if (malariaUnitStaffMember.getUid() != null) {
                        existingMalariaUnitStaffMember.setUid(malariaUnitStaffMember.getUid());
                    }
                    if (malariaUnitStaffMember.getCode() != null) {
                        existingMalariaUnitStaffMember.setCode(malariaUnitStaffMember.getCode());
                    }
                    if (malariaUnitStaffMember.getDescription() != null) {
                        existingMalariaUnitStaffMember.setDescription(malariaUnitStaffMember.getDescription());
                    }
                    if (malariaUnitStaffMember.getCreated() != null) {
                        existingMalariaUnitStaffMember.setCreated(malariaUnitStaffMember.getCreated());
                    }
                    if (malariaUnitStaffMember.getLastUpdated() != null) {
                        existingMalariaUnitStaffMember.setLastUpdated(malariaUnitStaffMember.getLastUpdated());
                    }
                    if (malariaUnitStaffMember.getMemberNo() != null) {
                        existingMalariaUnitStaffMember.setMemberNo(malariaUnitStaffMember.getMemberNo());
                    }
                    if (malariaUnitStaffMember.getMemberType() != null) {
                        existingMalariaUnitStaffMember.setMemberType(malariaUnitStaffMember.getMemberType());
                    }

                    return existingMalariaUnitStaffMember;
                }
            )
            .map(malariaUnitStaffMemberRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MalariaUnitStaffMember> findAll() {
        log.debug("Request to get all MalariaUnitStaffMembers");
        return malariaUnitStaffMemberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MalariaUnitStaffMember> findOne(Long id) {
        log.debug("Request to get MalariaUnitStaffMember : {}", id);
        return malariaUnitStaffMemberRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MalariaUnitStaffMember : {}", id);
        malariaUnitStaffMemberRepository.deleteById(id);
    }
}
