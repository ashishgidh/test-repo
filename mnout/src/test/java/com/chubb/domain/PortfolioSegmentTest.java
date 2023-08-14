package com.chubb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chubb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PortfolioSegmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PortfolioSegment.class);
        PortfolioSegment portfolioSegment1 = new PortfolioSegment();
        portfolioSegment1.setId("id1");
        PortfolioSegment portfolioSegment2 = new PortfolioSegment();
        portfolioSegment2.setId(portfolioSegment1.getId());
        assertThat(portfolioSegment1).isEqualTo(portfolioSegment2);
        portfolioSegment2.setId("id2");
        assertThat(portfolioSegment1).isNotEqualTo(portfolioSegment2);
        portfolioSegment1.setId(null);
        assertThat(portfolioSegment1).isNotEqualTo(portfolioSegment2);
    }
}
