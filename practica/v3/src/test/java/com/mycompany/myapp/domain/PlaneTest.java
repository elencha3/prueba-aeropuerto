package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plane.class);
        Plane plane1 = new Plane();
        plane1.setId(1L);
        Plane plane2 = new Plane();
        plane2.setId(plane1.getId());
        assertThat(plane1).isEqualTo(plane2);
        plane2.setId(2L);
        assertThat(plane1).isNotEqualTo(plane2);
        plane1.setId(null);
        assertThat(plane1).isNotEqualTo(plane2);
    }
}
