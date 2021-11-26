package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class LlinsVillageTargetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsVillageTarget.class);
        LlinsVillageTarget lLINSVillageTarget1 = new LlinsVillageTarget();
        lLINSVillageTarget1.setId(1L);
        LlinsVillageTarget lLINSVillageTarget2 = new LlinsVillageTarget();
        lLINSVillageTarget2.setId(lLINSVillageTarget1.getId());
        assertThat(lLINSVillageTarget1).isEqualTo(lLINSVillageTarget2);
        lLINSVillageTarget2.setId(2L);
        assertThat(lLINSVillageTarget1).isNotEqualTo(lLINSVillageTarget2);
        lLINSVillageTarget1.setId(null);
        assertThat(lLINSVillageTarget1).isNotEqualTo(lLINSVillageTarget2);
    }
}
