package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class PeopleGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeopleGroup.class);
        PeopleGroup peopleGroup1 = new PeopleGroup();
        peopleGroup1.setId(1L);
        PeopleGroup peopleGroup2 = new PeopleGroup();
        peopleGroup2.setId(peopleGroup1.getId());
        assertThat(peopleGroup1).isEqualTo(peopleGroup2);
        peopleGroup2.setId(2L);
        assertThat(peopleGroup1).isNotEqualTo(peopleGroup2);
        peopleGroup1.setId(null);
        assertThat(peopleGroup1).isNotEqualTo(peopleGroup2);
    }
}
