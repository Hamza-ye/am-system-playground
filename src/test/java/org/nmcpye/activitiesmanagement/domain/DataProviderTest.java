package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class DataProviderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataProvider.class);
        DataProvider dataProvider1 = new DataProvider();
        dataProvider1.setId(1L);
        DataProvider dataProvider2 = new DataProvider();
        dataProvider2.setId(dataProvider1.getId());
        assertThat(dataProvider1).isEqualTo(dataProvider2);
        dataProvider2.setId(2L);
        assertThat(dataProvider1).isNotEqualTo(dataProvider2);
        dataProvider1.setId(null);
        assertThat(dataProvider1).isNotEqualTo(dataProvider2);
    }
}
