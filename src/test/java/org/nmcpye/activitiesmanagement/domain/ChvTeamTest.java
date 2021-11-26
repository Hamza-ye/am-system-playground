package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class ChvTeamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChvTeam.class);
        ChvTeam chvTeam1 = new ChvTeam();
        chvTeam1.setId(1L);
        ChvTeam chvTeam2 = new ChvTeam();
        chvTeam2.setId(chvTeam1.getId());
        assertThat(chvTeam1).isEqualTo(chvTeam2);
        chvTeam2.setId(2L);
        assertThat(chvTeam1).isNotEqualTo(chvTeam2);
        chvTeam1.setId(null);
        assertThat(chvTeam1).isNotEqualTo(chvTeam2);
    }
}
