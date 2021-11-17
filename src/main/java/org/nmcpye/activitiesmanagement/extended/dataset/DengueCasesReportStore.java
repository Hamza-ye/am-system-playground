package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.dataset.DengueCasesReport;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface DengueCasesReportStore extends IdentifiableObjectStore<DengueCasesReport> {
    String ID = DengueCasesReportStore.class.getName();
}
