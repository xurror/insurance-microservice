package com.apache.fineract.cn.insurance.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.apache.fineract.cn.insurance.web.rest.TestUtil;

public class PolicyHolderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PolicyHolder.class);
        PolicyHolder policyHolder1 = new PolicyHolder();
        policyHolder1.setId(1L);
        PolicyHolder policyHolder2 = new PolicyHolder();
        policyHolder2.setId(policyHolder1.getId());
        assertThat(policyHolder1).isEqualTo(policyHolder2);
        policyHolder2.setId(2L);
        assertThat(policyHolder1).isNotEqualTo(policyHolder2);
        policyHolder1.setId(null);
        assertThat(policyHolder1).isNotEqualTo(policyHolder2);
    }
}
