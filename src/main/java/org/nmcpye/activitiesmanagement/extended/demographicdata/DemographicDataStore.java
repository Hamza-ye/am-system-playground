package org.nmcpye.activitiesmanagement.extended.demographicdata;

import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.extended.common.IdentifiableObjectStore;

/**
 * Created by Hamza on 16/11/2021.
 */
public interface DemographicDataStore extends IdentifiableObjectStore<DemographicData> {
    String ID = DemographicDataStore.class.getName();
}
