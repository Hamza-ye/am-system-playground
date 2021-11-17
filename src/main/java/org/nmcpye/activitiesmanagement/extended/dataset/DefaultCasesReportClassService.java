package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.CasesReportClass;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository.CasesReportClassPagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultCasesReportClassService implements CasesReportClassServiceExt {
    private final CasesReportClassPagingRepository casesReportClassPagingRepository;

    public DefaultCasesReportClassService(CasesReportClassPagingRepository casesReportClassPagingRepository) {
        this.casesReportClassPagingRepository = casesReportClassPagingRepository;
    }

    @Override
    public Long addCasesReportClass(CasesReportClass casesReportClass) {
        casesReportClassPagingRepository.saveObject(casesReportClass);
        return casesReportClass.getId();
    }

    @Override
    public void updateCasesReportClass(CasesReportClass casesReportClass) {
        casesReportClassPagingRepository.update(casesReportClass);
    }

    @Override
    public void deleteCasesReportClass(CasesReportClass casesReportClass) {
        casesReportClassPagingRepository.delete(casesReportClass);
    }

    @Override
    public CasesReportClass getCasesReportClass(Long id) {
        return casesReportClassPagingRepository.get(id);
    }

    @Override
    public CasesReportClass getCasesReportClass(String uid) {
        return casesReportClassPagingRepository.getByUid(uid);
    }

    @Override
    public CasesReportClass getCasesReportClassNoAcl(String uid) {
        return casesReportClassPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<CasesReportClass> getAllCasesReportClasss() {
        return casesReportClassPagingRepository.getAll();
    }

    @Override
    public List<CasesReportClass> getCasesReportClasssByPeriodType(PeriodType periodType) {
        return null;
    }

    @Override
    public List<CasesReportClass> getAllDataRead() {
        return casesReportClassPagingRepository.getDataReadAll();
    }

    @Override
    public List<CasesReportClass> getUserDataRead(User user) {
        return casesReportClassPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<CasesReportClass> getAllDataWrite() {
        return casesReportClassPagingRepository.getDataWriteAll();
    }

    @Override
    public List<CasesReportClass> getUserDataWrite(User user) {
        return casesReportClassPagingRepository.getDataWriteAll(user);
    }
}
