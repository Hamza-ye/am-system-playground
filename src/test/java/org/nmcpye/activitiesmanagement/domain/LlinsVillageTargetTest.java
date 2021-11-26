package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class LlinsVillageTargetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LlinsVillageTarget.class);
        LlinsVillageTarget llinsVillageTarget1 = new LlinsVillageTarget();
        llinsVillageTarget1.setId(1L);
        LlinsVillageTarget llinsVillageTarget2 = new LlinsVillageTarget();
        llinsVillageTarget2.setId(llinsVillageTarget1.getId());
        assertThat(llinsVillageTarget1).isEqualTo(llinsVillageTarget2);
        llinsVillageTarget2.setId(2L);
        assertThat(llinsVillageTarget1).isNotEqualTo(llinsVillageTarget2);
        llinsVillageTarget1.setId(null);
        assertThat(llinsVillageTarget1).isNotEqualTo(llinsVillageTarget2);
    }
}
