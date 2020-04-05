package com.apache.fineract.cn.insurance.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.apache.fineract.cn.insurance.web.rest.TestUtil;

public class EndorsementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Endorsement.class);
        Endorsement endorsement1 = new Endorsement();
        endorsement1.setId(1L);
        Endorsement endorsement2 = new Endorsement();
        endorsement2.setId(endorsement1.getId());
        assertThat(endorsement1).isEqualTo(endorsement2);
        endorsement2.setId(2L);
        assertThat(endorsement1).isNotEqualTo(endorsement2);
        endorsement1.setId(null);
        assertThat(endorsement1).isNotEqualTo(endorsement2);
    }
}
