package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.chv.CHV;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class CHVTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CHV.class);
        CHV cHV1 = new CHV();
        cHV1.setId(1L);
        CHV cHV2 = new CHV();
        cHV2.setId(cHV1.getId());
        assertThat(cHV1).isEqualTo(cHV2);
        cHV2.setId(2L);
        assertThat(cHV1).isNotEqualTo(cHV2);
        cHV1.setId(null);
        assertThat(cHV1).isNotEqualTo(cHV2);
    }
}
