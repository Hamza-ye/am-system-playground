package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class FamilyHeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyHead.class);
        FamilyHead familyHead1 = new FamilyHead();
        familyHead1.setId(1L);
        FamilyHead familyHead2 = new FamilyHead();
        familyHead2.setId(familyHead1.getId());
        assertThat(familyHead1).isEqualTo(familyHead2);
        familyHead2.setId(2L);
        assertThat(familyHead1).isNotEqualTo(familyHead2);
        familyHead1.setId(null);
        assertThat(familyHead1).isNotEqualTo(familyHead2);
    }
}
