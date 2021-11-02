package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class WHMovementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WHMovement.class);
        WHMovement wHMovement1 = new WHMovement();
        wHMovement1.setId(1L);
        WHMovement wHMovement2 = new WHMovement();
        wHMovement2.setId(wHMovement1.getId());
        assertThat(wHMovement1).isEqualTo(wHMovement2);
        wHMovement2.setId(2L);
        assertThat(wHMovement1).isNotEqualTo(wHMovement2);
        wHMovement1.setId(null);
        assertThat(wHMovement1).isNotEqualTo(wHMovement2);
    }
}
