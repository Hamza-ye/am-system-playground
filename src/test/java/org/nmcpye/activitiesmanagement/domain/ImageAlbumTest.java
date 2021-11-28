package org.nmcpye.activitiesmanagement.domain;

import org.junit.jupiter.api.Test;
import org.nmcpye.activitiesmanagement.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class ImageAlbumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageAlbum.class);
        ImageAlbum imageAlbum1 = new ImageAlbum();
        imageAlbum1.setId(1L);
        ImageAlbum imageAlbum2 = new ImageAlbum();
        imageAlbum2.setId(imageAlbum1.getId());
        assertThat(imageAlbum1).isEqualTo(imageAlbum2);
        imageAlbum2.setId(2L);
        assertThat(imageAlbum1).isNotEqualTo(imageAlbum2);
        imageAlbum1.setId(null);
        assertThat(imageAlbum1).isNotEqualTo(imageAlbum2);
    }
}
