package com.apache.fineract.cn.insurance.repository;

import com.apache.fineract.cn.insurance.domain.PaymentSchedule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PaymentSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
}
