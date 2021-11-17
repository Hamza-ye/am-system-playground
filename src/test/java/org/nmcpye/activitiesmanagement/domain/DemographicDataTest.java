package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.demographicdata.DemographicData;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class DemographicDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemographicData.class);
        DemographicData demographicData1 = new DemographicData();
        demographicData1.setId(1L);
        DemographicData demographicData2 = new DemographicData();
        demographicData2.setId(demographicData1.getId());
        assertThat(demographicData1).isEqualTo(demographicData2);
        demographicData2.setId(2L);
        assertThat(demographicData1).isNotEqualTo(demographicData2);
        demographicData1.setId(null);
        assertThat(demographicData1).isNotEqualTo(demographicData2);
    }
}
