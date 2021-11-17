package org.nmcpye.activitiesmanagement.extended.chv;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.extended.chv.pagingrepository.ChvPagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultChvService implements ChvServiceExt {
    private final ChvPagingRepository chvPagingRepository;

    public DefaultChvService(ChvPagingRepository chvPagingRepository) {
        this.chvPagingRepository = chvPagingRepository;
    }

    @Override
    public long addCHV(CHV chv) {
        chvPagingRepository.saveObject(chv);
        return chv.getId();
    }

    @Override
    public void updateCHV(CHV chv) {
        chvPagingRepository.update(chv);
    }

    @Override
    public void deleteCHV(CHV chv) {
        chvPagingRepository.delete(chv);
    }

    @Override
    public CHV getCHV(long id) {
        return chvPagingRepository.get(id);
    }

    @Override
    public CHV getCHV(String uid) {
        return chvPagingRepository.getByUid(uid);
    }

    @Override
    public CHV getCHVNoAcl(String uid) {
        return chvPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<CHV> getAllCHVs() {
        return chvPagingRepository.getAll();
    }

    @Override
    public List<CHV> getAllDataRead() {
        return chvPagingRepository.getDataReadAll();
    }

    @Override
    public List<CHV> getUserDataRead(User user) {
        return chvPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<CHV> getAllDataWrite() {
        return chvPagingRepository.getDataWriteAll();
    }

    @Override
    public List<CHV> getUserDataWrite(User user) {
        return chvPagingRepository.getDataWriteAll(user);
    }
}
