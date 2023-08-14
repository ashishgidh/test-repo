package com.chubb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chubb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LOBTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LOB.class);
        LOB lOB1 = new LOB();
        lOB1.setId(1L);
        LOB lOB2 = new LOB();
        lOB2.setId(lOB1.getId());
        assertThat(lOB1).isEqualTo(lOB2);
        lOB2.setId(2L);
        assertThat(lOB1).isNotEqualTo(lOB2);
        lOB1.setId(null);
        assertThat(lOB1).isNotEqualTo(lOB2);
    }
}
