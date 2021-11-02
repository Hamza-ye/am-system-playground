package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class OrganisationUnitGroupSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationUnitGroupSet.class);
        OrganisationUnitGroupSet organisationUnitGroupSet1 = new OrganisationUnitGroupSet();
        organisationUnitGroupSet1.setId(1L);
        OrganisationUnitGroupSet organisationUnitGroupSet2 = new OrganisationUnitGroupSet();
        organisationUnitGroupSet2.setId(organisationUnitGroupSet1.getId());
        assertThat(organisationUnitGroupSet1).isEqualTo(organisationUnitGroupSet2);
        organisationUnitGroupSet2.setId(2L);
        assertThat(organisationUnitGroupSet1).isNotEqualTo(organisationUnitGroupSet2);
        organisationUnitGroupSet1.setId(null);
        assertThat(organisationUnitGroupSet1).isNotEqualTo(organisationUnitGroupSet2);
    }
}
