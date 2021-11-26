package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class WhMovementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WhMovement.class);
        WhMovement whMovement1 = new WhMovement();
        whMovement1.setId(1L);
        WhMovement whMovement2 = new WhMovement();
        whMovement2.setId(whMovement1.getId());
        assertThat(whMovement1).isEqualTo(whMovement2);
        whMovement2.setId(2L);
        assertThat(whMovement1).isNotEqualTo(whMovement2);
        whMovement1.setId(null);
        assertThat(whMovement1).isNotEqualTo(whMovement2);
    }
}
