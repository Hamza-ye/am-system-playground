package org.nmcpye.activitiesmanagement.extended.dataset;

import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.domain.period.PeriodType;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

import java.util.List;

/**
 * Created by Hamza on 20/10/2021.
 */
public interface DataSetStore extends IdentifiableObjectStore<DataSet> {
    String ID = DataSetStore.class.getName();

    /**
     * Gets all DataSets associated with the given PeriodType.
     *
     * @param periodType the PeriodType.
     * @return a list of DataSets.
     */
    List<DataSet> getDataSetsByPeriodType(PeriodType periodType);

    /**
     * Gets all data sets which are not assigned to any organisation units.
     */
    List<DataSet> getDataSetsNotAssignedToOrganisationUnits();
}
