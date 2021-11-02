package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class FingerprintTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fingerprint.class);
        Fingerprint fingerprint1 = new Fingerprint();
        fingerprint1.setId(1L);
        Fingerprint fingerprint2 = new Fingerprint();
        fingerprint2.setId(fingerprint1.getId());
        assertThat(fingerprint1).isEqualTo(fingerprint2);
        fingerprint2.setId(2L);
        assertThat(fingerprint1).isNotEqualTo(fingerprint2);
        fingerprint1.setId(null);
        assertThat(fingerprint1).isNotEqualTo(fingerprint2);
    }
}
