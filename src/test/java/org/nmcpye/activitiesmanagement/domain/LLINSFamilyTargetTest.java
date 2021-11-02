package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class LLINSFamilyTargetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LLINSFamilyTarget.class);
        LLINSFamilyTarget lLINSFamilyTarget1 = new LLINSFamilyTarget();
        lLINSFamilyTarget1.setId(1L);
        LLINSFamilyTarget lLINSFamilyTarget2 = new LLINSFamilyTarget();
        lLINSFamilyTarget2.setId(lLINSFamilyTarget1.getId());
        assertThat(lLINSFamilyTarget1).isEqualTo(lLINSFamilyTarget2);
        lLINSFamilyTarget2.setId(2L);
        assertThat(lLINSFamilyTarget1).isNotEqualTo(lLINSFamilyTarget2);
        lLINSFamilyTarget1.setId(null);
        assertThat(lLINSFamilyTarget1).isNotEqualTo(lLINSFamilyTarget2);
    }
}
