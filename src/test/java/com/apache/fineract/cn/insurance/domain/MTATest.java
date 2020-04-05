package com.apache.fineract.cn.insurance.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.apache.fineract.cn.insurance.web.rest.TestUtil;

public class MTATest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MTA.class);
        MTA mTA1 = new MTA();
        mTA1.setId(1L);
        MTA mTA2 = new MTA();
        mTA2.setId(mTA1.getId());
        assertThat(mTA1).isEqualTo(mTA2);
        mTA2.setId(2L);
        assertThat(mTA1).isNotEqualTo(mTA2);
        mTA1.setId(null);
        assertThat(mTA1).isNotEqualTo(mTA2);
    }
}
