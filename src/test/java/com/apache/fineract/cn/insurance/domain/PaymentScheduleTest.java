package com.apache.fineract.cn.insurance.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.apache.fineract.cn.insurance.web.rest.TestUtil;

public class PaymentScheduleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentSchedule.class);
        PaymentSchedule paymentSchedule1 = new PaymentSchedule();
        paymentSchedule1.setId(1L);
        PaymentSchedule paymentSchedule2 = new PaymentSchedule();
        paymentSchedule2.setId(paymentSchedule1.getId());
        assertThat(paymentSchedule1).isEqualTo(paymentSchedule2);
        paymentSchedule2.setId(2L);
        assertThat(paymentSchedule1).isNotEqualTo(paymentSchedule2);
        paymentSchedule1.setId(null);
        assertThat(paymentSchedule1).isNotEqualTo(paymentSchedule2);
    }
}
