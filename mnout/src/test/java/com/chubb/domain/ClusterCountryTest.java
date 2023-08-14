package com.chubb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chubb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClusterCountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClusterCountry.class);
        ClusterCountry clusterCountry1 = new ClusterCountry();
        clusterCountry1.setId(1L);
        ClusterCountry clusterCountry2 = new ClusterCountry();
        clusterCountry2.setId(clusterCountry1.getId());
        assertThat(clusterCountry1).isEqualTo(clusterCountry2);
        clusterCountry2.setId(2L);
        assertThat(clusterCountry1).isNotEqualTo(clusterCountry2);
        clusterCountry1.setId(null);
        assertThat(clusterCountry1).isNotEqualTo(clusterCountry2);
    }
}
