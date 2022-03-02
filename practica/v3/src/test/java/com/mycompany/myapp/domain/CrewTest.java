package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crew.class);
        Crew crew1 = new Crew();
        crew1.setId(1L);
        Crew crew2 = new Crew();
        crew2.setId(crew1.getId());
        assertThat(crew1).isEqualTo(crew2);
        crew2.setId(2L);
        assertThat(crew1).isNotEqualTo(crew2);
        crew1.setId(null);
        assertThat(crew1).isNotEqualTo(crew2);
    }
}
