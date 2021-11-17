package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicDataSource;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class DemographicDataSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemographicDataSource.class);
        DemographicDataSource demographicDataSource1 = new DemographicDataSource();
        demographicDataSource1.setId(1L);
        DemographicDataSource demographicDataSource2 = new DemographicDataSource();
        demographicDataSource2.setId(demographicDataSource1.getId());
        assertThat(demographicDataSource1).isEqualTo(demographicDataSource2);
        demographicDataSource2.setId(2L);
        assertThat(demographicDataSource1).isNotEqualTo(demographicDataSource2);
        demographicDataSource1.setId(null);
        assertThat(demographicDataSource1).isNotEqualTo(demographicDataSource2);
    }
}
