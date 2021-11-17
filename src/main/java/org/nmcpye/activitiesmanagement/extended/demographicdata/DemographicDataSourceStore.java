package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 17/11/2021.
 */
public interface DemographicDataSourceStore extends IdentifiableObjectStore<DemographicDataSource> {
    String ID = DemographicDataSourceStore.class.getName();
}
