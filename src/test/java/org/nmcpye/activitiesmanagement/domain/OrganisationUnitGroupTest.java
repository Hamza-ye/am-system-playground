package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class OrganisationUnitGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationUnitGroup.class);
        OrganisationUnitGroup organisationUnitGroup1 = new OrganisationUnitGroup();
        organisationUnitGroup1.setId(1L);
        OrganisationUnitGroup organisationUnitGroup2 = new OrganisationUnitGroup();
        organisationUnitGroup2.setId(organisationUnitGroup1.getId());
        assertThat(organisationUnitGroup1).isEqualTo(organisationUnitGroup2);
        organisationUnitGroup2.setId(2L);
        assertThat(organisationUnitGroup1).isNotEqualTo(organisationUnitGroup2);
        organisationUnitGroup1.setId(null);
        assertThat(organisationUnitGroup1).isNotEqualTo(organisationUnitGroup2);
    }
}
