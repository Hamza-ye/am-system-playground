package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class LlinsFamilyTargetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsFamilyTarget.class);
        LlinsFamilyTarget lLINSFamilyTarget1 = new LlinsFamilyTarget();
        lLINSFamilyTarget1.setId(1L);
        LlinsFamilyTarget lLINSFamilyTarget2 = new LlinsFamilyTarget();
        lLINSFamilyTarget2.setId(lLINSFamilyTarget1.getId());
        assertThat(lLINSFamilyTarget1).isEqualTo(lLINSFamilyTarget2);
        lLINSFamilyTarget2.setId(2L);
        assertThat(lLINSFamilyTarget1).isNotEqualTo(lLINSFamilyTarget2);
        lLINSFamilyTarget1.setId(null);
        assertThat(lLINSFamilyTarget1).isNotEqualTo(lLINSFamilyTarget2);
    }
}
