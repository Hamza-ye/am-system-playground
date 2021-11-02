package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class StatusOfCoverageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StatusOfCoverage.class);
        StatusOfCoverage statusOfCoverage1 = new StatusOfCoverage();
        statusOfCoverage1.setId(1L);
        StatusOfCoverage statusOfCoverage2 = new StatusOfCoverage();
        statusOfCoverage2.setId(statusOfCoverage1.getId());
        assertThat(statusOfCoverage1).isEqualTo(statusOfCoverage2);
        statusOfCoverage2.setId(2L);
        assertThat(statusOfCoverage1).isNotEqualTo(statusOfCoverage2);
        statusOfCoverage1.setId(null);
        assertThat(statusOfCoverage1).isNotEqualTo(statusOfCoverage2);
    }
}
