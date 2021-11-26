package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class LlinsFamilyTargetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsFamilyTarget.class);
        LlinsFamilyTarget llinsFamilyTarget1 = new LlinsFamilyTarget();
        llinsFamilyTarget1.setId(1L);
        LlinsFamilyTarget llinsFamilyTarget2 = new LlinsFamilyTarget();
        llinsFamilyTarget2.setId(llinsFamilyTarget1.getId());
        assertThat(llinsFamilyTarget1).isEqualTo(llinsFamilyTarget2);
        llinsFamilyTarget2.setId(2L);
        assertThat(llinsFamilyTarget1).isNotEqualTo(llinsFamilyTarget2);
        llinsFamilyTarget1.setId(null);
        assertThat(llinsFamilyTarget1).isNotEqualTo(llinsFamilyTarget2);
    }
}
