package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class DataInputPeriodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataInputPeriod.class);
        DataInputPeriod dataInputPeriod1 = new DataInputPeriod();
        dataInputPeriod1.setId(1L);
        DataInputPeriod dataInputPeriod2 = new DataInputPeriod();
        dataInputPeriod2.setId(dataInputPeriod1.getId());
        assertThat(dataInputPeriod1).isEqualTo(dataInputPeriod2);
        dataInputPeriod2.setId(2L);
        assertThat(dataInputPeriod1).isNotEqualTo(dataInputPeriod2);
        dataInputPeriod1.setId(null);
        assertThat(dataInputPeriod1).isNotEqualTo(dataInputPeriod2);
    }
}
