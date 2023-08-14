package com.chubb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chubb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CSEUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CSEUser.class);
        CSEUser cSEUser1 = new CSEUser();
        cSEUser1.setId(1L);
        CSEUser cSEUser2 = new CSEUser();
        cSEUser2.setId(cSEUser1.getId());
        assertThat(cSEUser1).isEqualTo(cSEUser2);
        cSEUser2.setId(2L);
        assertThat(cSEUser1).isNotEqualTo(cSEUser2);
        cSEUser1.setId(null);
        assertThat(cSEUser1).isNotEqualTo(cSEUser2);
    }
}
