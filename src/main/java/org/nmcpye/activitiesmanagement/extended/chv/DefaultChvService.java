package org.nmcpye.activitiesmanagement.extended.chv;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.extended.chv.pagingrepository.ChvPagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17-11-2021.
 */
@Service
public class DefaultChvService implements ChvServiceExt {
    private final ChvPagingRepository chvPagingRepository;

    public DefaultChvService(ChvPagingRepository chvPagingRepository) {
        this.chvPagingRepository = chvPagingRepository;
    }

    @Override
    public Long addCHV(Chv chv) {
        chvPagingRepository.saveObject(chv);
        return chv.getId();
    }

    @Override
    public void updateCHV(Chv chv) {
        chvPagingRepository.update(chv);
    }

    @Override
    public void deleteCHV(Chv chv) {
        chvPagingRepository.delete(chv);
    }

    @Override
    public Chv getCHV(Long id) {
        return chvPagingRepository.get(id);
    }

    @Override
    public Chv getCHV(String uid) {
        return chvPagingRepository.getByUid(uid);
    }

    @Override
    public Chv getCHVNoAcl(String uid) {
        return chvPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<Chv> getAllCHVs() {
        return chvPagingRepository.getAll();
    }

    @Override
    public List<Chv> getAllDataRead() {
        return chvPagingRepository.getDataReadAll();
    }

    @Override
    public List<Chv> getUserDataRead(User user) {
        return chvPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<Chv> getAllDataWrite() {
        return chvPagingRepository.getDataWriteAll();
    }

    @Override
    public List<Chv> getUserDataWrite(User user) {
        return chvPagingRepository.getDataWriteAll(user);
    }
}
