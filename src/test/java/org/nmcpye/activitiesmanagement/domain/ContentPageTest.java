package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class ContentPageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentPage.class);
        ContentPage contentPage1 = new ContentPage();
        contentPage1.setId(1L);
        ContentPage contentPage2 = new ContentPage();
        contentPage2.setId(contentPage1.getId());
        assertThat(contentPage1).isEqualTo(contentPage2);
        contentPage2.setId(2L);
        assertThat(contentPage1).isNotEqualTo(contentPage2);
        contentPage1.setId(null);
        assertThat(contentPage1).isNotEqualTo(contentPage2);
    }
}
