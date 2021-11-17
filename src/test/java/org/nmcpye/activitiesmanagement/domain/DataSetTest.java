package org.nmcpye.activitiesmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.domain.dataset.DataSet;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

class DataSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataSet.class);
        DataSet dataSet1 = new DataSet();
        dataSet1.setId(1L);
        DataSet dataSet2 = new DataSet();
        dataSet2.setId(dataSet1.getId());
        assertThat(dataSet1).isEqualTo(dataSet2);
        dataSet2.setId(2L);
        assertThat(dataSet1).isNotEqualTo(dataSet2);
        dataSet1.setId(null);
        assertThat(dataSet1).isNotEqualTo(dataSet2);
    }
}
