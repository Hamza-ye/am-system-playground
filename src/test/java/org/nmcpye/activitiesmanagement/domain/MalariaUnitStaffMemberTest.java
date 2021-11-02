package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class MalariaUnitStaffMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MalariaUnitStaffMember.class);
        MalariaUnitStaffMember malariaUnitStaffMember1 = new MalariaUnitStaffMember();
        malariaUnitStaffMember1.setId(1L);
        MalariaUnitStaffMember malariaUnitStaffMember2 = new MalariaUnitStaffMember();
        malariaUnitStaffMember2.setId(malariaUnitStaffMember1.getId());
        assertThat(malariaUnitStaffMember1).isEqualTo(malariaUnitStaffMember2);
        malariaUnitStaffMember2.setId(2L);
        assertThat(malariaUnitStaffMember1).isNotEqualTo(malariaUnitStaffMember2);
        malariaUnitStaffMember1.setId(null);
        assertThat(malariaUnitStaffMember1).isNotEqualTo(malariaUnitStaffMember2);
    }
}
