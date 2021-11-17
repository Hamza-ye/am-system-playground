package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.dataset.pagingrepository.DengueCasesReportPagingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Hamza on 17/11/2021.
 */
@Service
public class DefaultDengueCasesReportService implements DengueCasesReportServiceExt {
    private final DengueCasesReportPagingRepository dengueCasesReportPagingRepository;

    public DefaultDengueCasesReportService(DengueCasesReportPagingRepository dengueCasesReportPagingRepository) {
        this.dengueCasesReportPagingRepository = dengueCasesReportPagingRepository;
    }

    @Override
    public long addDengueCasesReport(DengueCasesReport dengueCasesReport) {
        dengueCasesReportPagingRepository.saveObject(dengueCasesReport);
        return dengueCasesReport.getId();
    }

    @Override
    public void updateDengueCasesReport(DengueCasesReport dengueCasesReport) {
        dengueCasesReportPagingRepository.update(dengueCasesReport);
    }

    @Override
    public void deleteDengueCasesReport(DengueCasesReport dengueCasesReport) {
        dengueCasesReportPagingRepository.delete(dengueCasesReport);
    }

    @Override
    public DengueCasesReport getDengueCasesReport(long id) {
        return dengueCasesReportPagingRepository.get(id);
    }

    @Override
    public DengueCasesReport getDengueCasesReport(String uid) {
        return dengueCasesReportPagingRepository.getByUid(uid);
    }

    @Override
    public DengueCasesReport getDengueCasesReportNoAcl(String uid) {
        return dengueCasesReportPagingRepository.getByUidNoAcl(uid);
    }

    @Override
    public List<DengueCasesReport> getAllDengueCasesReports() {
        return dengueCasesReportPagingRepository.getAll();
    }

    @Override
    public List<DengueCasesReport> getDengueCasesReportsByPeriodType(PeriodType periodType) {
        return null;
    }

    @Override
    public List<DengueCasesReport> getAllDataRead() {
        return dengueCasesReportPagingRepository.getDataReadAll();
    }

    @Override
    public List<DengueCasesReport> getUserDataRead(User user) {
        return dengueCasesReportPagingRepository.getDataReadAll(user);
    }

    @Override
    public List<DengueCasesReport> getAllDataWrite() {
        return dengueCasesReportPagingRepository.getDataWriteAll();
    }

    @Override
    public List<DengueCasesReport> getUserDataWrite(User user) {
        return dengueCasesReportPagingRepository.getDataWriteAll(user);
    }
}
