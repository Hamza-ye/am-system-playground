package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class PersonAuthorityGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonAuthorityGroup.class);
        PersonAuthorityGroup personAuthorityGroup1 = new PersonAuthorityGroup();
        personAuthorityGroup1.setId(1L);
        PersonAuthorityGroup personAuthorityGroup2 = new PersonAuthorityGroup();
        personAuthorityGroup2.setId(personAuthorityGroup1.getId());
        assertThat(personAuthorityGroup1).isEqualTo(personAuthorityGroup2);
        personAuthorityGroup2.setId(2L);
        assertThat(personAuthorityGroup1).isNotEqualTo(personAuthorityGroup2);
        personAuthorityGroup1.setId(null);
        assertThat(personAuthorityGroup1).isNotEqualTo(personAuthorityGroup2);
    }
}
