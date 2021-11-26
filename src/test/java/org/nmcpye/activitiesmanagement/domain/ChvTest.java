package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.chv.Chv;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class ChvTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chv.class);
        Chv chv1 = new Chv();
        chv1.setId(1L);
        Chv chv2 = new Chv();
        chv2.setId(chv1.getId());
        assertThat(chv1).isEqualTo(chv2);
        chv2.setId(2L);
        assertThat(chv1).isNotEqualTo(chv2);
        chv1.setId(null);
        assertThat(chv1).isNotEqualTo(chv2);
    }
}
