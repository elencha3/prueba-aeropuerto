package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AirportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Airport.class);
        Airport airport1 = new Airport();
        airport1.setId(1L);
        Airport airport2 = new Airport();
        airport2.setId(airport1.getId());
        assertThat(airport1).isEqualTo(airport2);
        airport2.setId(2L);
        assertThat(airport1).isNotEqualTo(airport2);
        airport1.setId(null);
        assertThat(airport1).isNotEqualTo(airport2);
    }
}
