package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository.MalariaCasesReportPagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultMalariaCasesReportService implements MalariaCasesReportServiceExt {
    private final MalariaCasesReportPagingRepository malariaCasesReportPagingRepository;

    public DefaultMalariaCasesReportService(MalariaCasesReportPagingRepository malariaCasesReportPagingRepository) {
        this.malariaCasesReportPagingRepository = malariaCasesReportPagingRepository;
    }

    @Override
    public long addMalariaCasesReport(MalariaCasesReport malariaCasesReport) {
        malariaCasesReportPagingRepository.saveObject(malariaCasesReport);
        return malariaCasesReport.getId();
    }

    @Override
    public void updateMalariaCasesReport(MalariaCasesReport malariaCasesReport) {
        malariaCasesReportPagingRepository.update(malariaCasesReport);
    }

    @Override
    public void deleteMalariaCasesReport(MalariaCasesReport malariaCasesReport) {
        malariaCasesReportPagingRepository.delete(malariaCasesReport);
    }

    @Override
    public MalariaCasesReport getMalariaCasesReport(long id) {
        return malariaCasesReportPagingRepository.get(id);
    }

    @Override
    public MalariaCasesReport getMalariaCasesReport(String uid) {
        return malariaCasesReportPagingRepository.getByUid(uid);
    }

    @Override
    public MalariaCasesReport getMalariaCasesReportNoAcl(String uid) {
        return malariaCasesReportPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<MalariaCasesReport> getAllMalariaCasesReports() {
        return malariaCasesReportPagingRepository.getAll();
    }

    @Override
    public List<MalariaCasesReport> getMalariaCasesReportsByPeriodType(PeriodType periodType) {
        return null;
    }

    @Override
    public List<MalariaCasesReport> getAllDataRead() {
        return malariaCasesReportPagingRepository.getDataReadAll();
    }

    @Override
    public List<MalariaCasesReport> getUserDataRead(User user) {
        return malariaCasesReportPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<MalariaCasesReport> getAllDataWrite() {
        return malariaCasesReportPagingRepository.getDataWriteAll();
    }

    @Override
    public List<MalariaCasesReport> getUserDataWrite(User user) {
        return malariaCasesReportPagingRepository.getDataWriteAll(user);
    }
}
