package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class MalariaUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MalariaUnit.class);
        MalariaUnit malariaUnit1 = new MalariaUnit();
        malariaUnit1.setId(1L);
        MalariaUnit malariaUnit2 = new MalariaUnit();
        malariaUnit2.setId(malariaUnit1.getId());
        assertThat(malariaUnit1).isEqualTo(malariaUnit2);
        malariaUnit2.setId(2L);
        assertThat(malariaUnit1).isNotEqualTo(malariaUnit2);
        malariaUnit1.setId(null);
        assertThat(malariaUnit1).isNotEqualTo(malariaUnit2);
    }
}
