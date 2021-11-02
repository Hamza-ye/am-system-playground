package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class OrganisationUnitLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationUnitLevel.class);
        OrganisationUnitLevel organisationUnitLevel1 = new OrganisationUnitLevel();
        organisationUnitLevel1.setId(1L);
        OrganisationUnitLevel organisationUnitLevel2 = new OrganisationUnitLevel();
        organisationUnitLevel2.setId(organisationUnitLevel1.getId());
        assertThat(organisationUnitLevel1).isEqualTo(organisationUnitLevel2);
        organisationUnitLevel2.setId(2L);
        assertThat(organisationUnitLevel1).isNotEqualTo(organisationUnitLevel2);
        organisationUnitLevel1.setId(null);
        assertThat(organisationUnitLevel1).isNotEqualTo(organisationUnitLevel2);
    }
}
