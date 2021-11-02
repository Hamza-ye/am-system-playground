package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class WorkingDayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkingDay.class);
        WorkingDay workingDay1 = new WorkingDay();
        workingDay1.setId(1L);
        WorkingDay workingDay2 = new WorkingDay();
        workingDay2.setId(workingDay1.getId());
        assertThat(workingDay1).isEqualTo(workingDay2);
        workingDay2.setId(2L);
        assertThat(workingDay1).isNotEqualTo(workingDay2);
        workingDay1.setId(null);
        assertThat(workingDay1).isNotEqualTo(workingDay2);
    }
}
