package com.chubb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chubb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Branch.class);
        Branch branch1 = new Branch();
        branch1.setId("id1");
        Branch branch2 = new Branch();
        branch2.setId(branch1.getId());
        assertThat(branch1).isEqualTo(branch2);
        branch2.setId("id2");
        assertThat(branch1).isNotEqualTo(branch2);
        branch1.setId(null);
        assertThat(branch1).isNotEqualTo(branch2);
    }
}
