package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class ChvTeamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChvTeam.class);
        ChvTeam cHVTeam1 = new ChvTeam();
        cHVTeam1.setId(1L);
        ChvTeam cHVTeam2 = new ChvTeam();
        cHVTeam2.setId(cHVTeam1.getId());
        assertThat(cHVTeam1).isEqualTo(cHVTeam2);
        cHVTeam2.setId(2L);
        assertThat(cHVTeam1).isNotEqualTo(cHVTeam2);
        cHVTeam1.setId(null);
        assertThat(cHVTeam1).isNotEqualTo(cHVTeam2);
    }
}
