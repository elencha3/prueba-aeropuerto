package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PilotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pilot.class);
        Pilot pilot1 = new Pilot();
        pilot1.setId(1L);
        Pilot pilot2 = new Pilot();
        pilot2.setId(pilot1.getId());
        assertThat(pilot1).isEqualTo(pilot2);
        pilot2.setId(2L);
        assertThat(pilot1).isNotEqualTo(pilot2);
        pilot1.setId(null);
        assertThat(pilot1).isNotEqualTo(pilot2);
    }
}
