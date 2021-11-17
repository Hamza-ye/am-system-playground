package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.dataset.MalariaCasesReport;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface MalariaCasesReportStore extends IdentifiableObjectStore<MalariaCasesReport> {
    String ID = MalariaCasesReportStore.class.getName();
}
