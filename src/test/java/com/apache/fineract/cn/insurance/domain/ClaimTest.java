package com.apache.fineract.cn.insurance.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.apache.fineract.cn.insurance.web.rest.TestUtil;

public class ClaimTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Claim.class);
        Claim claim1 = new Claim();
        claim1.setId(1L);
        Claim claim2 = new Claim();
        claim2.setId(claim1.getId());
        assertThat(claim1).isEqualTo(claim2);
        claim2.setId(2L);
        assertThat(claim1).isNotEqualTo(claim2);
        claim1.setId(null);
        assertThat(claim1).isNotEqualTo(claim2);
    }
}
